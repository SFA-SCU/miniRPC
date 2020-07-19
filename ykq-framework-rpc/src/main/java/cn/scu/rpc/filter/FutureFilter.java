package cn.scu.rpc.filter;

import cn.scu.rpc.common.RpcFilter;
import cn.scu.rpc.common.RpcInvocation;
import cn.scu.rpc.common.RpcInvoker;
import cn.scu.rpc.config.ConstantConfig;
import cn.scu.rpc.context.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jiang on 2017/5/14.
 */
@ActiveFilter(group = {ConstantConfig.CONSUMER,ConstantConfig.PROVIDER})
public class FutureFilter implements RpcFilter {

    private final static Logger logger = LoggerFactory.getLogger(FutureFilter.class);

    @Override
    public Object invoke(RpcInvoker invoker, RpcInvocation invocation) {
        Object rpcResponse=invoker.invoke(invocation);
        logger.info("clear future");
        RpcContext.removeContext();
        return rpcResponse;
    }
}
