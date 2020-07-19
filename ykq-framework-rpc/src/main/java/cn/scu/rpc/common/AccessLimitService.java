package cn.scu.rpc.common;

/**
 * Interface: AccessLimitService
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/15 0:21
 * Version V1.0
 * Description:
 */
public interface AccessLimitService {

    void acquire(RpcInvocation invocation);
}
