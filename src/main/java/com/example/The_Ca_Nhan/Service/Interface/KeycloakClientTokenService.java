package com.example.The_Ca_Nhan.Service.Interface;


public interface KeycloakClientTokenService {
    public  String getAccessToken () ;
    // phuong thuc refresh token cho client (client dong vai tro la he thong khong phai user dang dang nhap)
    public void refreshToken() ;
}
