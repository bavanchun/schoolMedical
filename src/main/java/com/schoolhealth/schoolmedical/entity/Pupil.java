package com.schoolhealth.schoolmedical.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "pupil")
public class Pupil {
    @Id
    @Column(name = "pupil_id", nullable = false, unique = true)
    private String pupilId;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private char gender;
    @Column(name = "avatar", length = 255)
    private String avatar;
    /**
     * Số điện thoại của phụ huynh để liên kết với tài khoản phụ huynh
     */
    @Column(name = "parent_phone_number", length = 15)
    private String parentPhoneNumber;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    @JoinTable(
            name = "pupil_parent",
            joinColumns = @JoinColumn(name = "pupil_id", referencedColumnName = "pupil_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id", referencedColumnName = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"pupil_id", "parent_id"})
    )
    private List<User> parents;

//    // sau update
//    @OneToMany(mappedBy = "pupil", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PupilParent> pupilParents;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pupil", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = false)
    private List<SendMedication> sendMedications;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pupil", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = false)
    private List<HealthConditionHistory> healthConditionHistories;

    @OneToMany(mappedBy = "pupil", fetch = FetchType.LAZY)
    private List<MedicalEvent> medicalEvents;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pupil", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = false)
    private List<HealthCheckConsentForm> healthCheckConsentForms;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pupil", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = false)
    private List<PupilGrade> pupilGrade;
}
