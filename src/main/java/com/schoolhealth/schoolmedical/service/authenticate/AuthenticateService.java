package com.schoolhealth.schoolmedical.service.authenticate;

import com.schoolhealth.schoolmedical.model.dto.request.AuthRequest;
import com.schoolhealth.schoolmedical.model.dto.request.RegisterRequest;
import com.schoolhealth.schoolmedical.model.dto.response.AuthenticationResponse;

public interface AuthenticateService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthRequest request);
}
