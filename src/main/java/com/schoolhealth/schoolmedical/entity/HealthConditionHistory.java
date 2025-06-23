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
    // NÊN dùng Long cho id để chuẩn JPA và tránh lỗi tràn số khi dữ liệu lớn
    private Long conditionHistoryId;

    @Column(name = "name", nullable = false, length = 255)
    private String name; // Tên bệnh, dị ứng, tình trạng sức khỏe

    @Column(name = "reaction_or_note", columnDefinition = "TEXT")
    private String reactionOrNote; // Ghi chú hoặc phản ứng đặc biệt

    @Column(name = "image_url", length = 255)
    private String imageUrl; // Link ảnh minh họa (nếu có)

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Builder.Default // Cho phép set mặc định khi build bằng Lombok
    private boolean isActive = true; // Mặc định là true

    @Enumerated(EnumType.STRING)
    @Column(name = "type_history", nullable = false)
    private HealthTypeHistory typeHistory; // Dị ứng, bệnh nền, ...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pupil_id", nullable = false)
    private Pupil pupil; // Liên kết học sinh khai báo (FK)
}
