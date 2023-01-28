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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryResponseDto {
    private String title;
    private String url;
    private int score;
    @JsonSerialize(converter = InstantToStringConverter.class)
    private Instant submissionTime;
    private String userHandle;
}
