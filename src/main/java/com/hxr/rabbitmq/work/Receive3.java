package com.hxr.rabbitmq.work;

import com.hxr.rabbitmq.util.ConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

public class Receive3 {

    private static final String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(envelope.getRoutingKey() + "接收到的数据" + new String(body,Charset.forName("utf-8")));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    System.out.println("[3] done");
                }
            }
        };


        channel.basicConsume("", false, consumer);

    }
}
