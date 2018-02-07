package com.rttx.security.sign;

import com.rttx.security.support.SignException;
import org.apache.commons.codec.binary.Hex;

import java.util.Map;

import static com.rttx.security.util.SignTools.hmacMD5Sign;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/31 10:58
 * @Desc: as follows.
 * 我来贷 HMACMD5SIGN， 签名转成16进制
 */
public class WldHmacMD5Sign extends HmacMD5Sign {
    public WldHmacMD5Sign() {
    }

    public WldHmacMD5Sign(Map<String, Object> signParams, String privateKey) {
        super(signParams, privateKey);
    }

    @Override
    public String sign() throws SignException {
        super.setSortedParams(sortSignParams(getSignParams()));
        return Hex.encodeHexString(hmacMD5Sign(buildSignParams(), getSignKey())).toUpperCase();
    }
}
