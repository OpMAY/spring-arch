package com.service;

import com.dao.TestDao;
import com.exception.GrantAccessDeniedException;
import com.exception.enums.GlobalExceptionType;
import com.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestService {
    private final TestDao testDao;

    public void testException() {
        if (true) {
            throw new GrantAccessDeniedException(GlobalExceptionType.GRANT_EXCEPTION);
        }
    }


    public boolean isRegistered(String id)   { //아이디 중복검사
        boolean bool = false;
        String userId = testDao.getId(id);
        if(userId !=null || userId.length() > 0){
            bool = true;
        }else{
            bool = false;
        }
        return bool;
    }

    public void register(User user) {

    }

    public void updateUser(User user) {
    }

    public int getUserById(String id) {
       Integer i = 0;
        return i;
    }
}