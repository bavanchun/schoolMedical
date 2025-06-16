package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Optional;

/**
 * Service tự động tạo ID cho User dựa trên role của họ:
 * ADMIN: Axxxx (A0001, A0002,...)
 * MANAGER: Mxxxx (M0001, M0002,...)
 * SCHOOL_NURSE: Sxxxx (S0001, S0002,...)
 * PARENT: Pxxxx (P0001, P0002,...)
 */
@Component
public class UserIdGenerator {

    @Autowired
    private UserRepository userRepository;

    /**
     * Tạo ID mới cho user dựa trên role
     * @param role Role của user
     * @return ID mới theo định dạng tương ứng với role
     */
    public String generateUserId(Role role) {
        // Xác định prefix dựa trên role
        String prefix = getRolePrefix(role);

        // Tìm ID lớn nhất hiện có cho role này
        int nextSequence = findMaxSequenceForRole(prefix) + 1;

        // Format số thành chuỗi 4 chữ số với padding 0
        String formattedSequence = String.format("%04d", nextSequence);

        // Tạo ID mới
        return prefix + formattedSequence;
    }

    /**
     * Lấy prefix cho ID dựa vào role
     * @param role Role của user
     * @return Prefix tương ứng (A, M, S, P)
     */
    private String getRolePrefix(Role role) {
        switch (role) {
            case ADMIN:
                return "A";
            case MANAGER:
                return "M";
            case SCHOOL_NURSE:
                return "S";
            case PARENT:
                return "P";
            default:
                throw new IllegalArgumentException("Role không hợp lệ: " + role);
        }
    }

    /**
     * Tìm số sequence lớn nhất hiện có cho một prefix
     * @param prefix Prefix của role (A, M, S, P)
     * @return Số lớn nhất hiện có, hoặc 0 nếu chưa có user nào
     */
    private int findMaxSequenceForRole(String prefix) {
        return userRepository.findAll().stream()
                .map(user -> user.getUserId())
                .filter(id -> id != null && id.startsWith(prefix))
                .map(id -> {
                    try {
                        // Trích xuất phần số từ ID (bỏ qua prefix)
                        return Integer.parseInt(id.substring(prefix.length()));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max(Comparator.naturalOrder())
                .orElse(0);
    }
}
