package com.hxr.rabbitmq.ackanddurable;

import com.hxr.rabbitmq.util.ConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {

    private static final String QUEUE_NAME = "test_queue_durable";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectUtil.getConnection();

        Channel channel = connection.createChannel();

        //设置消息持久化
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        //设置队列单条分发
        channel.basicQos(1);

        for (int i = 0; i < 50; i++) {
            String msg = "Hello durable--" + i;
            channel.basicPublish("", "", null, msg.getBytes());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally{
                System.out.println(msg + "done");
            }
        }

        channel.close();
        connection.close();
    }

}
