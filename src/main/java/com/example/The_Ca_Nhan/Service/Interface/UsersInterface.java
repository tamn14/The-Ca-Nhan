package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.DTO.Request.*;
import com.example.The_Ca_Nhan.DTO.Response.SkillResponse;
import com.example.The_Ca_Nhan.DTO.Response.UsersResponse;
import com.example.The_Ca_Nhan.Entity.Users;

import java.util.List;

public interface UsersInterface {
    public UsersResponse createUser (UsersRequest request) ;
    public List<UsersResponse> getAllUsers() ;
    public UsersResponse getUserById(int id) ;
    public UsersResponse updateUser (UsersUpdateRequest usersRequest ) ;
    public UsersResponse getMyInfor() ;
    public void changePassword (ChangePasswordRequest request) ;
    public void deleteUser(int id) ;
    public UsersResponse VerifyUsers(VerifyUserRequest accountNumber ,  int id ) ;

}
