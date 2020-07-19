package cn.scu.rpc.common;

/**
 * ClassName: RpcInvoker
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/14 13:45
 * Version V1.0
 * Description:
 */

public interface RpcInvoker {

    Object invoke(RpcInvocation invocation);
}
