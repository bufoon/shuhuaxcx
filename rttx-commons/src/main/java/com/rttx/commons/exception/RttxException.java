package com.rttx.commons.exception;

import org.apache.commons.lang3.time.DateFormatUtils;

public class RttxException extends Exception {
    private String appId;
    private String code;
    private Object data;
    private String desc;

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

    public RttxException(String appId, String code, String message, Object data, Throwable cause) {
        super(message, cause);
        this.appId = appId;
        this.code = code;
        this.data = data;
    }

    public RttxException(String appId, String code, String message, String desc, Object data, Throwable cause) {
        this(appId, code, message, data, cause);
        this.desc = desc;
    }

    public RttxException(String appId, String code, String message) {
        this(appId, code, message, null, null);
    }
    public RttxException(String appId, String code, String message, String desc) {
        this(appId, code, message, desc, null, null);
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
