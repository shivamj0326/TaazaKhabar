package com.hackernews.taazakhabar.service.impl;

import com.hackernews.taazakhabar.common.dto.StoryDto;
import com.hackernews.taazakhabar.common.dto.response.CommentResponseDto;
import com.hackernews.taazakhabar.common.dto.response.StoryResponseDto;
import com.hackernews.taazakhabar.service.api.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides Implementation for {@link NewsService}
 * Delegates Implementation related to DB Access to {@link HackerNewsDBService}
 * Delegates Implementation related to REST Calls to {@link HackerNewsRestService}
 * Aggregates the Data and returns the response
 */
@Component
public class HackerNewsService implements NewsService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private HackerNewsRestService restService;

    @Autowired
    private HackerNewsDBService dbService;

    /**
     * Fetches Top Stories based on their score and the limit
     * @return List of {@link StoryResponseDto} Story ResponseTranfer object
     */
    @Cacheable(value = "topStories")
    @Override
    public List<StoryResponseDto> getTopStories(){
        List<StoryDto> stories = restService.getTopStories();
        dbService.saveStoriesInDB(stories);
        return mapToStoryResponse(stories);
    }

    /**
     * Fetches the Past Stories based on the cacheLiveTIme
     * @return List of {@link StoryResponseDto} Story ResponseTranfer object
     */
    @Override
    @Cacheable(value = "pastStories")
    public List<StoryResponseDto> getPastStories(){
        return dbService.findPastStoriesDB()
                        .stream()
                        .map(story -> this.mapper.map(story, StoryResponseDto.class))
                        .collect(Collectors.toList());
    }

    /**
     * Fetches the Top Comments having maximum child comments for a given id
     * @param id is the Unique Identifier for a {@link StoryDto}
     * @return List of {@link CommentResponseDto} the Comment ResponseTransfer object
     */
    @Override
    @Cacheable(value = "topComments")
    public List<CommentResponseDto> getCommentsForStory(Long id) {
        return restService.getCommentsForStory(id)
                .stream()
                .map(comment -> this.mapper.map(comment, CommentResponseDto.class))
                .collect(Collectors.toList());
    }

    private List<StoryResponseDto> mapToStoryResponse(List<StoryDto> stories) {
        return stories.stream()
                      .map(story -> this.mapper.map(story, StoryResponseDto.class))
                      .collect(Collectors.toList());
    }

}
