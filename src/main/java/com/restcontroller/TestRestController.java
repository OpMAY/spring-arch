package com.restcontroller;

import com.response.DefaultRes;
import com.response.Message;
import com.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestRestController {
    private final TestService testService;

    /**
     * Rest Exception Error Test
     */
    @RequestMapping(value = "/test/rest/exception", method = RequestMethod.GET)
    public ResponseEntity testException() {
        Message message = new Message();
        /*testService.testException();*/
        return new ResponseEntity(DefaultRes.res(HttpStatus.OK, message, true), HttpStatus.OK);
    }
}
