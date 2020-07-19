package cn.scu.rpc.filter;

import cn.scu.rpc.common.RpcFilter;
import cn.scu.rpc.common.RpcInvocation;
import cn.scu.rpc.common.RpcInvoker;
import cn.scu.rpc.config.ConstantConfig;
import cn.scu.rpc.context.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by jiang on 2017/5/14.
 */
@ActiveFilter(group = {ConstantConfig.PROVIDER},order = -1000)
public class ServerContextFilter implements RpcFilter {

    private final static Logger logger = LoggerFactory.getLogger(ServerContextFilter.class);

    @Override
    public Object invoke(RpcInvoker invoker, RpcInvocation invocation) {

        Map<String,Object> contextParameters = invocation.getContextParameters();
        RpcContext.getContext().setContextParameters(contextParameters);
        Object rpcResponse =  invoker.invoke(invocation);
        logger.info("ServerContextFilter.invoke end");
        return rpcResponse;
    }
}
