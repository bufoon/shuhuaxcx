package com.rttx.security.sign;

import com.rttx.commons.api.RequestParam;
import com.rttx.commons.utils.ObjectMapUtils;
import com.rttx.security.support.SignException;
import org.apache.commons.codec.binary.Hex;

import java.util.Base64;
import java.util.Map;

import static com.rttx.security.util.SignTools.hmacMD5Sign;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/29 16:27
 * @Desc: as follows.
 * HmacMd5Sign 签名base64
 */
public class HmacMD5Sign extends AbstractSign implements Sign {

    public HmacMD5Sign() {
    }

    public HmacMD5Sign(Map<String, Object> signParams, String privateKey) {
        super(signParams, privateKey);
    }

    @Override
    public String sign() throws SignException {
        super.setSortedParams(sortSignParams(getSignParams()));
        return Base64.getEncoder().encodeToString(hmacMD5Sign(buildSignParams(), getSignKey()));
    }


    public static void main(String[] args) throws SignException, IllegalAccessException {
        RequestParam<String> requestParam = new RequestParam<>();
        requestParam.setAppId("123");
        requestParam.setBizData("{123123123}");
        requestParam.setFormat("123");
        Map<String, Object> map = ObjectMapUtils.objectToMap(requestParam);
        //Map map = new BeanMap(requestParam);
        HmacMD5Sign hmacMD5Sign = new HmacMD5Sign(map, "privateKey");
        System.out.println(hmacMD5Sign.sign());
    }
}
