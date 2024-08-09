package com.example.workbookjwt2024.security.filter;

import com.example.workbookjwt2024.security.exception.RefreshTokenException;
import com.example.workbookjwt2024.util.JWTUtil;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Ref;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final String refreshPath;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if(!path.equals(refreshPath)){
            log.error("skip refresh token filter.........");
            filterChain.doFilter(request, response);
            return;
        }
        log.error("RefreshToken Filter.....................");

        // 전송된 JSON에서 accessToken 과 refreshToken을 얻는다
        Map<String, String> mapTokens = parseRequestJSON(request);
        if(mapTokens != null){
            String accessToken = mapTokens.get("accessToken");
            String refreshToken = mapTokens.get("refreshToken");

            log.error("accessToken: " + accessToken);
            log.error("refreshToken: " + refreshToken);

            try{
                checkAccessToken(accessToken);
                log.error("checkAccessToken after---------------");
            }catch (RefreshTokenException refreshTokenException){
                refreshTokenException.sendResponseError(response);
                return;
            }

            Map<String, Object> refreshClaims = null;
            try{
                refreshClaims = checkRefreshToken(refreshToken);
                log.error("refreshClaims : " + refreshClaims);

                // refresh TOken의 유효 가간이 얼마 남지 않은 경우
                Integer exp = (Integer)refreshClaims.get("exp");

                Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);
                Date current = new Date(System.currentTimeMillis());

                // 만료 시간과 현재 시간의 간격 계산
                long gapTime = (expTime.getTime() - current.getTime());

                log.error("--------------------------------------");
                log.error("current : " + current);
                log.error("expTime : " + expTime);
                log.error("gap : " + gapTime);

                String mid = (String)refreshClaims.get("mid");

                String accessTokenValue = jwtUtil.generateToken(Map.of("mid", mid), 1);


                String refreshTokenValue = mapTokens.get("refreshToken");

                // RefreshToken이 3일도 안 남았다면
                //if(gapTime < (1000 * 60 * 60 * 24 * 3) ){
                if(gapTime < (1000 * 60 * 3) ){ // 3분
                    log.error("new Refresh Token required...");
                    refreshTokenValue = jwtUtil.generateToken(Map.of("mid", mid), 30);
                }
                log.error("Refresh Token result..................");
                log.error("accessToken: " + accessTokenValue);
                log.error("refreshToken: " + refreshTokenValue);

                sendTokens(accessTokenValue, refreshTokenValue, response);

            }catch (RefreshTokenException refreshTokenException){
                refreshTokenException.sendResponseError(response);
                return;
            }
        }
    }

    private void sendTokens(String accessToken, String refreshToken, HttpServletResponse response){
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String jsonStr = gson.toJson(Map.of("accessToken", accessToken, "refreshToken", refreshToken ));

        try {
            response.getWriter().println(jsonStr);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
    // accessToken 검증
    private void checkAccessToken(String accessToken) throws RefreshTokenException {
        try {
            jwtUtil.validateToken(accessToken);
        } catch (ExpiredJwtException expiredJwtException) {
            log.error("Access Token has expired");
        } catch (Exception exception) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    private Map<String, Object> checkRefreshToken(String refreshToken) throws RefreshTokenException{
        try {
            Map<String, Object> values = jwtUtil.validateToken(refreshToken);
            return values;
        } catch (ExpiredJwtException expiredJwtException) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
        } catch (MalformedJwtException malformedJwtException) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        } catch (Exception e) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }
    }
    private Map<String, String> parseRequestJSON(HttpServletRequest request) {

        try (Reader reader = new InputStreamReader(request.getInputStream())) {

            Gson gson = new Gson();

            return gson.fromJson(reader, Map.class);
        } catch (Exception e) {
            log.error(e.getMessage());

        }
        return null;
    }



}
