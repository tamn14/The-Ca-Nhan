package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.DTO.Request.SkillRequest;
import com.example.The_Ca_Nhan.DTO.Request.UsersRequest;
import com.example.The_Ca_Nhan.DTO.Request.UsersUpdateRequest;
import com.example.The_Ca_Nhan.DTO.Response.SkillResponse;
import com.example.The_Ca_Nhan.DTO.Response.UsersResponse;

import java.util.List;

public interface UsersInterface {
    public UsersResponse insertUsers(UsersRequest request) ;
    public UsersResponse updateUsers(UsersUpdateRequest request , int Id) ;
    public void deleteUsers(int Id);
    public List<UsersResponse> findAll() ;
    public UsersResponse findById(int id) ;

}
