package com.rttx.commons.utils;

import com.rttx.commons.base.ResEnum;
import com.rttx.commons.spi.ResponseData;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/2/6 15:22
 * @Desc: as follows.
 */
public class SpiUtils {

    /**
     *  判断Dubbo服务响应是否成功
     * @param responseData
     * @return
     */
    public static boolean isSuccess(ResponseData responseData){
        if (responseData == null){
            responseData = new ResponseData(ResEnum.SYSTEM_ERROR, null);
        }
        return (responseData != null && ResEnum.SUCCESS.getCode().equals(responseData.getCode()));
    }

    /**
     *  判断Dubbo服务响应是否成功
     * @param responseData
     * @return
     */
    public static boolean isDataSuccess(ResponseData responseData){
        if (responseData == null){
            responseData = new ResponseData(ResEnum.SYSTEM_ERROR, null);
        }
        return (responseData != null && ResEnum.SUCCESS.getCode().equals(responseData.getCode()) && responseData.getData() != null );
    }
}
