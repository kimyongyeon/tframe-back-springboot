package com.skt.classic.web.template.controller;

import com.skt.classic.web.template.CommonConstants;
import com.skt.classic.web.template.dto.User;
import com.skt.classic.web.template.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController /** JSON 타입으로 리턴하기 위한 Controller로 html파일과 연결되지 않는다. */
@RequestMapping("/api") /** 부모 Path를 지정한다. 예) http://localhost:8080/api/user 에서 api가 부모 path임 */
@Slf4j /** log 관련 롬복 */
public class MainRestController {

    /** 필요한 서비스를 불러옵니다. */
    @Autowired
    UserService userService;

    /**
     * @description userSelect/Sample 전체조회
     * 1. http://localhost:8080/api/user 로 요청시
     * 2. userService.findAll(): 트랜잭션 처리용도
     * 3. userMapper.findAll(): interface 메서드와 XML 파일을 연결함
     * 4. userMapper.xml => 실제 SQL Query 구문이 있는 파일
     * 5. 리턴타입에 맞게 결과 리턴
     * 6. 화면에 JSON 타입으로 리턴
     */
    @GetMapping(value = "/user")
    public Map<String, Object> userSelect() {
        log.debug("/api/user uri called");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(CommonConstants.JSON_RETURN_CODE_KEY, CommonConstants.JSON_RETURN_CODE_SUCCESS);
        resultMap.put(CommonConstants.JSON_RETURN_MSG_KEY, userService.findAll());
        return resultMap;
    }
    /**
     * @description userDeleteAll/Sample 전체삭제
     * 1. http://localhost:8080/api/user 로 요청시
     */
    @DeleteMapping("/user")
    public Map<String, Object> userDeleteAll() {
        Map<String, Object> resultMap = new HashMap<>();
        boolean procFlag = true;
        try {
            userService.deleteAll();
        } catch (Exception e) {
            procFlag = false;
        }
        resultMap.put(CommonConstants.JSON_RETURN_CODE_KEY, procFlag ?
                CommonConstants.JSON_RETURN_CODE_SUCCESS : CommonConstants.JSON_RETURN_CODE_FAIL);
        resultMap.put(CommonConstants.JSON_RETURN_MSG_KEY, procFlag?
                CommonConstants.JSON_RETURN_MSG_SUCCESS : CommonConstants.JSON_RETURN_MSG_FAIL);
        return resultMap;
    }
    /**
     * @description userInsert/Sample 저장
     * 1. http://localhost:8080/api/user 로 요청시
     */
    @PostMapping(value = "/user" , consumes = "application/json")
    public Map<String, Object> userInsert(@RequestBody User user) {
        Map<String, Object> resultMap = new HashMap<>();
        boolean procFlag = true;
        try {
            userService.insert(user.getIdx(), user.getName(), user.getLocal());
        } catch (Exception e) {
            procFlag = false;
        }
        resultMap.put(CommonConstants.JSON_RETURN_CODE_KEY, procFlag ?
                CommonConstants.JSON_RETURN_CODE_SUCCESS : CommonConstants.JSON_RETURN_CODE_FAIL);
        resultMap.put(CommonConstants.JSON_RETURN_MSG_KEY, procFlag?
                CommonConstants.JSON_RETURN_MSG_SUCCESS : CommonConstants.JSON_RETURN_MSG_FAIL);
        return resultMap;
    }

    /**
     * @description userInsert/Sample 페이징 조회
     * 1. http://localhost:8080/api/userPaging?page=0&size=10 로 요청시
     */
    @GetMapping(value = "/userPaging")
    public Map<String, Object> userPaging (Pageable pageable) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(CommonConstants.JSON_RETURN_CODE_KEY, CommonConstants.JSON_RETURN_CODE_SUCCESS);
        resultMap.put(CommonConstants.JSON_RETURN_MSG_KEY, userService.findWithPagination(pageable));
        return resultMap;
    }
}
