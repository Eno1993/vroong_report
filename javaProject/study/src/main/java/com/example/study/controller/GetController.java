package com.example.study.controller;

import com.example.study.model.SearchParam;
import com.example.study.model.network.Header;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // Localhost 8080/api 매칭

public class GetController {

    @RequestMapping(method = RequestMethod.GET, path= "/getMethod") // Localhost:8080/api/getMethod
    public String getRequest(){
            return "Complete!";
    }

    @GetMapping("/getParameter") // Localhost:8080/api/getParameter?id=1234&password=abcd
    public String getParameter(@RequestParam String id, @RequestParam String password){
        System.out.println("id : "+ id);
        System.out.println("password : "+ password);

        return id + password;
    }

    //Localhost 8080/api/multiParameter?account=abc&email=stucy@gmail.com&page =10
    @GetMapping("/getMultiParameter")
    public SearchParam getMultiParameter(SearchParam searchParam){
        System.out.println(searchParam.getAccount());
        System.out.println(searchParam.getEmail());
        System.out.println(searchParam.getPage());

        return searchParam; //자동 json 변환
    }

    @GetMapping("/header")
    public Header getHeader(){
        // {"resultCode":"OK","description":"OK"}
        return Header.builder().resultCode("OK").description("OK").build();
    }

}
