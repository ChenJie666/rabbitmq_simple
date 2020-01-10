package com.hxr.rabbitmq.ackanddurable;

import com.hxr.rabbitmq.util.ConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

public class Receive1 {

    private static final String QUEUE_NAME = "test_queue_durable";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectUtil.getConnection();

        Channel channel = connection.createChannel();

        boolean durable = true;
        //每个消费者自己的信道
        channel.queueDeclare(QUEUE_NAME, durable,false,false,null);
        channel.basicQos(1);

        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(envelope.getRoutingKey() + "接收到的数据" + new String(body, Charset.forName("utf-8")));

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("[1] done" + envelope.getDeliveryTag());
                    //TODO 返回回执
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };

        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck,consumer);

    }

}
