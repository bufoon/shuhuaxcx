package com.rttx.protocol.rmq;

import com.rttx.protocol.rmq.base.RMQMessage;

public interface MqReceiver<T> {
    boolean handle(RMQMessage<T> message);
}
