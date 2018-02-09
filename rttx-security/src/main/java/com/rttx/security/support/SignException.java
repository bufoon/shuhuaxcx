package com.rttx.security.support;

import com.rttx.commons.exception.RttxException;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/29 18:11
 * @Desc: as follows.
 */
public class SignException extends RttxException {

    public SignException() {
    }

    public SignException(String message) {
        super(message);
    }

    public SignException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignException(Throwable cause) {
        super(cause);
    }

    public SignException(Integer prodId, Object data, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(prodId, data, message, cause, enableSuppression, writableStackTrace);
    }

    public SignException(Integer prodId, String code, String message, Object data, Throwable cause) {
        super(prodId, code, message, data, cause);
    }

    public SignException(Integer prodId, String code, String message, String desc, Object data, Throwable cause) {
        super(prodId, code, message, desc, data, cause);
    }

    public SignException(Integer prodId, String code, String message) {
        super(prodId, code, message);
    }

    public SignException(Integer prodId, String code, String message, String desc) {
        super(prodId, code, message, desc);
    }
}
