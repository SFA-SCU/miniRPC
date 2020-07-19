package cn.scu.rpc.threadpool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * ClassName: RpcThreadPoolFactory
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/14 14:32
 * Version V1.0
 * Description:
 */

@Component
public class RpcThreadPoolFactory {

    @Autowired
    private Map<String,RpcThreadPool> rpcThreadPoolMap;

    public RpcThreadPool getThreadPool(String threadPoolName){
        return this.rpcThreadPoolMap.get(threadPoolName);
    }
}
