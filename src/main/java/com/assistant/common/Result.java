package com.assistant.common;

import java.io.Serializable;

/**
 * 全局统一 API 响应结果封装类
 * 所有 Controller 方法的返回值都必须使用此类包裹，
 * 前端 Vue 统一通过 code / message / data 三个字段进行解析。
 *
 * @param <T> 响应数据的类型
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态码：200 表示成功，其他值表示各类失败 */
    private Integer code;

    /** 提示信息 */
    private String message;

    /** 承载的业务数据 */
    private T data;

    public Result() {}

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ======================== 成功响应（有数据） ========================

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    // ======================== 失败响应 ========================

    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    // ======================== Getter / Setter ========================

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
