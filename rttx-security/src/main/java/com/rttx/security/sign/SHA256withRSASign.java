package com.rttx.security.sign;

import com.rttx.commons.api.RequestParam;
import com.rttx.commons.utils.ObjectMapUtils;
import com.rttx.security.support.SignException;
import com.rttx.security.util.SignTools;
import javafx.util.Pair;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Map;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/29 16:26
 * @Desc: as follows.
 */
public class SHA256withRSASign extends AbstractSign implements Sign {

    public SHA256withRSASign() {
    }

    public SHA256withRSASign(Map<String, Object> signParams, String privateKey) {
        super(signParams, privateKey);
    }

    @Override
    public String sign() throws SignException {
        super.setSortedParams(sortSignParams(getSignParams()));
        return Base64.getEncoder().encodeToString(SignTools.sha256WithRsaSign(Base64.getDecoder().decode(getSignKey()), this.buildSignParams()));
    }

    public static void main(String[] args) throws IllegalAccessException, NoSuchAlgorithmException, SignException {
        RequestParam<String> requestParam = new RequestParam<>();
        requestParam.setAppId("123");
        requestParam.setBizData("{123123123}");
        requestParam.setFormat("123");
        Map<String, Object> map = ObjectMapUtils.objectToMap(requestParam);

        Pair<byte[], byte[]> pair = SignTools.generateKeyBytes();
        System.out.println("=======================================");
        System.out.println(Base64.getEncoder().encodeToString(pair.getKey()));
        System.out.println("-----------------------------------------");

        System.out.println(Base64.getEncoder().encodeToString(pair.getValue()));
        System.out.println("=======================================");

        SHA256withRSASign sign = new SHA256withRSASign(map, Base64.getEncoder().encodeToString(pair.getValue()));
        String signStr = sign.sign();
        System.out.println(signStr);
        System.out.println(SignTools.sha256WithRsaVerifySign(Base64.getEncoder().encodeToString(pair.getKey()), sign.buildSignParams(), signStr));
    }
}
