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
public class SHA256withRSAVerifySign extends AbstractSign implements VerifySign {

    public SHA256withRSAVerifySign() {
    }

    public SHA256withRSAVerifySign(String publicKey, Map<String, Object> signParams, String signStr) {
        super(publicKey, signParams, signStr);
    }

    @Override
    public boolean verify() throws SignException {
        super.setSortedParams(sortSignParams(getSignParams()));
        return SignTools.sha256WithRsaVerifySign(getSignKey(), buildSignParams(), getSignStr());
    }
}
