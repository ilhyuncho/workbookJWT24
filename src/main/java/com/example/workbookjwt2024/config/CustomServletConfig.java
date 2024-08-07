package com.example.workbookjwt2024.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){


        // http://localhost:8090/files/sample.html 로 접속시 정상 동작

        registry.addResourceHandler("/files/**")    // 어떤 경로로 들어왔을때 매핑 해줄지
                .addResourceLocations("classpath:/static/");    // 실제 파일이 있는 경로
    }
}
