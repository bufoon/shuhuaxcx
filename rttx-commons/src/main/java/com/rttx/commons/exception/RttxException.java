package com.rttx.commons.exception;

public class RttxException extends Exception {
    private Integer prodId;
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

    protected RttxException(Integer prodId, Object data, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.prodId = prodId;
        this.data = data;
    }

    public RttxException(Integer prodId, String code, String message, Object data, Throwable cause) {
        super(message, cause);
        this.prodId = prodId;
        this.code = code;
        this.data = data;
    }

    public RttxException(Integer prodId, String code, String message, String desc, Object data, Throwable cause) {
        this(prodId, code, message, data, cause);
        this.desc = desc;
    }

    public RttxException(Integer prodId, String code, String message) {
        this(prodId, code, message, null, null);
    }
    public RttxException(Integer prodId, String code, String message, String desc) {
        this(prodId, code, message, null, null);
    }

    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
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
