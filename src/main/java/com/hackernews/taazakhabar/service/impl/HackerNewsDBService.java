package com.hackernews.taazakhabar.service.impl;

import com.hackernews.taazakhabar.common.dto.StoryDto;
import com.hackernews.taazakhabar.common.dto.response.StoryResponseDto;
import com.hackernews.taazakhabar.domain.Story;
import com.hackernews.taazakhabar.domain.StoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class HackerNewsDBService {

    @Autowired
    private StoryRepository storyRepo;

    @Autowired
    private ModelMapper mapper;

    @Value("${taazakhabar.client.hackernews.cache.ttl:15}")
    private int cacheLiveTime;

    public void saveStoriesInDB(List<StoryDto> stories) {
        stories.parallelStream()
                .forEach(story -> this.storyRepo.save(this.mapper.map(story, Story.class)));
    }

    public List<StoryDto> findPastStoriesDB() {
        List<StoryDto> pastStories = new ArrayList<>();
        this.storyRepo.findPastStories(LocalDateTime.now().minusMinutes(cacheLiveTime))
                .forEach(story -> pastStories.add(this.mapper.map(story, StoryDto.class)));
        return pastStories;
    }
}
