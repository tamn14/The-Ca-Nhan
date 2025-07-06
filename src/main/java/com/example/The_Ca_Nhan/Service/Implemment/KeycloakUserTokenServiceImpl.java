package com.example.The_Ca_Nhan.Service.Implemment;

import com.example.The_Ca_Nhan.Config.UserRefreshTokenExchangeParam;
import com.example.The_Ca_Nhan.DTO.KeycloakRequest.UserAccessTokenExchangeParam;
import com.example.The_Ca_Nhan.DTO.KeycloakResponse.UserTokenExchangeResponse;
import com.example.The_Ca_Nhan.DTO.Request.LoginRequest;
import com.example.The_Ca_Nhan.DTO.Request.LogoutRequest;
import com.example.The_Ca_Nhan.DTO.Request.RefreshRequest;
import com.example.The_Ca_Nhan.DTO.Response.AuthenticationResponse;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Model.AuthModelTokenInfor;
import com.example.The_Ca_Nhan.Properties.IdpProperties;
import com.example.The_Ca_Nhan.Repository.IdentityProviderRepo;
import com.example.The_Ca_Nhan.Repository.UsersRepository;
import com.example.The_Ca_Nhan.Service.Interface.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class KeycloakUserTokenServiceImpl implements AuthenticationService {
    private final UsersRepository usersRepo ;
    private final IdentityProviderRepo identityProviderRepo ;
    private final IdpProperties idpProperties ;
    private final TokenBlackListServiceImpl tokenBlacklistService ;




    private void validateActiveUser(String username) {
        var user = usersRepo.findByUserName(username);
        if (user == null || !user.isEnable() || user.getDeleteAt() != null ) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

    }

    private AuthModelTokenInfor requestNewToken(LoginRequest loginRequest) {
        // kiem tra neu user da bi xoa trong db (xoa mem) thi khong duoc login
        validateActiveUser(loginRequest.getUserName());
        // tao tokenInfo
        UserAccessTokenExchangeParam param = UserAccessTokenExchangeParam.builder()
                .grant_type("password")
                .client_id(idpProperties.getClientId())
                .client_secret(idpProperties.getClientSecret())
                .userName(loginRequest.getUserName())
                .password(loginRequest.getPassword())
                .scope("openid")
                .build();

        UserTokenExchangeResponse response = identityProviderRepo.exchangeUserAccessToken(idpProperties.getRealm(), param);
        return new AuthModelTokenInfor(
                response.getAccessToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn())),
                response.getRefreshToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getRefreshExpiresIn()))
        );
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        AuthModelTokenInfor tokenInfo = requestNewToken(loginRequest);

        return AuthenticationResponse.builder()
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(tokenInfo.getAccessTokenExpiry().getEpochSecond() - Instant.now().getEpochSecond())
                .authenticated(true)
                .build() ;


    }

    @Override
    public String getAccessToken(LoginRequest loginRequest) {

        return requestNewToken(loginRequest).getAccessToken();
    }



    @Override
    public AuthenticationResponse refreshToken(RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.getToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        UserRefreshTokenExchangeParam param = UserRefreshTokenExchangeParam.builder()
                .grant_type("refresh_token")
                .client_id(idpProperties.getClientId())
                .client_secret(idpProperties.getClientSecret())
                .refresh_token(refreshToken)
                .build();

        UserTokenExchangeResponse response = identityProviderRepo.exchangeUserRefreshToken(idpProperties.getRealm(), param);
        AuthModelTokenInfor tokenInfo =  new AuthModelTokenInfor(
                response.getAccessToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn())),
                response.getRefreshToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getRefreshExpiresIn()))
        );
        return AuthenticationResponse.builder()
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(tokenInfo.getRefreshTokenExpiry().getEpochSecond() - Instant.now().getEpochSecond())
                .authenticated(true)
                .build();
    }


    @Override
    public void logout(LogoutRequest logoutRequest) {
        String refreshToken = logoutRequest.getRefreshToken();
        String accessToken = logoutRequest.getAccessToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        identityProviderRepo.revokeUserToken(
                idpProperties.getRealm(),
                idpProperties.getClientId(),
                idpProperties.getClientSecret(),
                refreshToken
        );
        if (accessToken != null && !accessToken.isEmpty()) {
            tokenBlacklistService.addToBlacklist(accessToken);
        }
    }
}
