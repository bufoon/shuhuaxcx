package com.rttx.protocol.rmq;

import com.rttx.protocol.rmq.base.RMQMessage;

public interface MqSender {

    void send(RMQMessage message);
}
