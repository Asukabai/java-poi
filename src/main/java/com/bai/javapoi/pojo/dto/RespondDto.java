package com.bai.javapoi.pojo.dto;


import com.bai.javapoi.pojo.code.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**响应实体类*/
@Data
@AllArgsConstructor
public class RespondDto<T> implements Serializable {
    /**时间戳*/
    private long timeStamp;
    /**响应码*/
    private Integer result;
    /**消息体*/
    private  String msg;
    /**响应数据*/
    private T data;

    /**
     * 没有数据返回，默认响应码为 1 ，消息体为：请求成功！
     */
    public RespondDto() {
        this.timeStamp = System.currentTimeMillis();
        this.result = ResultCode.OK;
        this.msg = "请求成功！";
    }

    /**
     * 没有数据返回，人为指定 消息体
     * @param msg
     */
    public RespondDto(String msg){
        this.timeStamp = System.currentTimeMillis();
        this.result = ResultCode.OK;
        this.msg = msg;
    }

    /**
     * 没有数据返回，人为指定 响应码、消息体
     * @param result
     * @param msg
     */
    public RespondDto(Integer result, String msg){
        this.timeStamp = System.currentTimeMillis();
        this.result = result;
        this.msg = msg;
    }

    /**
     * 有数据返回，默认响应码为 1 ，消息体为：请求成功！
     * @param data
     */
    public RespondDto(T data) {
        this.timeStamp = System.currentTimeMillis();
        this.result = ResultCode.OK;
        this.msg = "请求成功！";
        this.data = data;
    }

    /**
     * 有数据返回，人为指定 消息体
     * @param msg
     * @param data
     */
    public RespondDto(String msg, T data){
        this.timeStamp = System.currentTimeMillis();
        this.result = ResultCode.OK;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 有数据返回，人为指定 响应码、消息体
     * @param msg
     * @param result
     * @param data
     */
    public RespondDto(Integer result, String msg, T data){
        this.timeStamp = System.currentTimeMillis();
        this.result = result;
        this.msg = msg;
        this.data = data;
    }
}
