package com.rttx.commons.utils.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @description:
 * @author: zhangcs
 * @create: 2018-03-01 11:28
 **/
public class ChoseValidatorClass implements ConstraintValidator<ChoseValidator,Object> {
    //变量保存值列表
    private String values;

    //初始化values的值
    @Override
    public void initialize(ChoseValidator constraintAnnotation) {
        //将注解内配置的值赋值给变量
        this.values = constraintAnnotation.values();
    }
    //验证
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        //分割定义的有效值
        String[] valueArray = values.split(",");
        boolean isFlag = false;
        //遍历比对有效值
        for (int i=0;i<valueArray.length;i++){
            if(valueArray[i].equals(value)){
                isFlag = true;
                break;
            }
        }
        return isFlag;
    }
}
