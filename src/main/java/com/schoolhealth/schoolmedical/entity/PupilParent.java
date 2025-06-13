package com.schoolhealth.schoolmedical.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pupil_parent")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PupilParent {

    @EmbeddedId
    private PupilParentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pupilId")
    @JoinColumn(name = "pupil_id")
    private Pupil pupil;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("parentId")
    @JoinColumn(name = "parent_id")
    private User parent;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type", nullable = false)
    private RelationType relationType;

}
