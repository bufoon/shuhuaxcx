package com.rttx.redis;

import com.rttx.commons.exception.RttxException;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/3/19 17:54
 * @Desc: as follows.
 */
public class RttxRedisException extends RttxException {
    public RttxRedisException() {
    }

    public RttxRedisException(String message) {
        super(message);
    }

    public RttxRedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public RttxRedisException(Throwable cause) {
        super(cause);
    }

    public RttxRedisException(Integer prodId, Object data, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(prodId, data, message, cause, enableSuppression, writableStackTrace);
    }

    public RttxRedisException(Integer prodId, String code, String message, Object data, Throwable cause) {
        super(prodId, code, message, data, cause);
    }

    public RttxRedisException(Integer prodId, String code, String message, String desc, Object data, Throwable cause) {
        super(prodId, code, message, desc, data, cause);
    }

    public RttxRedisException(Integer prodId, String code, String message) {
        super(prodId, code, message);
    }

    public RttxRedisException(Integer prodId, String code, String message, String desc) {
        super(prodId, code, message, desc);
    }
}
