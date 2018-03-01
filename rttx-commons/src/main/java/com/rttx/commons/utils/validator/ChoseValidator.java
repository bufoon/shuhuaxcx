package com.rttx.commons.utils.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @description:
 * @author: zhangcs
 * @create: 2018-03-01 11:25
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Constraint(validatedBy = ChoseValidatorClass.class)
public @interface ChoseValidator {
    //多个有效值用逗号隔开
    String values();

    //提示内容
    String message() default "输入类型不存在";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default {};

}
