package com.example.The_Ca_Nhan.Service.Implemment;

import ch.qos.logback.classic.spi.EventArgUtil;
import com.example.The_Ca_Nhan.DTO.KeycloakRequest.Credential;
import com.example.The_Ca_Nhan.DTO.KeycloakRequest.UserCreationParam;
import com.example.The_Ca_Nhan.DTO.Request.*;
import com.example.The_Ca_Nhan.DTO.Response.UsersResponse;
import com.example.The_Ca_Nhan.Entity.Users;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Exception.KeycloakNormalizer;
import com.example.The_Ca_Nhan.Mapper.UsersMapper;
import com.example.The_Ca_Nhan.Properties.IdpProperties;
import com.example.The_Ca_Nhan.Properties.RoleTemplate;
import com.example.The_Ca_Nhan.Repository.IdentityProviderRepo;
import com.example.The_Ca_Nhan.Repository.UsersRepository;
import com.example.The_Ca_Nhan.Service.Interface.AuthenticationService;
import com.example.The_Ca_Nhan.Service.Interface.KeycloakClientTokenService;
import com.example.The_Ca_Nhan.Service.Interface.MailInterface;
import com.example.The_Ca_Nhan.Service.Interface.UsersInterface;
import com.example.The_Ca_Nhan.Util.Extract;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersInterface {
    private final UsersRepository usersRepository ;
    private final IdentityProviderRepo identityProviderRepo ;
    private final UsersMapper usersMapper ;
    private final KeycloakClientTokenService keycloakClientTokenService ;
    private final AuthenticationService authenticationService ;
    private final IdpProperties idpProperties ;
    private final KeycloakNormalizer keycloakNormalizer ;
    private final Extract extract ;
    private final MailInterface mailInterface ;

    @Value("${spring.mail.from}")
    private String mailForm ;

    @Value("${url.myself}")
    private String urlMySelf ;



    private void checkUserNameExisted(String username) {
        if (usersRepository.findByUserName(username) != null) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
    }

    private String createAccountNumber(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // hoặc thêm chữ thường nếu cần
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }


    private void checkEmailExisted (String email) {
        if (usersRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_EXISTED) ;
        }
    }

    private void checkDeleteAt(Users users) {
        if (users.getDeleteAt() != null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
    }

    @Override
    public UsersResponse VerifyUsers(VerifyUserRequest verifyUserRequest ,  int id) {
        Users users = usersRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)) ;

        if( verifyUserRequest.getAccountNumber().equals(users.getAccountNumber())) {
            users.setEnable(true);
        }
        else {
            users.setEnable(false);
        }
        usersRepository.save(users) ;
        return usersMapper.toDTO(users);
    }

    @Override
    @PreAuthorize( "hasRole('ADMIN')")
    public UsersResponse createUser(UsersRequest request) {
        // kiem tra user
        checkUserNameExisted(request.getUserName());

        // kiem tra email ton tai
        checkEmailExisted(request.getEmail());

        // tao accountNumber
        String accountNumber = createAccountNumber(5) ;
        String name = request.getLastName()+ request.getFirstName() ;
        // goi accountNumber den email de xac thuc tai khoan
        mailInterface.verifyEmail(mailForm, request.getEmail(), accountNumber , name);

        try {
            // lay access Token de co the goi API keycloak tao nguoi dung

            var accessToken = keycloakClientTokenService.getAccessToken() ;
            var creationResponse = identityProviderRepo.createUser(
                    idpProperties.getRealm() ,
                    "Bearer " + accessToken ,
                    UserCreationParam.builder()
                            .userName(request.getUserName())
                            .email(request.getEmail())
                            .lastName(request.getLastName())
                            .firstName(request.getFirstName())
                            .enabled(true)
                            .emailVerified(false)
                            .credentialList(List.of(Credential.builder()
                                    .type("password")
                                    .value(request.getPassword())
                                    .temporary(false)
                                    .build()))
                            .build());
            // lay keycloakID
            String userKeycloakId = extract.extractUserId(creationResponse) ;

            // gan role cho user vua tao
            var role = identityProviderRepo.getRealmRoleByName(idpProperties.getRealm(),
                    "Bearer " + accessToken,
                    RoleTemplate.USER.getValue()
            );
            identityProviderRepo.assignRealmRolesToUser(
                    idpProperties.getRealm(),
                    "Bearer " + accessToken,
                    userKeycloakId,
                    List.of(role)
            );

            // luu thong tin vao db
            Users users = usersMapper.toEntity(request) ;
            users.setKeycloakId(userKeycloakId);
            users.setCreateAt(LocalDate.now());
            users.setAccountNumber(accountNumber);
            users.setEnable(false);
            // luu duong dan dan toi trang ca nhan cua user do
            users.setUrl(urlMySelf + "/"+ request.getUserName());
            users.setDeleteAt(null);

            // luu user vao db ( vi day la he thong booking nen co the hieu khi tao user moi la tao passenger moi)
            usersRepository.save(users) ;
            return usersMapper.toDTO(users) ;

        }catch (FeignException feignException){
            throw keycloakNormalizer.handleKeycloakException(feignException);
        }
    }

    @Override
    @PreAuthorize( "hasRole('ADMIN')")
    public List<UsersResponse> getAllUsers() {
        return usersRepository.findByDeletedAtIsNull()
                .stream().map(usersMapper::toDTO).toList();
    }

    @Override
    @PreAuthorize( "hasRole('ADMIN')")
    public UsersResponse getUserById(int id) {
        Users users = usersRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)) ;
        return usersMapper.toDTO(users) ;
    }

    @Override
    @PreAuthorize( "hasRole('ADMIN')")
    @PostAuthorize("returnObject.userName == authentication.name")
    public UsersResponse updateUser(UsersUpdateRequest usersRequest) {
        // lay user hien tai tu phien dang nhap
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Users passenger = extract.getUserInFlowLogin() ;

        if (passenger == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        // kiem tra da delete
        checkDeleteAt(passenger);


        // cap nhat thong tin users
        try {
            // lay accessToken cua user
            // do user chi duoc cap nhat thong tin cua chinh user dang dang nhap nen khong dung accessToken cua client ma phai token cua user
            Jwt jwt = (Jwt) auth.getPrincipal();
            String accessToken = jwt.getTokenValue();
            // cap nhat thong tin tren keycloak
            identityProviderRepo.updateUser(
                    idpProperties.getRealm(),
                    "Bearer " + accessToken,
                    passenger.getKeycloakId() ,
                    usersRequest
            );
            // cap nhat trong db
            passenger.setLastName(usersRequest.getLastName());
            passenger.setFirstName(usersRequest.getFirstName());
            passenger.setAddress(usersRequest.getAddress());
            // kiem tra email
            if (usersRequest.getEmail() != null && !usersRequest.getEmail().equals(passenger.getEmail())) {
                if (usersRepository.existsByEmail(usersRequest.getEmail())) {
                    throw new AppException(ErrorCode.EMAIL_EXISTED) ;
                }
                passenger.setEmail(usersRequest.getEmail());
            }
            usersRepository.save(passenger) ;

            return usersMapper.toDTO(passenger) ;

        } catch (FeignException feignException){
            throw keycloakNormalizer.handleKeycloakException(feignException);
        }
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    @PostAuthorize("returnObject.userName == authentication.name")
    public UsersResponse getMyInfor() {

        var user = extract.getUserInFlowLogin() ;

        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        // kiem tra delete
        checkDeleteAt(user);
        return usersMapper.toDTO(user) ;
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public void changePassword(ChangePasswordRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users passenger = extract.getUserInFlowLogin() ;
        if (passenger == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        // kiem tra delete
        checkDeleteAt(passenger);


        // kiem tra co mat khau cu co khop khong
        //Y tuong la lay accessToken cua user neu duoc la dung
        try {
            authenticationService.getAccessToken(
                    new LoginRequest(passenger.getUserName(), request.getOldPassword())
            );
        } catch (FeignException.Unauthorized e) {
            throw new AccessDeniedException("Old password is incorrect");
        }

        try {
            // lay accessToken cua user
            // do user chi duoc cap nhat thong tin cua chinh user dang dang nhap nen khong dung accessToken cua client ma phai token cua user
            Jwt jwt = (Jwt) auth.getPrincipal();
            String accessToken = jwt.getTokenValue();
            // cap nhat mat khau tren keycloak
            identityProviderRepo.resetUserPassword(
                    idpProperties.getRealm() ,
                    "Bearer " + accessToken,
                    passenger.getKeycloakId() ,
                    Credential.builder()
                            .type("password")
                            .value(request.getNewPassword())
                            .temporary(false)
                            .build()
            );

        }
        catch (FeignException feignException){
            throw keycloakNormalizer.handleKeycloakException(feignException);
        }
    }

    @Override
    public void deleteUser(int id) {
        Users passenger = usersRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)) ;
        if (passenger.getDeleteAt() != null) {
            throw new AppException(ErrorCode.USER_ALREADY_DELETED);
        }
        passenger.setDeleteAt(LocalDateTime.now());
        usersRepository.save(passenger);
    }
}
