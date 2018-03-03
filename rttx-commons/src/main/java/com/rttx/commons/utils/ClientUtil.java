package com.rttx.commons.utils;

import org.apache.commons.lang.CharSet;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**

/**
 * @description: 接口公用类
 * @author: zhangcs
 * @create: 2018-01-22 9:37
 **/
public class ClientUtil {

//    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *
     * @param
     * @param url
     * @param header
     * @param JSONStr
     * @return
     */
    public static String HttpWithPost(String url, Map<String,String> header, String JSONStr) {
        String rev ="";
        System.out.println("~~~~~~~~~~~~客户端接口调用~~~~~~~~~~");
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        System.out.println("url=="+url);
        if (!header.isEmpty()) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                System.out.println("key= " + entry.getKey() + " and value= "
                        + entry.getValue());
                httppost.addHeader(entry.getKey(), entry.getValue());
            }
        }
//        httppost.addHeader("Content-Type","application/json");
        HttpResponse response = null;
        try {
            if(!"".equals(JSONStr) && JSONStr!=null && JSONStr.length()>2){

                    httppost.setEntity(new StringEntity(JSONStr, "UTF-8"));
            }
            response = httpclient.execute(httppost);
            int code = response.getStatusLine().getStatusCode();
            System.out.println(code+"code");
           /* if (code == 200) {
                rev = EntityUtils.toString(response.getEntity(), "UTF-8");//返回json格式： {"id": "","name": ""}
                //设置返回结果编码格式，使用String类的构造方法进行编码转换
                //					String result=new String(rev.getBytes("ISO-8859-1"),"utf-8")。
            }*/
            return EntityUtils.toString(response.getEntity());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;


    }

    public static String HttpWithGet(String url, Map<String,String> header){
        String result="";
        HttpResponse response = null;
        try {

            // 根据地址获取请求
            HttpGet request = new HttpGet(url);//这里发送get请求

            if (!header.isEmpty()) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    System.out.println("key= " + entry.getKey() + " and value= "
                            + entry.getValue());
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 获取当前客户端对象
            HttpClient httpClient =  HttpClients.createDefault();
            // 通过请求对象获取响应对象
           response = httpClient.execute(request);

           result = EntityUtils.toString(response.getEntity(),"UTF-8");
            // 判断网络连接状态码是否正常(0--200都数正常)
//            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                result= EntityUtils.toString(response.getEntity(),"utf-8");
//            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return  result;

    }


}
