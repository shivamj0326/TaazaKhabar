package com.hackernews.taazakhabar.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @JsonProperty(value = "text")
    String comment;

    @JsonProperty(value = "by")
    String userHandle;

    @JsonProperty(value = "kids")
    Long[] childComments;
}
