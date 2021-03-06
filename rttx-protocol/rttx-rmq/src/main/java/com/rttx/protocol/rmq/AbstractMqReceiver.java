package com.rttx.protocol.rmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.rabbitmq.client.Channel;
import com.rttx.commons.utils.StringUtils;
import com.rttx.protocol.rmq.base.RMQMessage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 消息接收抽象类
 * @param <T>
 */
public abstract class AbstractMqReceiver<T> implements MqReceiver<T> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 消息接收原生的方法
     * @param message
     * @param channel
     * @throws RMQException
     */
    @RabbitHandler
    public void receive(byte[] message, @Header(AmqpHeaders.DELIVERY_TAG)long deliveryTag, Channel channel) throws RMQException {
        // 获取字符串数据
        String data = new String(message, Charset.defaultCharset());
        if (StringUtils.isEmpty(data)){
            logger.error("receive data is empty.");
            throw new RMQException("receive data is empty.");
        }
        RMQMessage<String> rmqMessage = null;
        try {
            // 数据转换
            rmqMessage = JSON.parseObject(data, new TypeReference<RMQMessage<String>>(){});
            logger.info("receive rabbit mq info: \n{}", JSON.toJSONString(rmqMessage));
        } catch (Exception e) {
            logger.error("received data connot convert valid Message.{}", ExceptionUtils.getStackTrace(e));
            throw new RMQException("received data connot convert valid Message.", e);
        }
        // 消息处理
        T t = null;
        try {
            t = JSON.parseObject(rmqMessage.getData(), this.childHandleClass());
        } catch (Exception e) {
            logger.error("received data connot convert valid Message.{}", ExceptionUtils.getStackTrace(e));
            throw new RMQException("received data connot convert valid Message.", e);
        }
        boolean handle = handle(t);
        // 处理是否应答
        ackAction(deliveryTag, channel, handle);


    }

    /**
     * 子类 类型，必须实现
     * @return
     */
    protected abstract Class<T> childHandleClass();

    /**
     * 数据转换
     * @param data
     * @param clazz
     * @return
     */
    protected <T> T dataConverter(String data, Class<T> clazz) {

        return JSON.parseObject(data, clazz);

    }

    /**
     * 处理应答，成功确认消费消息，失败，消息重新入列
     * @param deliveryTag
     * @param channel
     * @param handleStatus
     */
    protected void ackAction(long deliveryTag, Channel channel, boolean handleStatus) throws RMQException {
        try {
            if (handleStatus){
                channel.basicAck(deliveryTag, false);
            } else {
                channel.basicNack(deliveryTag, false, true);
            }
        } catch (IOException e) {
            throw new RMQException("message confirm fail.", e);
        }
    }

    /**
     * 根据消息处理结果，如果处理不成功的数据，可以重下该方法，将数据再次存入其它地方
     *
     * @param message
     */
    protected void handleErrorMessage(RMQMessage<T> message){
        logger.debug("default handle error message print.");
    }


}
