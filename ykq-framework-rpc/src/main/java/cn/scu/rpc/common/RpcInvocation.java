package cn.scu.rpc.common;

import java.util.Map;

/**
 * Interface: RpcInvocation
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/14 10:29
 * Version V1.0
 * Description:
 */
public interface RpcInvocation {

    String getMethodName();

    String getClassName();

    String getRequestId();

    Class<?>[] getParameterTypes();

    Object[] getParameters();

    int getMaxExecutesCount();

    Map<String,Object> getContextParameters();

}
