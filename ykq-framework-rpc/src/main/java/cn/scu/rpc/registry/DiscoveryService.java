package cn.scu.rpc.registry;

import cn.scu.rpc.common.RpcURL;

import java.util.List;

/**
 * Created by jiang on 2017/5/19.
 */
public interface DiscoveryService {

    List<RpcURL> getUrls(String registryHost, int registryPort);
}
