package com.skt.classic.web.template;

import com.skt.classic.web.template.mapper.UserMapper;
import com.skt.classic.web.template.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    // before setup
    @Before
    public void setUp() {
        userMapper.createUserTable();
        userService.insert(1L, "tony", "seoul");
    }

    // select test
    @Test
    public void testSelect() {
        List list = userService.findByIdx(1L);
        log.debug("list: " + list.toString());
    }

    // insert test

    // update test

    // rollback test
}
