package com.rttx.commons.base;

import com.rttx.commons.exception.RttxException;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/2/6 14:27
 * @Desc: as follows.
 * 公共响应码
 */
public enum ResEnum {

    SUCCESS("000000", "Success", "成功"),
    FAIL("000001", "Fail", "失败"),
    SYSTEM_ERROR("000002", "System Error", "系统错误"),
    PARAMS_INVALID("000003", "Params Invalid", "基本参数不合法"),
    UNKNOWN_ERROR("000004", "Unknow Error", "未知错误")
    ;

    ResEnum(String code, String msg, String desc) {
        this.code = code;
        this.msg = msg;
        this.desc = desc;
    }

    private String code;
    private String msg;
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 根据枚举值获取 AuthStatusEnum
     * @param code
     * @return
     */
    public static ResEnum getEnum(String code) throws RttxException
    {
        for (ResEnum resEnum : ResEnum.values())
            if (resEnum.getCode().equals(code))
            {
                return resEnum;
            }
        throw new RttxException(String.format("传入的枚举值不存在,AuthStatusEnum：%s", code));
    }
}
