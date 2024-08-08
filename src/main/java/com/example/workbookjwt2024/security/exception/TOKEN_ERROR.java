package com.example.workbookjwt2024.security.exception;

public enum TOKEN_ERROR {
    UNCCEPT(401, "Token is nulll or too short"),
    BADTYPE(401, "Token type Bearer"),
    MALFORM(403, "Malformed Token"),
    BADSIGN(403, "BadSignatured Token"),
    EXPIRED(403, "Expired Token");

    private int status;
    private String msg;

    TOKEN_ERROR(int status, String msg){
        this.status = status;
        this.msg = msg;
    }
    public int getStatus(){
        return this.status;
    }
    public String getMsg(){
        return this.msg;
    }
}
