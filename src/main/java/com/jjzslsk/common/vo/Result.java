package com.jjzslsk.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private boolean success;
    private String  message;
    private T data;

    public static <T> Result<T> success(){
        return new Result<>(200,true,"操作成功",null);
    }

    public static <T> Result<T> success(T data){
        return new Result<>(200,true,"查询成功",data);
    }

    public static <T> Result<T> success(String message, T data){
        return new Result<>(200,true,message,data);
    }

    public static <T> Result<T> fail(){
        return new Result<>(200,false,"fail",null);
    }

    public static <T> Result<T> fail(Integer code){
        return new Result<>(code,false,"message",null);
    }

    public static <T> Result<T> fail(Integer code,String message){
        return new Result<>(code,false,message,null);
    }

}