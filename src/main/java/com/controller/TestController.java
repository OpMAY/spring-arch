package com.controller;

import com.exception.GrantAccessDeniedException;
import com.exception.enums.GlobalExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
}
