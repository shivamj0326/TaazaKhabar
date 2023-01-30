package com.hackernews.taazakhabar.service.impl;

import com.hackernews.taazakhabar.common.dto.StoryDto;
import com.hackernews.taazakhabar.common.dto.response.CommentResponseDto;
import com.hackernews.taazakhabar.common.dto.response.StoryResponseDto;
import com.hackernews.taazakhabar.domain.StoryRepository;
import com.hackernews.taazakhabar.service.api.NewsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HackerNewsService implements NewsService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private HackerNewsRestService restService;

    @Autowired
    private HackerNewsDBService dbService;

    @Cacheable(value = "topStories")
    @Override
    public List<StoryResponseDto> getTopStories(){

        List<StoryDto> stories = restService.getTopStories();
        dbService.saveStoriesInDB(stories);
        return mapToStoryResponse(stories);
    }

    @Override
    public List<StoryResponseDto> getPastStories(){
        return dbService.findPastStoriesDB()
                        .stream()
                        .map(story -> this.mapper.map(story, StoryResponseDto.class))
                        .collect(Collectors.toList());
    }

    @Override
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
