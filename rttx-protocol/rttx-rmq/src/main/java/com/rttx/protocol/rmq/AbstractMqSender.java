package com.rttx.protocol.rmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMqSender implements MqSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;


}
