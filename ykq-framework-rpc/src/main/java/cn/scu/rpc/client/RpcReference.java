package cn.scu.rpc.client;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcReference {

    boolean isSync() default true;

    /**
     * 客户端最大并发数
     * @return
     */
    int maxExecutesCount() default 10;

    /**
     * 服务降级的伪装者类对象
     * @return
     */
    Class<?> fallbackServiceClazz() default Object.class;
}
