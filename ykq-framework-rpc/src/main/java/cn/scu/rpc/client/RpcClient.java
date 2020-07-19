package cn.scu.rpc.client;

import cn.scu.rpc.common.RpcURL;
import cn.scu.rpc.config.ReferenceConfig;
import cn.scu.rpc.loadbalance.LoadbalanceService;
import cn.scu.rpc.loadbalance.RoundRobinLoadbalanceService;
import cn.scu.rpc.proxy.RpcProxy;
import cn.scu.rpc.registry.ConsulDiscoveryService;
import cn.scu.rpc.registry.DiscoveryService;
import cn.scu.rpc.registry.ZookeeperDiscoveryService;
import cn.scu.rpc.util.ApplicationContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * ClassName: RpcClient
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/14 10:02
 * Version V1.0
 * Description:
 */


public class RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private ReferenceConfig referenceConfig;

    private static LoadbalanceService loadbalanceService = new RoundRobinLoadbalanceService();


    public RpcClient(ReferenceConfig referenceConfig) {


        this.referenceConfig = referenceConfig;
        DiscoveryService discoveryService = new ConsulDiscoveryService();

        if(ApplicationContextUtils.getApplicationContext() == null){

            logger.info("ApplicationContextUtils.getApplicationContext()未初始化完成");
            System.out.println();
        }

       ZookeeperDiscoveryService zkDiscoveryService  = ApplicationContextUtils.getApplicationContext().getBean(ZookeeperDiscoveryService.class);
//
        zkDiscoveryService.setReferenceConfig(referenceConfig);

        List<RpcURL> zkurls = zkDiscoveryService.getUrls(referenceConfig.getRegistryHost(),referenceConfig.getPort());


        List<RpcURL> urls = discoveryService.getUrls(referenceConfig.getRegistryHost(),referenceConfig.getRegistryPort());

        int size = urls.size();

        //负载均衡，轮询随机
        int index = loadbalanceService.index(size);
        logger.info("RpcClient init");
        RpcURL url = urls.get(index);
        this.referenceConfig.setHost(url.getHost());
        this.referenceConfig.setPort(url.getPort());
    }

    @SuppressWarnings("unchecked")
    public <T> T createProxy(Class<T> interfaceClass,RpcReference reference) {

        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new RpcProxy<T>(interfaceClass,this.referenceConfig,reference)
        );
    }

}
