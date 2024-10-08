package com.example.workbookjwt2024.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/sample")
public class SampleController {

    @ApiOperation("Sample Get DoA")
    @GetMapping("/doA")
    public List<String> doA(){
        return Arrays.asList("AAA","BBB","CCC");
    }
}
