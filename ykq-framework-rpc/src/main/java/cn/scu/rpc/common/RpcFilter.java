package cn.scu.rpc.common;

/**
 * ClassName: RpcFilter
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/14 10:23
 * Version V1.0
 * Description:
 */

public interface RpcFilter<T> {

    <T> T invoke(RpcInvoker invoker, RpcInvocation invocation);
}