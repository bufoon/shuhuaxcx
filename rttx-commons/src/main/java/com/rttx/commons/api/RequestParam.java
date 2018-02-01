package com.rttx.commons.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rttx.commons.base.AppInfo;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/23 14:35
 * @Desc: as follows.
 */
public class RequestParam<T> {

    private String appId; // 分配给接入方应用ID
    private String method; // 接口方法
    private String sign; // 签名
    private String signType; //签名类型，支持RSA256
    private Integer bizEnc; // bizData加密方式（0不加密，1加密）
    private String encKey; // 加密的key
    private String version; // 接口版本号
    private String format; // 返回数据格式，暂支持json
    private String timestamp; // 请求时间戳，精确到秒，格式 yyyy-MM-dd HH:mm:ss
    private T bizData; // 业务数据

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public Integer getBizEnc() {
        return bizEnc;
    }

    public void setBizEnc(Integer bizEnc) {
        this.bizEnc = bizEnc;
    }

    public String getEncKey() {
        return encKey;
    }

    public void setEncKey(String encKey) {
        this.encKey = encKey;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public T getBizData() {
        return bizData;
    }

    public void setBizData(T bizData) {
        this.bizData = bizData;
    }

    @Override
    public String toString() {
        return "RequestParam{" +
                "appId='" + appId + '\'' +
                ", method='" + method + '\'' +
                ", sign='" + sign + '\'' +
                ", signType=" + signType +
                ", bizEnc='" + bizEnc + '\'' +
                ", encKey='" + encKey + '\'' +
                ", version='" + version + '\'' +
                ", format='" + format + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", bizData=" + bizData +
                '}';
    }

    public static void main(String[] args) {
        RequestParam<AppInfo> requestParam = new RequestParam<>();
        requestParam.setAppId("0101011");
        AppInfo appInfo = new AppInfo();
        appInfo.setId(1);
        appInfo.setDesc("haha");
        appInfo.setName("test");
        requestParam.setBizData(appInfo);
        System.out.println(JSON.toJSONString(requestParam, SerializerFeature.WriteMapNullValue));
    }
}
