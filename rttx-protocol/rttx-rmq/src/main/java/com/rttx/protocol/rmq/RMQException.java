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

    public RMQException(String appId, Object data, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(appId, data, message, cause, enableSuppression, writableStackTrace);
    }

    public RMQException(String appId, String code, String message, Object data, Throwable cause) {
        super(appId, code, message, data, cause);
    }

    public RMQException(String appId, String code, String message, String desc, Object data, Throwable cause) {
        super(appId, code, message, desc, data, cause);
    }

    public RMQException(String appId, String code, String message) {
        super(appId, code, message);
    }
}
