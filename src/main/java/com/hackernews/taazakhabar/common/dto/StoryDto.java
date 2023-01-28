package com.hackernews.taazakhabar.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown=true)
public class StoryDto {

    @JsonProperty(value = "id")
    private Long id;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "url")
    private String url;
    @JsonProperty(value = "score")
    private int score;
    @JsonProperty(value = "time")
    @JsonDeserialize(converter = UnixTimeConverter.class)
    private Instant submissionTime;
    @JsonProperty(value = "by")
    private String userHandle;
    @JsonProperty(value = "kids")
    private Long[] commentIds;
}
