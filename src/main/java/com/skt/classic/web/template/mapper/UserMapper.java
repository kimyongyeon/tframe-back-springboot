package com.skt.classic.web.template.mapper;

import com.skt.classic.web.template.common.PageableHolder;
import com.skt.classic.web.template.dto.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    /** 샘플 insert mapper */
    void insert(@Param("idx") Long idx, @Param("name") String name, @Param("local") String local);

    /** 샘플 페이징 mapper */
    List<User> findWithPagination(Pageable pageable);

    /** 샘플 인덱스 조회 mapper */
    List<User> findByIdx(@Param("idx") Long idx);

    /** 샘플 100건 전체 조회 mapper */
    List<User> findAll();

    /** 샘플 인덱스 삭제 mapper */
    void deleteByIdx(@Param("idx") Long idx);

    /** 샘플 전제 삭제 mapper */
    void deleteAll();

    /** 샘플 인덱스별 수정 mapper */
    void setFixedNameByIdx(@Param("name") String name, @Param("idx") Long idx);

    /** 샘플 테이블 생성 jUnit 테스트 용도 mapper */
    void createUserTable();
}
