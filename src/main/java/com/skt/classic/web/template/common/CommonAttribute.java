package com.skt.classic.web.template.common;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CommonAttribute {
    /**
     * @description headerAttribute/공통으로 화면에 내려줄 속성을 정의 합니다.
     * @param model
     */
    @ModelAttribute
    public void headerAttribute(Model model) {
        model.addAttribute("title", "no title");
    }
}
