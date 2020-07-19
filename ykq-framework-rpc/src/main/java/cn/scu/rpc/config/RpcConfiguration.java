package cn.scu.rpc.config;

import cn.scu.rpc.util.ApplicationContextUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RpcConfiguration {

    @Bean
    public ApplicationContextUtils applicationContextUtils(){

     //   System.out.println("applicationContext初始化");
        return new ApplicationContextUtils();
    }
}
