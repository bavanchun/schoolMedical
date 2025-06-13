package com.schoolhealth.schoolmedical.entity;

import com.schoolhealth.schoolmedical.entity.enums.VaccinationSource;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

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
    private int historyId;

    @Column(name = "vaccine_id")
    private int vaccineId;

    @Column(name = "pupil_id", length = 255)
    private String pupilId;

    @Column(name = "campaign_id")
    private int campaignId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private VaccinationSource source;

    @Column(name = "vaccinated_at")
    private LocalDateTime vaccinatedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    @Nationalized
    private String notes;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccine_id", referencedColumnName = "vaccine_id", insertable = false, updatable = false)
    private Vaccine vaccine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pupil_id", referencedColumnName = "pupil_id", insertable = false, updatable = false)
    private Pupil pupil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", referencedColumnName = "campaign_id", insertable = false, updatable = false)
    private VaccinationCampagin campaign;
}
