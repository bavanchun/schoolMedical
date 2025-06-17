package com.schoolhealth.schoolmedical.service.user;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Objects;

public interface JwtService {
    String extractUserName(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    //String generateToken(Map<String, Objects> extraClaims, UserDetails userDetails);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
}
