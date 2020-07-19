package cn.scu.rpc.threadpool;

import java.util.concurrent.Executor;

/**
 * ClassName: RpcThreadPool
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/14 14:34
 * Version V1.0
 * Description:
 */

public interface RpcThreadPool {

    Executor getExecutor(int threadSize, int queues);
}
