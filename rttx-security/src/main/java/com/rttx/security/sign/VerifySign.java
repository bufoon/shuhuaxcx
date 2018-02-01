package com.rttx.security.sign;

import com.rttx.security.support.SignException;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/29 11:12
 * @Desc: as follows.
 * 验证签名接口
 */
public interface VerifySign {

    /**
     * 验证签名
     * @return 返回true or false
     */
    boolean verify() throws SignException;
}
