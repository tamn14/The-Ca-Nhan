package com.example.The_Ca_Nhan.Config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//------------------- FILE DUNG DE CONVERT JWT -------------------//
public class CustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>>  {
    // cac cau hinh nay la mac dinh theo keycloak
    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";
    private static final String ROLE_PREFIX = "ROLE_";


    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String , Object> realmAccess = jwt.getClaim(REALM_ACCESS) ;
        // check loi , tranh runtime
        if(realmAccess == null || !(realmAccess.get(ROLES) instanceof List<?> roles)) {
            return Collections.emptyList() ;
        }

        return roles.stream()
                .filter(role-> role instanceof String)
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .collect(Collectors.toList()) ;
    }




}
