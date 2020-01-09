package com.hxr.rabbitmq.simple;

import com.hxr.rabbitmq.util.ConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者生产消息
 */
public class Send {

    private static final String QUEUE_NAME="test_simple";

    public static void main(String[] args) throws IOException, TimeoutException {

        //获取连接对象
        Connection connection = ConnectUtil.getConnection();

        //在连接中创建一个信道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //向指定队列中发送消息
        String msg = "hello simple2";
        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());

        //关闭连接
        channel.close();
        connection.close();

    }

}
