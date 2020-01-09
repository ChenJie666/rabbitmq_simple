package com.hxr.rabbitmq.workfair;

import com.hxr.rabbitmq.util.ConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

public class Receive1 {

    private static final String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        //获取连接
        Connection connection = ConnectUtil.getConnection();
        //打开信道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //TODO 保证一次只分配一个消息
        channel.basicQos(1);

        //定义消费者
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(envelope.getRoutingKey() + "接收到的数据:" + new String(body,Charset.forName("utf-8")));

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    System.out.println("[1] done  " + envelope.getDeliveryTag());
                    //TODO 手动回执处理完成消息
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };

        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);

    }

}