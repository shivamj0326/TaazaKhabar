package com.hackernews.taazakhabar.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Entity
@Table(name = "story")
@Getter
@Setter
public class Story {
    @Id
    @Column(name = "storyId")
    private Long id;
    private String title;
    private String url;
    private Integer score;
    @Column(name = "submitted_at")
    private Instant submissionTime;
    @Column(name = "submitted_by")
    private String userHandle;
}
