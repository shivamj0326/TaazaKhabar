package com.hackernews.taazakhabar.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommentDto {

    @JsonProperty(value = "text")
    String comment;

    @JsonProperty(value = "by")
    String userHandle;

    @JsonProperty(value = "kids")
    Long[] childComments;
}
