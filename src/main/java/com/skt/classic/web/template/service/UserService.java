package com.skt.classic.web.template.service;

import com.skt.classic.web.template.common.PageableHolder;
import com.skt.classic.web.template.dto.User;
import com.skt.classic.web.template.exception.BusinessException;
import com.skt.classic.web.template.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    UserMapper userMapper;
    /** 샘플 insert method */
    public void insert(Long idx, String name, String local) {
        userMapper.insert(idx, name, local);
    }
    /** 샘플 인덱스 조회 method */
    public List<User> findByIdx(Long idx) {
        return userMapper.findByIdx(idx);
    }
    /** 샘플 전체 조회 method */
    public List<User> findAll() {
        return userMapper.findAll();
    }
    /** 샘플 페이징 조회 method */
    public Page<User> findWithPagination(final Pageable pageable) {
        return PageableHolder.getPage(pageable, pa -> userMapper.findWithPagination(pa));
    }
    /** 샘플 전체 삭제 method */
    public void deleteAll() {
        userMapper.deleteAll();
    }
    /** 샘플 인덱스 삭제 method: 강제 Rollback 테스트 */
    public void deleteByIdx(Long idx) {
        userMapper.deleteByIdx(idx);
        if(1==1)
            throw new BusinessException("ERR01", "ROLLBACK");
    }
    /** 샘플 인덱스 수정 method */
    public void setFixedNameByIdx(String name, Long idx) {
        userMapper.setFixedNameByIdx(name, idx);
    }

}
