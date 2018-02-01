package com.rttx.security.sign;

import com.rttx.security.support.SignException;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/29 10:36
 * @Desc: as follows.
 * 签名接口，生成签名
 */
public interface Sign {

    /**
     * 生成签名,返回base64加密的byte数组
     * @return  base64.encode(byte[])
     */
    String sign() throws SignException;
}
