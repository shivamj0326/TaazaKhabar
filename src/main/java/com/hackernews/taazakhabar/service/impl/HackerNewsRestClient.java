package com.hackernews.taazakhabar.service.impl;

import com.hackernews.taazakhabar.common.dto.CommentDto;
import com.hackernews.taazakhabar.common.dto.StoryDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.concurrent.Executor;

public class HackerNewsRestClient {

    private RestTemplate restTemplate;

    HackerNewsRestClient(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Value("${taazakhabar.client.hackernews.endpoints.topstory}")
    private String TOP_STORY_API = "";

    @Value("${taazakhabar.client.hackernews.endpoints.item}")
    private String ITEM_API = "";

    private StoryDto getStory(Long storyId) {
        return restTemplate.getForObject(ITEM_API, StoryDto.class, storyId);
    }

    private CommentDto getComment(Long commentId) {
        return restTemplate.getForObject(ITEM_API, CommentDto.class, commentId);
    }

    private Long[] getAllTopStoryIds() {
        return restTemplate.getForObject(TOP_STORY_API, Long[].class);
    }
}
