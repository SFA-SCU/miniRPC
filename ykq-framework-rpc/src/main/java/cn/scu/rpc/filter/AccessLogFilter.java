package cn.scu.rpc.filter;

import cn.scu.rpc.common.RpcFilter;
import cn.scu.rpc.common.RpcInvocation;
import cn.scu.rpc.common.RpcInvoker;
import cn.scu.rpc.config.ConstantConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jiang on 2017/5/14.
 */
@ActiveFilter(group = {ConstantConfig.PROVIDER,ConstantConfig.CONSUMER})
public class AccessLogFilter implements RpcFilter {

    private final static Logger logger = LoggerFactory.getLogger(AccessLogFilter.class);

    @Override
    public Object invoke(RpcInvoker invoker, RpcInvocation invocation) {
        logger.info("before call");
        Object rpcResponse=invoker.invoke(invocation);
        logger.info("after call");
        return rpcResponse;
    }
}
