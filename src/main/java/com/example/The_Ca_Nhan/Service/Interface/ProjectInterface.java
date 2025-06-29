package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.DTO.Request.ProfilesRequest;
import com.example.The_Ca_Nhan.DTO.Response.ProfilesResponse;
import com.example.The_Ca_Nhan.DTO.Response.ProjectResponse;

import java.util.List;

public interface ProjectInterface {
    public ProjectResponse insertProject(ProfilesRequest request) ;
    public ProjectResponse updateProject(ProfilesRequest request , int Id) ;
    public void deleteProject(int Id);
    public List<ProjectResponse> findAll() ;
    public ProjectResponse findById(int id) ;

}
