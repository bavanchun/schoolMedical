package com.schoolhealth.schoolmedical.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schoolhealth.schoolmedical.constant.ValidationConstants;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "user")
public class User implements UserDetails{
    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "birth_date", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @Column(nullable = false, length = 72)
    private String password;

    @Email(message = "Email không hợp lệ")
    @Column(nullable = true)
    private String email;

    @Pattern(regexp = ValidationConstants.PHONE_NUMBER_REGEX,
            message = "Số điện thoại không hợp lệ")
    @Column(name = "phone_number", nullable = false, length = 12)
    private String phoneNumber;

    @Column(nullable = true)
    private String avatar;

    @Column(name = "created_at", nullable = false,  updatable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;

    @OneToMany(mappedBy = "authorId", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = true)
    private List<Blog> blogs;

    @ManyToMany(mappedBy = "parents")
    private List<Pupil> pupils;

//    // sau
//    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PupilParent> pupilParents;

//    /**
//     * Phương thức tiện ích để lấy danh sách tất cả học sinh của phụ huynh
//     * @return Danh sách các Pupil (học sinh) mà User này là phụ huynh
//     */
//    @Transient
//    public List<Pupil> getChildren() {
//        if (pupilParents == null) {
//            return new ArrayList<>();
//        }
//        return pupilParents.stream()
//                          .map(PupilParent::getPupil)
//                          .toList();
//    }

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = true)
    private List<UserNotification> userNotifications;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
