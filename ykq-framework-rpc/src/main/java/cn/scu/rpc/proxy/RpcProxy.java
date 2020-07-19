package cn.scu.rpc.proxy;

import cn.scu.rpc.client.RpcReference;
import cn.scu.rpc.config.ReferenceConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * ClassName: RpcProxy
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/14 15:07
 * Version V1.0
 * Description:
 */

public class RpcProxy <T> implements InvocationHandler {


    private Class<T> clazz;

    private boolean isSync = true;

    private ReferenceConfig referenceConfig;

    private RpcReference reference;


    public RpcProxy(Class<T> clazz, ReferenceConfig referenceConfig,RpcReference reference) {

        this.clazz = clazz;
        this.referenceConfig = referenceConfig;
        this.reference = reference;
        this.isSync = reference.isSync();
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcHystrixCommand rpcHystrixCommand = new RpcHystrixCommand(proxy, method,args,this.reference,this.referenceConfig);
        return rpcHystrixCommand.execute();
    }
}
