package com.y2game.common.pojo;


import java.io.Serializable;

/**
 * 前后端交互数据标准
 * @author Exrickx
 * @Date 2018/03/24
 */
public class RestResp<T> implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 失败消息
     */
    private String message;

    /**
     * 返回代码
     */
    private Integer code=200;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 结果对象
     */
    private T result;

    public RestResp(T t){
        this.result=t;
    }

    public RestResp(Integer code, String msg){
        this.code=code;
        this.message=msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }


}
