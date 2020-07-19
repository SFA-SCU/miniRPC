package cn.scu.rpc.registry;

import cn.scu.rpc.common.RpcURL;
import cn.scu.rpc.config.ReferenceConfig;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: ZookeeperDiscoveryService
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/27 19:52
 * Version V1.0
 * Description:
 */

@Configuration
public class ZookeeperDiscoveryService implements DiscoveryService{

    @Resource(name = "zkClient")
    private ZooKeeper zkClient;


    public void setReferenceConfig(ReferenceConfig referenceConfig) {
        this.referenceConfig = referenceConfig;
    }

    private ReferenceConfig referenceConfig;


    private String registryAddress = "127.0.0.1";

    // @Value("timeout")
    private int timeout = 2000;

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfig.class);


    @Override
    public List<RpcURL> getUrls(String registryHost, int registryPort) {

        List<String> serviceLists = referenceConfig.getServiceLists();

        List<RpcURL> lists = new ArrayList<RpcURL>();

        for (String serviceList : serviceLists) {

            try {

                List<String> addressAndPorts = zkClient.getChildren("/"+serviceList,true);

                for (String addressAndPort : addressAndPorts) {

                    //将获得的ip和端口进行分离
                    String[] strs = addressAndPort.split(":");

                    RpcURL url = new RpcURL();
                    url.setServiceName(serviceList);
                    url.setHost(strs[0]);
                    url.setPort(Integer.valueOf(strs[1]));
                    url.setRegistryHost(registryAddress);
                    url.setRegistryPort(2181);
                    lists.add(url);
                }
            } catch (Exception e) {
                logger.error("【获取zookeeper节点值异常....】={}",e);
            }

        }

        return lists;
    }
}
