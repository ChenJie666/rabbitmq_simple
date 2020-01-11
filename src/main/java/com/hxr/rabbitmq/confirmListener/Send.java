package com.hxr.rabbitmq.confirmListener;

import com.hxr.rabbitmq.util.ConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

public class Send {
    private static final String EXCHANGE_NAME = "test_exchange_confirmListener";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic",true);

        //TODO 开启确认模式
        channel.confirmSelect();

        //TODO 存放未确认的消息的标识
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());  //TODO 获取有序Set集合

        //TODO 开启确认模式
        channel.addConfirmListener(new ConfirmListener() {  //TODO 异步接受信息，通过设置监听器，在信息返回时回调监听器方法
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                //TODO 返回成功
                if(multiple) {
                    System.out.println("---handleAck---multiple---");
                    confirmSet.headSet(deliveryTag+1).clear();  //将集合中的比deliveryTag+1小的集合作为视图返回并进行清除，该操作会反应在原集合中。
                }else{
                    System.out.println("---handleAck---multiple false---");
                    confirmSet.remove(deliveryTag);
                }
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                //TODO 返回失败,如果失败，根据实际业务场景进行延迟重发
                if(multiple) {
                    System.out.println("---handleNack---multiple---");
                    confirmSet.headSet(deliveryTag+1).clear();
                }else{
                    System.out.println("---handleNack---multiple false---");
                    confirmSet.remove(deliveryTag);
                }
            }
        });

        String msg = "test confirmListener";

        //TODO 在集合中添加待确认的DeliveryTag
        for (int i = 0; i < 50; i++) {
            long seqNo = channel.getNextPublishSeqNo();
            channel.basicPublish(EXCHANGE_NAME, "com.hxr", null, msg.getBytes());
            confirmSet.add(seqNo);
        }


//        channel.close();
//        connection.close();
    }
}
