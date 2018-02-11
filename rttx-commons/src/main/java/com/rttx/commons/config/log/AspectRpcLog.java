package com.rttx.commons.config.log;

import com.alibaba.fastjson.JSON;
import com.rttx.commons.base.AppInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Enumeration;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/2/11 15:58
 * @Desc: as follows.
 */
@Component
@Aspect
public class AspectRpcLog {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AppInfo appInfo;

    @Pointcut("@annotation(com.rttx.commons.config.log.RpcLog)")
    private void annotationCut() {

    }

    @Before("annotationCut()")
    public void beforeLog(JoinPoint joinPoint){

        //获取目标方法的参数信息
        Object[] obj = joinPoint.getArgs();
        //用的最多 通知的签名
        Signature signature = joinPoint.getSignature();
        logger.info("Rpc Service Request Info------------------------\n" +
                "Method: {} \n" +
                "AppInfo: appId_{}, appName_{} \n " +
                "params: {} \n ", signature.getDeclaringTypeName()+"."+signature.getName(),
                appInfo.getId(), appInfo.getName(),
                JSON.toJSONString(obj));

    }

    @AfterReturning(value = "annotationCut()", returning = "returnValue")
    public void afterLog(JoinPoint joinPoint, Object returnValue){
        //用的最多 通知的签名
        Signature signature = joinPoint.getSignature();
        logger.info("Rpc Service Method Response Info========================\n" +
                        "Method: {} \n" +
                        "AppInfo: appId_{}, appName_{} \n " +
                        "params: {} \n ", signature.getDeclaringTypeName()+"."+signature.getName(),
                appInfo.getId(), appInfo.getName(),
                JSON.toJSONString(returnValue));
    }
}
