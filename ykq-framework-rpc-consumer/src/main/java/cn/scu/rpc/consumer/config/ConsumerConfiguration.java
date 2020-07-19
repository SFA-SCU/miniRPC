package cn.scu.rpc.consumer.config;

import cn.scu.rpc.annotation.BeanPostPrcessorReference;
import cn.scu.rpc.client.RpcReference;
import cn.scu.rpc.config.ReferenceConfig;
import cn.scu.rpc.consumer.service.ProductCommentService;
import cn.scu.rpc.server.RpcService;
import cn.scu.rpc.util.ApplicationContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ConsumerConfiguration
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/15 0:14
 * Version V1.0
 * Description:
 */



@ComponentScan(basePackages = {"cn.scu.rpc.consumer","cn.scu.rpc","cn.scu.rpc.consumer.service.impl"})
@Configuration
public class ConsumerConfiguration {

    @Autowired
    ApplicationContextUtils applicationContextUtils;

    @Bean
    @Autowired
    public BeanPostPrcessorReference beanPostPrcessorReference(ReferenceConfig referenceConfig){

        return new BeanPostPrcessorReference(referenceConfig);
    }

    @Autowired
    private ApplicationContext applicationContext ;

    @Autowired
    private ProductCommentService productCommentService;
    @Bean
    public ReferenceConfig referenceConfig( ){



        Map<String,Object> map0 = ApplicationContextUtils.getApplicationContext().getBeansWithAnnotation(RpcService.class);
        Map<String,Object> map = applicationContext.getBeansWithAnnotation(RpcReference.class);

        ReferenceConfig referenceConfig = new ReferenceConfig();
        referenceConfig.setRegistryHost("127.0.0.1");

        referenceConfig.setRegistryPort(8500);

        List<String> serviceLists = new ArrayList<String>();
        for (Object value : map.values()) {

            for (Class<?> anInterface : value.getClass().getInterfaces()) {

                serviceLists.add(anInterface.getName());
            }
        }

        serviceLists.add("cn.scu.rpc.api.service.CommentService");
        referenceConfig.setServiceLists(serviceLists);

        return referenceConfig;
    }

}