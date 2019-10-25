package com.skt.classic.web.template.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
public class MainController {

    /**
     * @description onePath/uri 요청시 해당 이름으로 html 파일을 연결합니다.
     * 예) http://localhost:8080/test => resources/templates/test.html
     * */
    @GetMapping("/{path}")
    public String onePath(@PathVariable String path, Model model) {
        model.addAttribute("title", path + " title");
        return path;
    }

}
