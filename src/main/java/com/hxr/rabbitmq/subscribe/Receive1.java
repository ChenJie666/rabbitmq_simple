package com.hxr.rabbitmq.subscribe;

import com.hxr.rabbitmq.util.ConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

public class Receive1 {

    private static final String EXCHANGE_NAME = "test_exchange_fanout";
    private static final String QUEUE_NAME = "test_queue_fanout1";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicQos(1);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(envelope.getRoutingKey() + "接收到的数据" + new String(body, Charset.forName("utf-8")));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    System.out.println("[1] done");
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };

        channel.basicConsume(QUEUE_NAME,false,consumer);

    }

}
