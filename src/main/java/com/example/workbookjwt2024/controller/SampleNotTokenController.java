package com.example.workbookjwt2024.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/apiNotToken/sample")  // token 지정 안해도 실행 됨
public class SampleNotTokenController {

    @ApiOperation("Sample Get DoA")
    @GetMapping("/doA")
    public List<String> doA(){
        return Arrays.asList("AAA","BBB","CCC");
    }
}
