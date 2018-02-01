package com.rttx.security.sign;

import com.rttx.security.support.SignException;
import com.rttx.security.util.SignTools;

import java.util.Base64;
import java.util.Map;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/29 17:40
 * @Desc: as follows.
 */
public class HmacMD5VerifySign extends AbstractSign implements VerifySign {

    public HmacMD5VerifySign() {
    }

    public HmacMD5VerifySign(String signKey, Map<String, Object> signParams, String signStr) {
        super(signKey, signParams, signStr);
    }

    /**
     * 默认的验证是base64字符串
     * @return
     * @throws SignException
     */
    @Override
    public boolean verify() throws SignException {
        super.setSortedParams(sortSignParams(getSignParams())); // 排序
        // 生成签名
        String signBase64 = Base64.getEncoder().encodeToString(SignTools.hmacMD5Sign(buildSignParams(), getSignKey()));
        return signBase64.equalsIgnoreCase(getSignStr());
    }
}
