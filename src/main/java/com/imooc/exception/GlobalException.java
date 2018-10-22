package com.imooc.exception;

import com.imooc.result.CodeMsg;

/**
 * Created by ${User} on 2018/10/21
 */
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private CodeMsg cm;
    public GlobalException(CodeMsg cm){
        super(cm.toString());
        this.cm = cm;
    }
    public CodeMsg getCm(){
        return cm;
    }
}
