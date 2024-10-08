package com.example.workbookjwt2024.security.filter;


import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Log4j2
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {

    public APILoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    // 필터에 인터셉트된 로그인 Request는 attemptAuthentication 함수로 전달됨
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        log.error("API Login FIlter-------------------");

        if(request.getMethod().equalsIgnoreCase("GET")){
            log.error("Get method not support");
            return null;
        }
        Map<String, String> jsonData = parseRequestJSON(request);

        log.error(jsonData);

        // 이후 UsernamePasswordAuthenticationFilter 에서 로그인 처리
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        jsonData.get("mid"), jsonData.get("mpw"));

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request){
        try(Reader reader = new InputStreamReader(request.getInputStream())){

            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);
        }
        catch(Exception e){
            log.error(e.getMessage());
        }

        return null;
    }
}
