package com.hackernews.taazakhabar.common.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StoryResponseDto {
    private String title;
    private String url;
    private int score;

    private LocalDateTime submissionTime;
    private String userHandle;

}
