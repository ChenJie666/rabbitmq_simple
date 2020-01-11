package com.hxr.rabbitmq.transaction;

import com.hxr.rabbitmq.util.ConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {

    private static final String EXCHANGE_NAME = "test_exchange_transaction";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME,"topic",true);

        try{
            channel.txSelect();
            String msg = "test transaction";
            channel.basicPublish(EXCHANGE_NAME,"com.hxr",null,msg.getBytes());
            int i = 1/0;
            channel.txCommit();
            System.out.println("transaction commit!");
        }catch(Exception e){
            channel.txRollback();
            System.out.println("transaction rollback!");
        }

        channel.close();
        connection.close();
    }

}
