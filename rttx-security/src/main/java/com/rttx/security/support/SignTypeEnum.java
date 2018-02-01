package com.rttx.security.support;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/30 11:01
 * @Desc: as follows.
 * 签名枚举
 */
public enum SignTypeEnum {

    HmacMD5("HmacMD5", "HmacMD5", "hmac&Md5 消息签名"),
    WldHmacMD5("WldHmacMD5", "WldHmacMD5", "第三方我来贷HmacMD5 消息签名"),
    SHA256withRSA("SHA256withRSA", "SHA256withRSA", "SHA256withRSA 消息签名");

    private String name;
    private String value;
    private String label;

    private SignTypeEnum(String name, String value, String label){
        this.name = name;
        this.value = value;
        this.label = label;
    }

    /**
     * 根据枚举值获取 AuthStatusEnum
     * @param value
     * @return
     */
    public static SignTypeEnum getEnum(String value)
    {
        for (SignTypeEnum enumData : SignTypeEnum.values())
            if (enumData.getValue().equals(value))
            {
                return enumData;
            }
            return null;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
