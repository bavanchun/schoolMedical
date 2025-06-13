package com.schoolhealth.schoolmedical.service.authenticate;

import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.model.dto.request.AuthRequest;
import com.schoolhealth.schoolmedical.model.dto.request.RegisterRequest;
import com.schoolhealth.schoolmedical.model.dto.response.AuthenticationResponse;
import com.schoolhealth.schoolmedical.repository.UserRepository;
import com.schoolhealth.schoolmedical.service.user.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticateServiceImpl implements  AuthenticateService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(request.getPassword())
                .birthDate(request.getBirthDate())
                .role(Role.PARENT)
                .build();
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(new HashMap<>(), user);

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthRequest request) {
        return null;
    }
}
