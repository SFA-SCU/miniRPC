package cn.scu.rpc.registry;

import cn.scu.rpc.common.RpcURL;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ClassName: ZookeeperRegistryService
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/27 10:52
 * Version V1.0
 * Description:
 */

@Component
public class ZookeeperRegistryService  implements RegistryService{

    @Resource(name = "zkClient")
    private ZooKeeper zkClient;

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfig.class);

    @Override
    public void register(RpcURL url)  {

        String path = "/"+url.getServiceName();

        try {

            if(zkClient.exists(path,false) == null){

                zkClient.create(path,"veraion".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            String address = path+"/"+ url.getHost() + ":"+url.getPort();

            if(zkClient.exists(address,true) == null){

                zkClient.create(address,"1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            }

        } catch (KeeperException e) {

            logger.error("【创建节点异常....】={}",e);
        } catch (InterruptedException e) {

            logger.error("【创建节点异常....】={}",e);
        }
    }

    @Override
    public void unregister(RpcURL url) {

    }
}
