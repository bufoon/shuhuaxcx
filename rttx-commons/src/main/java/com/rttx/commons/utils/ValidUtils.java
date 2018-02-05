package com.rttx.commons.utils;

import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/30 11:29
 * @Desc: as follows.
 * 验证
 */
public class ValidUtils {


    /**
     * 列表验证
     * @param e
     * @param <E>
     * @return
     */
    public static <E> List<Map<String, Object>> valids(List<E> e) {
        if (e == null || e.isEmpty()) {
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < e.size(); i++) {
            Map<String, Object> map = ValidUtils.valid(e.get(i));
            if (map != null) {
                map.put("index", i);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 验证返回Map
     * @param object
     * @return
     */
    public static Map<String, Object> valid(Object object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
        if (constraintViolations == null || constraintViolations.isEmpty()) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            map.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
        }
        return map;
    }

    /**
     * 获取errors
     * @param errors
     * @return
     */
    public static Map<String, Object> getErrors(List<FieldError> errors) {
        Map<String, Object> map = new HashMap<>();
        for (FieldError fieldError : errors) {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return map;
    }
}
