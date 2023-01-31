package com.hackernews.taazakhabar.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hackernews.taazakhabar.common.converter.UnixTimeConverter;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
@EqualsAndHashCode
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
    private LocalDateTime submissionTime;
    @JsonProperty(value = "by")
    private String userHandle;
    @JsonProperty(value = "kids")
    private Long[] commentIds;
}
