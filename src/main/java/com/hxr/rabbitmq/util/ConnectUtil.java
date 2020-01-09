package com.hxr.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectUtil {

    /**
     * 获取MQ连接
     * @return
     */
    public static Connection getConnection() throws IOException, TimeoutException {

        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();

        //设置工厂对象的属性
        connectionFactory.setHost("116.62.148.11");
        connectionFactory.setPort(5672);    //TODO 通讯端口是5672，15672是管理月面端口

        connectionFactory.setVirtualHost("/vhost_mmr");

        connectionFactory.setUsername("user");
        connectionFactory.setPassword("user");

        //创建连接
        Connection connection = connectionFactory.newConnection();

        return connection;
    }

}
