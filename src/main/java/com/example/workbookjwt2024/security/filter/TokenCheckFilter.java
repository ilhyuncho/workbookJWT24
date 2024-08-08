package com.example.workbookjwt2024.security.filter;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.example.workbookjwt2024.security.exception.AccessTokenException;
import com.example.workbookjwt2024.security.exception.TOKEN_ERROR;
import com.example.workbookjwt2024.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter  {   // 하나의 요청에 한번씩 동작하는 필터

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if(!path.startsWith("/api/")){
            filterChain.doFilter(request, response);
            return;
        }

        log.error("Token Check filter................");

        try{
            validateAccessToken(request);
            filterChain.doFilter(request, response);
        }catch(AccessTokenException accessTokenException){
            accessTokenException.sendResonseError(response);
        }

    }

    private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException{

        String headerStr = request.getHeader("Authorization");

        if(headerStr == null || headerStr.length() < 8){

            log.error("headerStr check---------------------");
            throw new AccessTokenException(TOKEN_ERROR.UNCCEPT);
        }

        // Bearer 생략
        String tokenType = headerStr.substring(0, 6);
        String tokenStr = headerStr.substring(7);

        if(!tokenType.equalsIgnoreCase("Bearer")){
            log.error("tokenType !Bearer---------------------");
            throw new AccessTokenException(TOKEN_ERROR.BADTYPE);
        }

        log.error("tokenStr : " + tokenStr);

        try{
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);
            return values;
        }catch (MalformedJwtException malformedJwtException){
            log.error("MalformedJwtException---------------------");
            throw new AccessTokenException(TOKEN_ERROR.MALFORM);
        }catch (SignatureException signatureException){
            log.error("SignatureException---------------------");
            throw new AccessTokenException(TOKEN_ERROR.BADSIGN);
        }catch (ExpiredJwtException expiredJwtException){
            log.error("ExpiredJwtException---------------------");
            throw new AccessTokenException(TOKEN_ERROR.EXPIRED);
        }catch (Exception e){
            log.error("Exception---------------------");
            throw new AccessTokenException(TOKEN_ERROR.EXPIRED);
        }
    }

}
