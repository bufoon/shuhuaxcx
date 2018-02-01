package com.rttx.commons.spi;

import java.io.Serializable;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/24 10:47
 * @Desc: as follows.
 */
public class ResponseData<T> implements Serializable {
    private static final long serialVersionUID = 1445518963620876799L;

    private String code;
    private String msg;
    private T data;

    public ResponseData(){}

    public ResponseData(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
