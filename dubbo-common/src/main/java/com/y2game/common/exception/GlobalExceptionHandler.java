package com.y2game.common.exception;

import com.y2game.common.pojo.ErrorCodes;
import com.y2game.common.pojo.RestResp;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

        @ExceptionHandler(value=Exception.class)
        public RestResp allExceptionHandler(HttpServletRequest request, Exception exception) throws Exception {
                exception.printStackTrace();
                if(exception instanceof  ServiceException) {
                    ErrorCodes code = ((ServiceException) exception).getCode();
                    return new RestResp<String>(code.getErrorCode(),code.getInfo());
                }
               return  new RestResp<String>(ErrorCodes.SERVICE_ERROR.getErrorCode(),ErrorCodes.SERVICE_ERROR.getInfo());
        }

}