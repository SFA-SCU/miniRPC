package cn.scu.rpc.config;


import org.springframework.stereotype.Component;

import java.util.List;


public class ReferenceConfig {

    private String host;
    private int port;

    private String registryHost;
    private int registryPort;

    private String serviceName;
    private List<String> serviceLists ;


    public List<String> getServiceLists() {
        return serviceLists;
    }

    public void setServiceLists(List<String> serviceLists) {
        this.serviceLists = serviceLists;
    }

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

    protected long connectTimeoutMillis = 6000;

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

    public long getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(long connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

}
