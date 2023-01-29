package com.hackernews.taazakhabar.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.cglib.core.Local;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "story")
@Getter
@Setter
public class Story {
    @Id
    @Column(name = "storyId", unique = true)
    private Long id;
    private String title;
    private String url;
    private Integer score;
    @Column(name = "submitted_at")
    private LocalDateTime submissionTime;
    @Column(name = "submitted_by")
    private String userHandle;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
