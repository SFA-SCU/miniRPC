package cn.scu.rpc.proxy;

import cn.scu.rpc.client.RpcClientInvoker;
import cn.scu.rpc.client.RpcClientInvokerManager;
import cn.scu.rpc.client.RpcReference;
import cn.scu.rpc.common.RpcInvoker;
import cn.scu.rpc.common.RpcRequest;
import cn.scu.rpc.config.ReferenceConfig;
import cn.scu.rpc.context.RpcContext;
import cn.scu.rpc.exception.RpcException;
import cn.scu.rpc.util.ApplicationContextUtils;
import com.netflix.hystrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.util.UUID;

/**
 * ClassName: RpcHystrixCommand
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/14 22:46
 * Version V1.0
 * Description:
 */

public class RpcHystrixCommand  extends HystrixCommand {


    private Logger logger = LoggerFactory.getLogger(RpcHystrixCommand.class);

    /**
     * 远程目标方法
     */
    private Method method;

    /**
     * 远程目标接口
     */
    private Object obj;

    /**
     * 远程方法所需要的参数
     */
    private Object[] params;

    /**
     * 远程接口客户端引用注解
     */
    private RpcReference rpcReference;

    /**
     * RPC客户端配置
     */
    private ReferenceConfig referenceConfig;

    public RpcHystrixCommand(Object obj, Method method, Object[] params, RpcReference rpcReference, ReferenceConfig referenceConfig) {

        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CircuitBreakerRpcHystrixCommandGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("CircuitBreakerRpcHystrixCommandKey"))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withCircuitBreakerEnabled(true)
                                .withCircuitBreakerRequestVolumeThreshold(1)
                                .withCircuitBreakerErrorThresholdPercentage(50)
                                .withCircuitBreakerSleepWindowInMilliseconds(5*1000)
                                .withMetricsRollingStatisticalWindowInMilliseconds(10*1000)
                )
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("CircuitBreakerRpcHystrixCommandPool"))
                .andThreadPoolPropertiesDefaults(
                        HystrixThreadPoolProperties.Setter().withCoreSize(100)
                )
        );

        this.obj = obj;
        this.method = method;
        this.params = params;
        this.rpcReference = rpcReference;
        this.referenceConfig = referenceConfig;
    }


    @Override
    protected Object run() throws Exception {

        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(this.params);

        //MappedByteBuffer
        if (this.rpcReference != null) {

            request.setMaxExecutesCount(this.rpcReference.maxExecutesCount());
        }

        request.setContextParameters(RpcContext.getContext().getContextParameters());

        RpcClientInvoker invoker = RpcClientInvokerManager.getInstance(this.referenceConfig).getInvoker();
        invoker.setRpcRequest(request);

        RpcInvoker rpcInvoker = invoker.buildInvokerChain(invoker);
        ResponseFuture response = (ResponseFuture) rpcInvoker.invoke(invoker.buildRpcInvocation(request));

        if(this.rpcReference.isSync()){

            return response.get();
        }else {

            RpcContext.getContext().setResponseFuture(response);
            return null;
        }
    }


    @Override
    protected Object getFallback() {

        Method[] methods = this.rpcReference.fallbackServiceClazz().getMethods();

        for (Method methodFallback : methods) {
            if(this.method.getName().equals(methodFallback.getName())){
                try {

                    Object fallbackServiceMock= ApplicationContextUtils.getApplicationContext().getBean(this.rpcReference.fallbackServiceClazz());
                    return  methodFallback.invoke(fallbackServiceMock,this.params);

                } catch (IllegalAccessException e) {

                    logger.error("RpcHystrixCommand.getFallback error",e);
                } catch (InvocationTargetException e) {

                    logger.error("RpcHystrixCommand.getFallback error",e);
                }
            }
        }
        throw new RpcException("service fallback unimplement");
    }
}
