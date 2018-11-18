package com.adwork.microservices.users.controller.advice;

import com.adwork.microservices.users.service.exception.UserServiceException;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorAdvice {

    @Bean
    public ErrorAttributes errorAttributes() {
        // Hide exception field in the return object
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
                //errorAttributes.remove("exception");
                return errorAttributes;
            }
        };
    }

    /*@ExceptionHandler(UserServiceException.class)
    public void handleCustomException(HttpServletResponse res, UserServiceException ex) throws IOException {
        res.sendError(ex.getHttpStatus().value(), ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(HttpServletResponse res) throws IOException {
        res.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
    }*/

    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletResponse res, Exception ex) throws IOException {
    	this.ex = ex;
        res.sendError(HttpStatus.BAD_REQUEST.value(), "Something went wrong");
    }
    
    Exception ex;
    
    @Override
    public String toString() {
    	return this.ex.toString();
    }

}
