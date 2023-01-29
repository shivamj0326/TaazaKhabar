package com.hackernews.taazakhabar.service.api;


import com.hackernews.taazakhabar.common.dto.response.CommentResponseDto;
import com.hackernews.taazakhabar.common.dto.response.StoryResponseDto;

import java.util.List;

public interface NewsClient {

    List<StoryResponseDto> getTopStories();

    List<StoryResponseDto> getPastStories();

    List<CommentResponseDto> getCommentsForStory(Long id);

}
