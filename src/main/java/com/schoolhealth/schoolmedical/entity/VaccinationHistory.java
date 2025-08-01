package com.schoolhealth.schoolmedical.entity;

import com.schoolhealth.schoolmedical.entity.enums.VaccinationSource;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "vaccination_history")
public class VaccinationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private VaccinationSource source;

    @Column(name = "vaccinated_at")
    private LocalDateTime vaccinatedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccine_id", referencedColumnName = "vaccine_id", nullable = false)
    private Vaccine vaccine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pupil_id", referencedColumnName = "pupil_id", nullable = false)
    private Pupil pupil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", referencedColumnName = "campaign_id", nullable = true)
    private VaccinationCampagin campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id", referencedColumnName = "disease_id", nullable = false)
    private Disease disease;
}
