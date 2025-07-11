package com.schoolhealth.schoolmedical.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id", nullable = false, unique = true)
    private Long blogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User authorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verifier_id", nullable = false)
    private User verifierId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false,  updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "last_updated_at", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDate lastUpdatedAt;

    @Column(name = "image_url", nullable = true, length = 255)
    private String imageUrl;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;



}
