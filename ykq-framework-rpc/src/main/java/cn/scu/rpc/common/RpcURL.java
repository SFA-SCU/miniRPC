package cn.scu.rpc.common;

/**
 * ClassName: RpcURL
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/14 10:24
 * Version V1.0
 * Description:
 */

public class RpcURL {

    private String host;
    private int port;

    private String registryHost;
    private int registryPort;

    private String serviceName;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRegistryHost() {
        return registryHost;
    }

    public void setRegistryHost(String registryHost) {
        this.registryHost = registryHost;
    }

    public int getRegistryPort() {
        return registryPort;
    }

    public void setRegistryPort(int registryPort) {
        this.registryPort = registryPort;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
