package com.rttx.commons.utils;/**
 * @Author: zhangcs
 * @Description:
 * @Date: $time$ $date$
 */


import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * @description:
 * @author: zhangcs
 * @create: 2018-01-18 16:02
 **/
public class ImageWithBase64 {

    /**
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     * @param imgStr base64编码字符串
     * @param path 图片路径-具体到文件
     * @return
     */
    public static boolean generateImage(String imgStr, String path) {
        if(imgStr==null)
            return false;
        BASE64Decoder decoder = new BASE64Decoder();

        try{
            //解密
//            byte[] b = decoder.decodeBuffer(imgStr);
            byte[] b = Base64.decodeBase64(imgStr);
            //处理数据
            for (int i = 0; i < b.length; i++) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;

        }catch (Exception e){
            return false;
        }

    }


        /**
         * @Description: 根据图片地址转换为base64编码字符串
         * @Author:
         * @CreateTime:
         * @return
         */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data);
        return new String(Base64.encodeBase64(data));
    }

    /**
     * 示例
     */
    public static void main(String[] args) {
        String strImg = getImageStr("D:/image/spring-boot-project.jpg");
        System.out.println(strImg);
        generateImage(strImg, "D:/image/spring-boot-project11.jpg");
    }

}
