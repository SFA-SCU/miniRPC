package cn.scu.rpc.config;

import org.springframework.stereotype.Component;

/**
 * ClassName: ServiceConfig
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/15 0:34
 * Version V1.0
 * Description:
 */

public class ServiceConfig {

    private String host;
    private int port;

    private String registryHost;
    private int registryPort;

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
