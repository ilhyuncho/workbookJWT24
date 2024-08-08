package com.example.workbookjwt2024.security.exception;

import com.google.gson.Gson;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class AccessTokenException extends RuntimeException{

    TOKEN_ERROR token_error;

    public AccessTokenException(TOKEN_ERROR error){
        super(error.name());
        this.token_error = error;
    }

    public void sendResonseError(HttpServletResponse response){
        response.setStatus(token_error.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();
        String responseStr = gson.toJson(Map.of("msg", token_error.getMsg(), "time", new Date()));

        try{
            response.getWriter().println(responseStr);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }


}
