package com.example.The_Ca_Nhan.Service.Implemment;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Service.Interface.S3Interface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Interface {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename() ;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
            return amazonS3.getUrl(bucketName, fileName).toString(); // URL ảnh
        }
        catch (IOException e) {
            throw new AppException(ErrorCode.S3_SERVICE_FAILED);
        }
    }

    @Override
    public void deleteImage(String fileUrl) {
        try {
            URI uri = new URI(fileUrl) ;
            String key = uri.getPath().substring(1) ; // bo dau '/' dau tien
            amazonS3.deleteObject(bucketName , key);
        }
        catch (Exception e) {
            throw new AppException(ErrorCode.S3_SERVICE_FAILED);
        }
    }


}
