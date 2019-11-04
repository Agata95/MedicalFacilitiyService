package com.j25.finalproject.final_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/")
public class IndexController {


    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/aboutUs")
    public String aboutUs(){
        return "about-hospital";
    }

    @GetMapping("/login")
    public String indexZLogowaniem(){
        return "login-form";
    }

}
