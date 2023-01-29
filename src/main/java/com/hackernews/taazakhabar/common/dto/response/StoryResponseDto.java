package com.hackernews.taazakhabar.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

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
