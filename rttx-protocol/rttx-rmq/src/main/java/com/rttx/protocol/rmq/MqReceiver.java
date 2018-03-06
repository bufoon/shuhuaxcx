package com.rttx.protocol.rmq;

public interface MqReceiver<T> {
    boolean handle(T message);
}
