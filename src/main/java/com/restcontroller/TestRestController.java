package com.restcontroller;

import com.response.DefaultRes;
import com.response.Message;
import com.service.TestService;
import com.util.Encryption.EncryptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestRestController {
    private final TestService testService;

    @Value("${KAKAO.CLIENT_ID}")
    private String KAKAO_CLIENT_ID;

    @Value("${KAKAO.CLIENT_SECRET}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${KAKAO.JAVASCRIPT}")
    private String KAKAO_JAVASCRIPT;
    /**
     * Rest Exception Error Test
     */



    public static void main(String[] args) {

           EncryptionService encryptionService = new EncryptionService();
           encryptionService.encryptAES("d313563cd5ce9c6fcc6f6ed1e6b65bfc",true);
           System.out.println( encryptionService.encryptAES("a0a20ee78364f16b2fce94d6a4847f75",false));
       }


    @RequestMapping(value = "/test/rest/exception", method = RequestMethod.GET)
    public ResponseEntity testException() {
        Message message = new Message();
        /*testService.testException();*/
        return new ResponseEntity(DefaultRes.res(HttpStatus.OK, message, true), HttpStatus.OK);
    }

    @RequestMapping(value = "/sns/key", method = RequestMethod.POST)
    public ResponseEntity getKey(HttpServletRequest request
                                 ) {
        Message message = new Message();
        message.put("javascript",KAKAO_JAVASCRIPT);
        return new ResponseEntity(DefaultRes.res(HttpStatus.OK, message, true), HttpStatus.OK);
    }


}
