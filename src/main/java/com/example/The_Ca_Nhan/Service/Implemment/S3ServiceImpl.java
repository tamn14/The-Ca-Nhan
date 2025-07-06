package com.example.The_Ca_Nhan.Service.Implemment;


import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Properties.AWSProperties;
import com.example.The_Ca_Nhan.Service.Interface.S3Interface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Interface {
    private final S3Client s3Client;
    private final AWSProperties awsProperties ;


    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename() ;
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsProperties.getBucket())
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return s3Client.utilities()
                    .getUrl(b -> b.bucket(awsProperties.getBucket()).key(fileName))
                    .toString();
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
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(awsProperties.getBucket())
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteRequest);
        }
        catch (Exception e) {
            throw new AppException(ErrorCode.S3_SERVICE_FAILED);
        }
    }


}
