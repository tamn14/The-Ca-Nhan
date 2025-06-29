package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.DTO.Request.EducationRequest;
import com.example.The_Ca_Nhan.DTO.Request.ExperiencesRequest;
import com.example.The_Ca_Nhan.DTO.Response.EducationResponse;
import com.example.The_Ca_Nhan.DTO.Response.ExperiencesResponse;

import java.util.List;

public interface ExperienccesInterface {
    public ExperiencesResponse insertExp(ExperiencesRequest request) ;
    public ExperiencesResponse updateCard(ExperiencesRequest request , int Id) ;
    public void deleteExp(int id);
    public List<ExperiencesResponse> findAll() ;
    public ExperiencesResponse findById(int id) ;


}
