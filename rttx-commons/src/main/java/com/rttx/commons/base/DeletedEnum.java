package com.rttx.commons.base;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/2/8 18:45
 * @Desc: as follows.
 */
public enum DeletedEnum {
    DELETED("deleted", 1, "已删除"),
    NON_DELETED("nonDeleted", 0, "未删除");
    ;

    DeletedEnum(String name, Integer value, String label) {
        this.name = name;
        this.value = value;
        this.label = label;
    }

    private String name;
    private Integer value;
    private String label;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
