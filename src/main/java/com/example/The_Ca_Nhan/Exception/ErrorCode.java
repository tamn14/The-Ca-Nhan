package com.example.The_Ca_Nhan.Exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002, "user not existed", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1003, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1004, "You do not have permission", HttpStatus.FORBIDDEN),
    CARD_NOT_FOUND(1005,"Card not existed" ,HttpStatus.NOT_FOUND ),
    ACCOUNTS_BLOCK(1006,"Account has been block" ,HttpStatus.UNAUTHORIZED ),
    USER_EXISTED(1015, "User already existed", HttpStatus.CONFLICT),
    EMAIL_EXISTED(1016, "Email already existed", HttpStatus.CONFLICT),
    USERNAME_IS_MISSING(1017, "Username is missing", HttpStatus.BAD_REQUEST);


    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
