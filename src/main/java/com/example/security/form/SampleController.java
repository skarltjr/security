package com.example.security.form;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class SampleController {

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        // 모두 접근이 가능하지만 로그인 여부에 따라 첫 화면을 달리하고싶다
        if (principal == null) {
            model.addAttribute("message", "Hello Spring security");
        } else {
            model.addAttribute("message", "Hello "+ principal.getName());
        }
        return "index";
    }
    @GetMapping("/info")
    public String info(Model model) {
        model.addAttribute("message", "Hello Spring security");
        return "info";
    }

    // 아래는 로그인 (인증된) 사람만 접근 가능

    @GetMapping("/admin")
    public String admin(Model model,Principal principal) {
        model.addAttribute("message", "Hello "+ principal.getName());
        return "admin";
    }
    @GetMapping("/dashBoard")
    public String dashBoard(Model model, Principal principal) {
        model.addAttribute("message", "Hello "+ principal.getName());
        return "dashBoard";
    }
}
