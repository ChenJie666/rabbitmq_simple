package com.hxr.rabbitmq.confirm;

import com.hxr.rabbitmq.util.ConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send1 {

    private static final String EXCHANGE_NAME = "test_exchange_confirm";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct",true);

        //TODO 开启确认模式
        channel.confirmSelect();

        String msg = "test confirm";
        channel.basicPublish(EXCHANGE_NAME,"com.hxr",null,msg.getBytes());

        if(!channel.waitForConfirms()){     //TODO 会阻塞线程监听端口等待信息返回
            System.out.println("消息发送失败");
        }else{
            System.out.println("消息发送成功");
        }

        channel.close();
        connection.close();
    }

}
