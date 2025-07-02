package com.schoolhealth.schoolmedical.service.authenticate;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.model.dto.request.AuthRequest;
import com.schoolhealth.schoolmedical.model.dto.request.RegisterRequest;
import com.schoolhealth.schoolmedical.model.dto.response.AuthenticationResponse;
import com.schoolhealth.schoolmedical.repository.PupilRepo;
import com.schoolhealth.schoolmedical.repository.UserRepository;
import com.schoolhealth.schoolmedical.service.user.UserIdGenerator;
import com.schoolhealth.schoolmedical.service.user.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AuthenticateServiceImpl implements  AuthenticateService {

    private final UserRepository userRepository;
    private final PupilRepo pupilRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserIdGenerator userIdGenerator;

    @Override
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        // Xác định role cho user mới
        Role userRole = request.getRole() != null ? request.getRole() : Role.PARENT;

        // Tạo userId tự động theo định dạng tương ứng với role
        String userId = userIdGenerator.generateUserId(userRole);

        // Xây dựng đối tượng User từ request
        var user = User.builder()
                .userId(userId) // Sử dụng ID tự động theo định dạng yêu cầu
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword())) // Mã hóa password
                .birthDate(request.getBirthDate())
                .role(userRole) // Sử dụng role đã xác định
                .isActive(true)
                .build();

        // Lưu người dùng mới
        User savedUser = userRepository.save(user);

        // Nếu là PARENT, tìm và liên kết với các học sinh
        if (savedUser.getRole() == Role.PARENT) {
            // Tìm học sinh theo trường parentPhoneNumber
            List<Pupil> childrenByDirectPhone = pupilRepo.findByParentPhoneNumber(request.getPhoneNumber());

            // Tìm học sinh đã được liên kết qua bảng quan hệ
            List<Pupil> childrenByRelation = pupilRepo.findByLinkedParentPhoneNumber(request.getPhoneNumber());

            // Gộp và loại bỏ trùng lặp
            List<Pupil> allChildren = Stream.concat(
                childrenByDirectPhone.stream(),
                childrenByRelation.stream()
            ).distinct().collect(Collectors.toList());

            if (!allChildren.isEmpty()) {
                for (Pupil pupil : allChildren) {
                    if (pupil.getParents() == null) {
                        pupil.setParents(new ArrayList<>());
                    }
                    if (!pupil.getParents().contains(savedUser)) {
                        pupil.getParents().add(savedUser);
                    }
                }
                pupilRepo.saveAll(allChildren);
            }
        }

        // Tạo JWT token
        var jwtToken = jwtService.generateToken(new HashMap<>(), user);
        String fullName = savedUser.getLastName() + " " + savedUser.getFirstName();

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .role(savedUser.getRole().name())
                .fullName(fullName)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhoneNumber(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getUserId());
        extraClaims.put("role", user.getRole().name());
        var jwtToken = jwtService.generateToken(extraClaims, user);
        String fullName = user.getLastName() + " " + user.getFirstName();

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .role(user.getRole().name())
                .fullName(fullName)
                .build();
    }
}
