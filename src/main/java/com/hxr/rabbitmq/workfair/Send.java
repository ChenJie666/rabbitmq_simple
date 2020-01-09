package com.hxr.rabbitmq.workfair;

import com.hxr.rabbitmq.util.ConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送消息到队列，两个消费者分别消费对列。虽然消费的速度不同，但是交替消费消息（round-robin），消费数量相同
 */
public class Send {

    public static final String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        //获取连接
        Connection connection = ConnectUtil.getConnection();
        //创建信道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //通过信道发送消息
        for (int i = 0; i < 50; i++) {
            String msg = "hello" + i;
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            Thread.sleep(500);
        }

        //关闭连接
        channel.close();
        connection.close();
    }

}
