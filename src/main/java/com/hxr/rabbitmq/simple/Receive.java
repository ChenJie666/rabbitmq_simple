package com.hxr.rabbitmq.simple;

import com.hxr.rabbitmq.util.ConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

/**
 * 消费者消费消息
 */
public class Receive {

    private static final String QUEUE_NAME = "test_simple";

    public static void main(String[] args) throws IOException, TimeoutException {

        //获取连接对象
        Connection connection = ConnectUtil.getConnection();

        //在连接中创建信道
        Channel channel = connection.createChannel();

        //队列声明(如果队列已存在，可以不声明)
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);



        //定义队列的消费者,有新消息会调用handleDelivery方法
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(envelope.getRoutingKey() + "接收到的数据:" + new String(body,Charset.forName("utf-8")));
            }
        };

        //监听队列，会记录上次消费的位置
        channel.basicConsume(QUEUE_NAME, true, consumer);

        //TODO 不能将连接关闭，关闭连接导致无法接受数据
//        channel.close();
    }

}
