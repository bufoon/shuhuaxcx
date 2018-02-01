package com.rttx.security.sign;

import com.rttx.commons.api.RequestParam;
import com.rttx.commons.utils.StringUtils;
import com.rttx.security.support.SignException;

import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/29 16:24
 * @Desc: as follows.
 * 签名抽象父类
 *  参数签名生成算法如下：
  1.接口请求参数（不包含签名参数sign，desKey），根据字母ASCII码顺序对参数名称进行排序。其中bizData进行json编码，
    bizData作为一个整体参与排序。例如：sign = 3dfwdsv， signType = RSA，method = notify。
    那么排序后的顺序为method = notify, sign = 3dfwdsv，sign_type = RSA。
  2.将排序好的参数名和参数值按照key1=value1key2=value2的方式拼接，拼接后的字符串即为待签名字符串。
    拼装好的字符串需要采用utf-8编码。例如，接着上面的例子拼接后：method=notifysign=3dfwdsvsign_type=RSA。
  3.对需要签名的字符串，根据分配的私钥，通过RSA签名，生成sign。
  4.对生成的签名sign进行base64编码。
  5.最终将以上得到的参数（sign）拼装到请求中，发起HTTP请求。
 */
public abstract class AbstractSign{

    private String signKey;  // 签名key，privateKey， publickKey

    private Map<String, Object> signParams; // 签名参数，未排序

    private Map<String, Object> sortedParams; // 排序参数  treeMap

    private String signStr; //签好名的sign，用于验证

    public AbstractSign() {
    }

    public AbstractSign(Map<String, Object> signParams, String signKey) {
        this.signParams = signParams;
        this.signKey = signKey;
    }

    public AbstractSign(String signKey, Map<String, Object> signParams, String signStr) {
        this.signKey = signKey;
        this.signParams = signParams;
        this.signStr = signStr;
    }

    /**
     * 排序
     * @param signParams
     */
    public Map<String, Object> sortSignParams(Map<String, Object> signParams){
        return new TreeMap<>(signParams);
    }

    /**
     * 组装参数串
     * @return keyvaluekeyvalue....
     * @throws SignException
     */
    protected String buildSignParams() throws SignException {
        if (sortedParams == null || sortedParams.isEmpty()){
            throw new SignException("签名参数不能为空");
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
            if (StringUtils.isNotEmpty(entry.getValue())){
                sb.append(entry.getKey() + entry.getValue().toString());
            }
        }
        return sb.toString();
    }

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public Map<String, Object> getSortedParams() {
        return sortedParams;
    }

    public void setSortedParams(Map<String, Object> sortedParams) {
        this.sortedParams = sortedParams;
    }

    public Map<String, Object> getSignParams() {
        return signParams;
    }

    public void setSignParams(Map<String, Object> signParams) {
        this.signParams = signParams;
    }

    public String getSignStr() {
        return signStr;
    }

    public void setSignStr(String signStr) {
        this.signStr = signStr;
    }
}
