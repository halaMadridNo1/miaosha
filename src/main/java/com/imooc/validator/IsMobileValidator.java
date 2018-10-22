package com.imooc.validator;

import com.imooc.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by ${User} on 2018/10/22
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {

    private boolean required = false;
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
       if(required){
            return ValidatorUtil.isMobile(value);
       }else {
        if (StringUtils.isEmpty(value)){
            return true;
        }else {
            return ValidatorUtil.isMobile(value);
        }
       }

    }
}
