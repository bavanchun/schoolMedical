package com.schoolhealth.schoolmedical.entity;

import com.schoolhealth.schoolmedical.entity.enums.HealthTypeHistory;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "health_condition_history")
public class HealthConditionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "condition_history_id", nullable = false, unique = true)
    private int conditionId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "reaction_or_note", nullable = true, columnDefinition = "TEXT")
    private String reactionOrNote;

    @Column(name = "image_url", nullable = true, length = 255)
    private String imageUrl;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private HealthTypeHistory typeHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pupil_id", nullable = false)
    private Pupil pupil;
}
