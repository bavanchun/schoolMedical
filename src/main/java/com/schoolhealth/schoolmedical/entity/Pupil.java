package com.schoolhealth.schoolmedical.entity;

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

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "pupil_parent",
            joinColumns = @JoinColumn(name = "pupil_id", referencedColumnName = "pupil_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id", referencedColumnName = "user_id")
    )
    private List<User> parents;

//    // sau update
//    @OneToMany(mappedBy = "pupil", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PupilParent> pupilParents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id" /*, nullable = true*/) // Thay đổi nullable = false thành nullable = true
    private Grade grade;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = false)

    @JoinColumn(name = "pupil_id")
    private List<SendMedication> sendMedications;

    @OneToMany(mappedBy = "pupil", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = false)
    private List<HealthConditionHistory> healthConditionHistories;

    @OneToMany(mappedBy = "pupil", fetch = FetchType.LAZY)
    private List<MedicalEvent> medicalEvents;

    @OneToMany(mappedBy = "pupil", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = false)
    private List<HealthCheckConsentForm> healthCheckConsentForms;
}


