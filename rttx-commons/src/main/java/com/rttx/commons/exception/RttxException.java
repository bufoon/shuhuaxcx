package com.rttx.commons.exception;

public class RttxException extends Exception {
    private String appId;
    private Integer exceptionCode;
    private Object data;

    public RttxException() {
        super();
    }

    public RttxException(String message) {
        super(message);
    }

    public RttxException(String message, Throwable cause) {
        super(message, cause);
    }

    public RttxException(Throwable cause) {
        super(cause);
    }

    protected RttxException(String appId, Object data, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.appId = appId;
        this.data = data;
    }

    public RttxException(String appId, Integer exceptionCode, String message, Object data, Throwable cause) {
        super(message, cause);
        this.appId = appId;
        this.exceptionCode = exceptionCode;
        this.data = data;
    }

    public RttxException(String appId, Integer exceptionCode, String message) {
        this(appId, exceptionCode, message, null, null);
    }


}
