package com.controller;

import com.exception.GrantAccessDeniedException;
import com.exception.enums.GlobalExceptionType;
import com.model.User;
import com.util.Encryption.EncryptionService;
import com.util.Encryption.JWTEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TestController {
    public static void main(String[] args) {

    }

    /**
     * Recover Interceptor Active
     */
    @RequestMapping(value = "/test/recover", method = RequestMethod.GET)
    public ModelAndView testRecover() {
        return new ModelAndView("error/recover");
    }

    @RequestMapping(value = "/test/error", method = RequestMethod.GET)
    public ModelAndView testError() {
        return new ModelAndView("error/error");
    }

    /**
     * Exception Error Test
     */
    @RequestMapping(value = "/test/exception", method = RequestMethod.GET)
    public ModelAndView testException() {
        if (true) {
            throw new GrantAccessDeniedException(GlobalExceptionType.GRANT_EXCEPTION);
        }
        return new ModelAndView("error/error");
    }

    /*
     * JWT Token Test
     * */
    private final EncryptionService encryptionService;

    @RequestMapping(value = "/test/jwt", method = RequestMethod.GET)
    public ModelAndView testJwt(HttpServletRequest request) {
        request.getSession().setAttribute(JWTEnum.JWTToken.name(), encryptionService.encryptJWT(new User()));
        return new ModelAndView("sample");
    }

    /**
     * Logic Test
     */
    @RequestMapping(value = "/test/logic", method = RequestMethod.GET)
    public ModelAndView test(HttpServletRequest request) {
        return new ModelAndView("test/file-test");
    }
}
