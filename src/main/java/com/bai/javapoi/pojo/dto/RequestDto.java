package com.bai.javapoi.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 请求实体类
 * @author zhangxuejin
 * */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto<T> implements Serializable {
    /**token*/
    private String token;
    /**请求id*/
    private String reqId;
    /**消息体*/
    private String msg;
    /**请求参数*/
    private T reqData;
}
