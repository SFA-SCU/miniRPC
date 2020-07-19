package cn.scu.rpc.consumer.controller;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import sun.security.provider.MD5;


import java.util.concurrent.TimeUnit;

/**
 * ClassName: ClientPool
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/7/12 22:50
 * Version V1.0
 * Description:
 */
public class ClientPool extends BasePooledObjectFactory<Channel> {

    private String ip;
    private int port;

    public ClientPool() {
        ip = "127.0.0.1";
        port = 8866;
    }

    public int[] intersect(int[] nums1, int[] nums2) {

        Arrays.sort(nums1);
        Arrays.sort(nums2);



        ArrayList<Integer> ans = new ArrayList<Integer>();

        int index1 = 0;
        int index2 = 0;

        while(index1 < nums1.length && index2 < nums2.length){

            if(nums1[index1] == nums2[index2]){

                ans.add(nums1[index1]);
                index1++;
                index2++;
            }else if(nums1[index1] > nums2[index2]){

                index2++;
            }else {

                index1++;
            }
        }

        return ans.stream().mapToInt(Integer::intValue).toArray();
    }

    public static void main(String[] args) throws Exception {


        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxWaitMillis(2000);
        config.setMinIdle(3);
        config.setMaxIdle(5);
        config.setMaxTotal(5);

        /**
         * 销毁到minidle
         */
        config.setSoftMinEvictableIdleTimeMillis(10000);
        config.setTestOnBorrow(true);
        config.setTestOnCreate(true);

        /**
         * 销毁所有
         */
        //   config.setMinEvictableIdleTimeMillis(3000);

        // 词句是必须的，否则（Soft）MinEvictableIdleTimeMillis不生效
        config.setTimeBetweenEvictionRunsMillis(100);

        GenericObjectPool<Channel> objectPool = new GenericObjectPool<Channel>(new ClientPool(), config);

        // 先睡5s让系统补足3个对象
        Thread.sleep(5000);

        List<Channel> list = new ArrayList<>();
        for(int i=0; i<4; ++i) {
            list.add(objectPool.borrowObject());
        }

        for(Channel channel : list) {
            objectPool.returnObject(channel);
        }

        /**
         * 休息8秒等待服务端断开连接
         */
        Thread.sleep(8000);

        /**
         * 触发activate，validate后失败，触发create
         */
        objectPool.borrowObject();

    }

    @Override
    public Channel create() throws Exception {


        ChannelFuture future = getBoot().connect(new InetSocketAddress("127.0.0.1",8866)).sync();
        return future.channel();
    }



    @Override
    public PooledObject<Channel> wrap(Channel channel) {

        System.out.println("创建" + new Date());
        return new DefaultPooledObject<Channel>(channel);
    }

    /**
     * 对象销毁
     * @param pooledObject
     */
    @Override
    public void destroyObject(PooledObject<Channel> pooledObject) throws Exception {

        System.out.println("销毁" + new Date());
        Channel channel = pooledObject.getObject();
        channel.close();
        super.destroyObject(pooledObject);
    }

    /**
     * 验证对象有效性
     * @param
     * @return
     */
    @Override
    public boolean validateObject(PooledObject<Channel> pooledObject) {

        System.out.println("validate " + pooledObject.getObject().isActive() + new Date());
        return pooledObject.getObject().isActive();
    }

    private static Bootstrap bootstrap;
    public static Bootstrap getBoot() {

        if(bootstrap == null) {
            synchronized (ClientPool.class) {
                if (bootstrap == null) {
                    EventLoopGroup worker = new NioEventLoopGroup();

                    //辅助启动类
                    bootstrap = new Bootstrap();

                    //设置线程池
                    bootstrap.group(worker);

                    //设置socket工厂
                    bootstrap.channel(NioSocketChannel.class);

                    //设置管道
                    bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //获取管道
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(10000, 0, 4, 0, 4));
                       //     pipeline.addLast(new ProtobufDecoder(MyBaseProto.BaseProto.getDefaultInstance()));
                            pipeline.addLast(new LengthFieldPrepender(4));
                            pipeline.addLast(new ProtobufEncoder());

                            pipeline.addLast(new IdleStateHandler(61, 30, 0, TimeUnit.SECONDS));
                 //           pipeline.addLast(new ClientHeartbeatHandler());
//
                            //处理类
                  //          pipeline.addLast(new ClientHandler4Heart());
                        }
                    });

                    return bootstrap;
                } else {
                    return bootstrap;
                }
            }

        } else {
            return bootstrap;
        }
    }



}
