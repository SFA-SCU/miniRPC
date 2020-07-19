package cn.scu.rpc.registry;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * ClassName: AbstractZookeeperService
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/27 10:45
 * Version V1.0
 * Description:
 */

@Configuration
@ConfigurationProperties(prefix = "zookeeper")
public class ZookeeperConfig {


    private String registryAddress = "127.0.0.1:2181";

   // @Value("timeout")
    private int timeout = 2000;

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfig.class);
   // private static final Logger LOGGER = LoggerFactory.getLogger(AbstractZookeeperService.class);

    @Bean(name = "zkClient")
    public ZooKeeper zkClient() {

        ZooKeeper zk = null;
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            zk = new ZooKeeper(registryAddress, timeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {

                    if(Event.KeeperState.SyncConnected ==event.getState()){
                        //如果收到了服务端的响应事件,连接成功
                        countDownLatch.countDown();
                    }
                }
            });

            countDownLatch.await();
            logger.info("【初始化ZooKeeper连接状态....】={}",zk.getState());
        } catch (IOException | InterruptedException e) {
        //    LOGGER.error("", e);
            logger.error("初始化ZooKeeper连接异常....】={}",e);
        }

        return zk;
    }
}
