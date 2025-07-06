package com.example.The_Ca_Nhan.Util;

import com.example.The_Ca_Nhan.Entity.Users;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class Extract {
    @Autowired
    private final UsersRepository usersRepository ;
    public String extractUserId(ResponseEntity<?> responseEntity) {
        // lay ra Location
        // Response ma keycloak tra ve gom HTTP va Location vd nhu sau :
        // HTTP/1.1 201 Created
        //Location: http://localhost:8080/admin/realms/myrealm/users/1a2b3c4d-5678-90ab-cdef-1234567890ab
        String Location = Objects.requireNonNull(responseEntity.getHeaders().get("Location")).get(0);
        // tach theo dau '/'
        String[] strings = Location.split("/") ;
        return strings[strings.length-1] ;
    }

    public Users getUserInFlowLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication() ;
        String userName = auth.getName() ;
        Users users = usersRepository.findByUserName(userName) ;
        if(users == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED) ;
        }
        return users ;
    }


}
