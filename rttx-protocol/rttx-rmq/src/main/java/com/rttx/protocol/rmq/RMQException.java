package com.rttx.protocol.rmq;

import com.rttx.common.exception.RttxException;

public class RMQException extends RttxException {
    public RMQException() {
        super();
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

    protected RMQException(String appId, Object data, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(appId, data, message, cause, enableSuppression, writableStackTrace);
    }

    public RMQException(String appId, Integer exceptionCode, String message, Object data, Throwable cause) {
        super(appId, exceptionCode, message, data, cause);
    }

    public RMQException(String appId, Integer exceptionCode, String message) {
        super(appId, exceptionCode, message);
    }
}
