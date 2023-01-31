package com.hackernews.taazakhabar.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;;
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

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        setCreatedAt(LocalDateTime.now());
    }

}
