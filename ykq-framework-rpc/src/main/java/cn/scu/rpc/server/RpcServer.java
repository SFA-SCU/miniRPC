package cn.scu.rpc.server;

import cn.scu.rpc.common.RpcURL;
import cn.scu.rpc.config.ServiceConfig;
import cn.scu.rpc.exception.RpcException;
import cn.scu.rpc.registry.ConsulRegistryService;
import cn.scu.rpc.registry.RegistryService;
import cn.scu.rpc.registry.ZookeeperRegistryService;
import cn.scu.rpc.util.ApplicationContextUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


public class RpcServer {

    private final static Logger logger = LoggerFactory.getLogger(RpcServer.class);



    private ServiceConfig serviceConfig;
    private RpcServerInitializer rpcServerInitializer;

    public RpcServer(ServiceConfig serviceConfig, RpcServerInitializer rpcServerInitializer){
        this.rpcServerInitializer=rpcServerInitializer;
        this.serviceConfig=serviceConfig;
        this.bind(this.serviceConfig);
    }

    public void bind(ServiceConfig serviceConfig) {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(this.rpcServerInitializer)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);

            try {

                ChannelFuture channelFuture = bootstrap.bind(serviceConfig.getHost(),serviceConfig.getPort()).sync();
                RpcURL url = new RpcURL();
                url.setHost(serviceConfig.getHost());
                url.setPort(serviceConfig.getPort());
                url.setRegistryHost(serviceConfig.getRegistryHost());
                url.setRegistryPort(serviceConfig.getRegistryPort());
                Map<String,Object> map = ApplicationContextUtils.getApplicationContext().getBeansWithAnnotation(RpcService.class);


                //注册到zookeeper
                ZookeeperRegistryService zkRegistryService = ApplicationContextUtils.getApplicationContext().getBean(ZookeeperRegistryService.class);

                for (Object value : map.values()) {


                    for (Class<?> anInterface : value.getClass().getInterfaces()) {

                        url.setServiceName(anInterface.getName());
                        zkRegistryService.register(url);
                    }

                }

                RegistryService registryService = new ConsulRegistryService();
                registryService.register(url);
                channelFuture.channel().closeFuture().sync();

            } catch (InterruptedException e) {
                throw new RpcException(e);
            }
        }finally {

            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
