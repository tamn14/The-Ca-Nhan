package com.example.The_Ca_Nhan.Service.Interface;

import com.example.The_Ca_Nhan.DTO.Request.EducationRequest;
import com.example.The_Ca_Nhan.DTO.Response.EducationResponse;

import java.util.List;

public interface EducationInterface {
    public EducationResponse insertEdu(EducationRequest request) ;
    public EducationResponse updateCard(EducationRequest request , int Id) ;
    public void deleteEdu(int eduId);
    public List<EducationResponse> findAll() ;
    public EducationResponse findById(int id) ;


}
