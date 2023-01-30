package com.hackernews.taazakhabar.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class CommentDto {

    @JsonProperty(value = "text")
    String comment;

    @JsonProperty(value = "by")
    String userHandle;

    @JsonProperty(value = "kids")
    Long[] childComments;
}
