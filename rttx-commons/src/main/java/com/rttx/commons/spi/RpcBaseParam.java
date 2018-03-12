package com.rttx.commons.spi;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/2/5 13:44
 * @Desc: as follows.
 */
public class RpcBaseParam {
    /**
     * 项目应用ID
     */
    private Integer proId;

    /**
     * 项目名称
     */
    private String proName;

    /**
     * 用于日志打印跟踪ID，保证唯一
     */
    private String traceId;

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
