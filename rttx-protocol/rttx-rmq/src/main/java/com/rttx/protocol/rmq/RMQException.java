package com.rttx.protocol.rmq;


import com.rttx.commons.exception.RttxException;

public class RMQException extends RttxException {
    public RMQException() {
    }

    public RMQException(String message) {
        super(message);
    }

    public RMQException(String message, Throwable cause) {
        super(message, cause);
    }

    public RMQException(Throwable cause) {
        super(cause);
    }

    public RMQException(Integer prodId, Object data, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(prodId, data, message, cause, enableSuppression, writableStackTrace);
    }

    public RMQException(Integer prodId, String code, String message, Object data, Throwable cause) {
        super(prodId, code, message, data, cause);
    }

    public RMQException(Integer prodId, String code, String message, String desc, Object data, Throwable cause) {
        super(prodId, code, message, desc, data, cause);
    }

    public RMQException(Integer prodId, String code, String message) {
        super(prodId, code, message);
    }

    public RMQException(Integer prodId, String code, String message, String desc) {
        super(prodId, code, message, desc);
    }
}
