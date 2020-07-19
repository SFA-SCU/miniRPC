package cn.scu.rpc.loadbalance;

import cn.scu.rpc.client.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jiang on 2017/5/22.
 */
public class RoundRobinLoadbalanceService implements LoadbalanceService {

    private static final Logger logger = LoggerFactory.getLogger(RoundRobinLoadbalanceService.class);

    private AtomicInteger roundRobin = new AtomicInteger(0);
    private static final int MAX_VALUE=1000;
    private static final int MIN_VALUE=1;

    private AtomicInteger getRoundRobinValue(){

        if(this.roundRobin.getAndAdd(1)>MAX_VALUE){
            this.roundRobin.set(MIN_VALUE);
        }
        return this.roundRobin;
    }

    @Override
    public int index(int size) {

        if(size == 0){

            logger.warn("the size is zreo");
            return 0;
        }
        return  (this.getRoundRobinValue().get() + size) % size;
    }
}
