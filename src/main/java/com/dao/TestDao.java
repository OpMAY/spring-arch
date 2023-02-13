package com.dao;

import com.mapper.TestMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TestDao {
    private TestMapper mapper;

    private TestDao(SqlSession sqlSession) {

        this.mapper = sqlSession.getMapper(TestMapper.class);
    }


    public String getId(String id)   {

        return mapper.select(id);
    }


}