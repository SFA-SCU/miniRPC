package cn.scu.rpc.context;

import cn.scu.rpc.proxy.ResponseFuture;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * ClassName: RpcContext
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/14 22:51
 * Version V1.0
 * Description:
 */

public class RpcContext {


    private ResponseFuture responseFuture;

    private Map<String,Object> contextParameters;

    public ResponseFuture getResponseFuture() {
        return responseFuture;
    }

    public void addContextParameter(String key,Object value){
        this.getContextParameters().put(key,value);
    }

    public Object getContextParameter(String key){
        return this.getContextParameters().get(key);
    }

    public void setResponseFuture(ResponseFuture responseFuture) {
        System.out.println("current thread id:"+Thread.currentThread().getId());
        this.responseFuture = responseFuture;
    }

    public Map<String, Object> getContextParameters() {
        return contextParameters;
    }

    public void setContextParameters(Map<String, Object> contextParameters) {
        this.contextParameters = contextParameters;
    }

    private static final ThreadLocal<RpcContext> rpcContextThreadLocal=new ThreadLocal<RpcContext>(){
        @Override
        protected RpcContext initialValue() {
            RpcContext context= new RpcContext();
            context.setContextParameters(Maps.newHashMap());
            return context;
        }
    };

    public static RpcContext getContext() {
        return rpcContextThreadLocal.get();
    }

    public static void removeContext() {


        rpcContextThreadLocal.remove();
    }

    private RpcContext() {
    }

}
