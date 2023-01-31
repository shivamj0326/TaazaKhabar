package com.hackernews.taazakhabar.service.impl;

import com.hackernews.taazakhabar.common.dto.StoryDto;
import com.hackernews.taazakhabar.common.dto.response.StoryResponseDto;
import com.hackernews.taazakhabar.domain.Story;
import com.hackernews.taazakhabar.domain.StoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
/** Loads and Saves Stories from|in the DB **/
public class HackerNewsDBService {

    @Autowired
    private StoryRepository storyRepo;

    @Autowired
    private ModelMapper mapper;

    @Value("${taazakhabar.client.hackernews.cache.ttl:15}")
    private int cacheLiveTime;


    /**
     * @param stories mapped to List of {@link Story} and saved in DB
     */
    public void saveStoriesInDB(List<StoryDto> stories) {
        stories.parallelStream()
                .filter(story -> !this.storyRepo.findById(story.getId()).isPresent())
                .forEach(story -> this.storyRepo.save(this.mapper.map(story, Story.class)));
        log.debug("Saved stories in DB");
    }


    /**
     * @return List of {@link StoryDto} that have been persisted in DB for more than cacheLiveTime
     */
    public List<StoryDto> findPastStoriesDB() {
        List<StoryDto> pastStories = new ArrayList<>();
        this.storyRepo.findPastStories(LocalDateTime.now().minusMinutes(cacheLiveTime))
                .forEach(story -> pastStories.add(this.mapper.map(story, StoryDto.class)));
        log.debug("Stories Fetched from DB");
        return pastStories;
    }
}
