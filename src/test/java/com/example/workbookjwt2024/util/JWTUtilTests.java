package com.example.workbookjwt2024.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@Log4j2
public class JWTUtilTests {

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    public void testGenerate(){
        Map<String, Object> claimMap = Map.of("mid", "Abd");
        String jwtStr = jwtUtil.generateToken(claimMap,1);

        log.error(jwtStr);
    }

    @Test
    public void testValidate(){
        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MjMwODQ5NTgsIm1pZCI6IkFiZCIsImlhdCI6MTcyMzA4NDg5OH0.lz3uYR7n1g31jx3G0YdmpS2OI_BXJkabkUCST7V6uQQ";

        Map<String, Object> claim = jwtUtil.validateToken(jwtStr);

        log.error(claim);   // {exp=1723084958, mid=Abd, iat=1723084898}
    }


}
