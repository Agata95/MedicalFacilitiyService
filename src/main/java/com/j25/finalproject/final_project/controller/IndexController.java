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

//    gdy nie jesteśmy zalogowani nie da się wejść do danej strony,
//    tylko po zalogowaniu można na tym mappingu przeglądać strony
    @GetMapping("/tylkodlakozakow")
    public String tylkoDlaKozakow(){
        return "index";
    }

    @GetMapping("/login")
    public String indexZLogowaniem(){
        return "login-form";
    }

}
