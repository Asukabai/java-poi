package com.bai.javapoi.interceptors;

import com.bai.javapoi.pojo.code.ResultCode;
import com.bai.javapoi.pojo.dto.RespondDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常响应
 *
 * @author zhangxuejin
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**处理其他异常*/
    @ExceptionHandler({FileSizeLimitExceededException.class})
    public RespondDto onKnowException(FileSizeLimitExceededException e){
        String message = e.getMessage();
        log.info("未知异常：{}，全局异常已生效",message);
        return new RespondDto(ResultCode.UNKNOWN_ABNORMAL,"excel文件大小超过最大文件20M限制!");
    }


    /**处理其他异常*/
    @ExceptionHandler({NullPointerException.class})
    public RespondDto onException(NullPointerException e){
        String message = e.getMessage();
        log.info("未知异常：{}，全局异常已生效",message);
        return new RespondDto(ResultCode.UNKNOWN_ABNORMAL,"excel文件解析失败，请检查上传的文件是否正确，重新上传！");
    }


    @ExceptionHandler(IllegalStateException.class)
    public RespondDto onException(Exception e){
        String message = e.getMessage();
        log.info("未知异常：{}，系统异常",message);
        return new RespondDto(ResultCode.UNKNOWN_ABNORMAL,"系统异常");
    }

//    @ExceptionHandler(RuntimeException.class)
//    public RespondDto onException(RuntimeException e){
//        String message = e.getMessage();
//        log.info("未知异常：{}，系统异常",message);
//        return new RespondDto(ResultCode.UNKNOWN_ABNORMAL,"系统异常");
//    }
}
