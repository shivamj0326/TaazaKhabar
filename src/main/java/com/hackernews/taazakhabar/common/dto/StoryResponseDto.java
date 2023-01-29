package com.hackernews.taazakhabar.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hackernews.taazakhabar.common.InstantToStringConverter;
import com.hackernews.taazakhabar.common.UnixTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.Instant;
import java.time.LocalDateTime;

import com.hazelcast.core.HazelcastJsonValue;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryResponseDto {
    private String title;
    private String url;
    private int score;

    private LocalDateTime submissionTime;
    private String userHandle;
}
