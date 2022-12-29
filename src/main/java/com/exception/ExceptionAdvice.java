package com.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

@ComponentScan
@ControllerAdvice(basePackages = "com.controller")
@Slf4j
public class ExceptionAdvice {
    @PostConstruct
    public void ExceptionAdvice() {
        log.info("ExceptionAdvice Initialized -> {}", "com.controller");
    }
    private ModelAndView modelAndView;

    /**
     * JSON 파싱, 역파싱 관련 Exception 발생시
     */
    @ExceptionHandler(JSONException.class)
    protected ModelAndView handleJSONException(HttpServletRequest request, JSONException e) {
        log.error("handleJSONException -> {}", e.getMessage());
        modelAndView = new ModelAndView("error/error");
        return modelAndView;
    }

    /**
     * Schedule Exception 발생시
     */
    @ExceptionHandler(InterruptedException.class)
    protected void handleInterruptedException(HttpServletRequest request, InterruptedException e) {
        log.error("handleInterruptedException -> {}", e.getMessage());
    }

    /**
     * 잘못된 Handler URI를 사용 했을때 NoHandlerFoundException 발생
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ModelAndView handleNoHandlerFoundException(HttpServletRequest request, NoHandlerFoundException e) {
        if (!e.getMessage().contains("/favicon.ico")) {
            log.error("handleNoHandlerFoundException -> {}", e.getMessage());
        }
        modelAndView = new ModelAndView("error/error");
        return modelAndView;
    }

    /**
     * Session JWTToken의 권한이 부족 및 없을 때 GrantAccessDeniedException 발생
     */
    @ExceptionHandler(GrantAccessDeniedException.class)
    protected ModelAndView handleGrantAccessDeniedException(HttpServletRequest request, GrantAccessDeniedException e) {
        log.error("handleGrantAccessDeniedException -> {}", e.getMessage());
        modelAndView = new ModelAndView("error/error");
        return modelAndView;
    }

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ModelAndView handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException -> {}", e.getMessage());
        modelAndView = new ModelAndView("error/error");
        return modelAndView;
    }

    /**
     * @ModelAttribut 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    protected ModelAndView handleBindException(HttpServletRequest request, BindException e) {
        log.error("handleBindException -> {}", e.getMessage());
        modelAndView = new ModelAndView("error/error");
        return modelAndView;
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ModelAndView handleMethodArgumentTypeMismatchException(HttpServletRequest request, MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException -> {}", e.getMessage());
        modelAndView = new ModelAndView("error/error");
        return modelAndView;
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ModelAndView handleHttpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException -> {}", e.getMessage());
        modelAndView = new ModelAndView("error/error");
        return modelAndView;
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ModelAndView handleAccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        log.error("handleAccessDeniedException -> {}", e.getMessage());
        modelAndView = new ModelAndView("error/error");
        return modelAndView;
    }

    @ExceptionHandler(NullPointerException.class)
    protected ModelAndView handleNullPointerException(HttpServletRequest request, Exception e) {
        log.error("NullPointerException -> {}", e.getMessage());
        modelAndView = new ModelAndView("error/error");
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    protected ModelAndView handleException(HttpServletRequest request, Exception e) {
        log.error("Global General Exception -> {}", e.getMessage());
        modelAndView = new ModelAndView("error/error");
        return modelAndView;
    }

    @ExceptionHandler(RuntimeException.class)
    protected ModelAndView handleRuntimeException(HttpServletRequest request, Exception e) {
        log.error("handleRuntimeException -> {}", e.getMessage());
        modelAndView = new ModelAndView("error/error");
        return modelAndView;
    }

    @ExceptionHandler(TokenInvalidException.class)
    protected ModelAndView handleTokenInvalidException(HttpServletRequest request, Exception e) {
        log.error("handleTokenInvalidException -> {}", e.getMessage());
        modelAndView = new ModelAndView("error/error");
        return modelAndView;
    }

    @ExceptionHandler(JWTVerificationException.class)
    protected ModelAndView handleJWTVerificationException(HttpServletRequest request, Exception e) {
        log.error("handleJWTVerificationException -> {}", e.getMessage());
        modelAndView = new ModelAndView("error/error");
        return modelAndView;
    }
}
