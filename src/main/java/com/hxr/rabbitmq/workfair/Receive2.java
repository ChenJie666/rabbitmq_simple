package com.hxr.rabbitmq.workfair;

import com.hxr.rabbitmq.util.ConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

public class Receive2 {

    private static final String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectUtil.getConnection();

        final Channel channel = connection.createChannel(); //匿名内部类中使用外部定义的局部变量，因为局部变量与内部类对象的生命周期不一致，未避免内部类对象访问不存在的变量，需要将局部变量复制一份到内部类中，final保证该复制品与变量一定相同。但jdk1.8将它默认设置为final了

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //TODO 队列一次只分配一条消息
        channel.basicQos(1);

        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(envelope.getRoutingKey() + "接收到的数据" + new String(body,Charset.forName("utf-8")));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    System.out.println("[2] done");
                    //TODO 手动回执
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };

        boolean autoAck = false;
        channel.basicConsume("", autoAck, consumer);

    }
}
