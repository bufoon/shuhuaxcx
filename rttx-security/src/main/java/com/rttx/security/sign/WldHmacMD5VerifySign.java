package com.rttx.security.sign;

import com.rttx.security.support.SignException;
import org.apache.commons.codec.binary.Hex;

import java.util.Map;

import static com.rttx.security.util.SignTools.hmacMD5Sign;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/30 10:16
 * @Desc: as follows.
 * 我来贷签名验证
 */
public class WldHmacMD5VerifySign extends HmacMD5VerifySign {

    public WldHmacMD5VerifySign(String signKey, Map<String, Object> signParams, String signStr) {
        super(signKey, signParams, signStr);
    }

    /**
     * 我来贷验证签名，字符串是16进制
     * @return
     * @throws SignException
     */
    @Override
    public boolean verify() throws SignException {
        super.setSortedParams(sortSignParams(getSignParams()));  // 排序
        String signStr = Hex.encodeHexString(hmacMD5Sign(buildSignParams(), getSignKey()));
        return signStr.equalsIgnoreCase(getSignStr());
    }
}
