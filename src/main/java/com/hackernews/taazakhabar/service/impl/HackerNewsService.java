package com.hackernews.taazakhabar.service.impl;

import com.hackernews.taazakhabar.common.dto.StoryDto;
import com.hackernews.taazakhabar.common.dto.response.CommentResponseDto;
import com.hackernews.taazakhabar.common.dto.response.StoryResponseDto;
import com.hackernews.taazakhabar.domain.Story;
import com.hackernews.taazakhabar.domain.StoryRepository;
import com.hackernews.taazakhabar.service.api.NewsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HackerNewsService implements NewsService {

    @Autowired
    private StoryRepository storyRepo;

    @Autowired
    private ModelMapper mapper;

    @Value("${taazakhabar.client.hackernews.item.limit}")
    private long LIMIT_ITEM = 0l ;

    @Value("${taazakhabar.client.hackernews.cache.ttl:15}")
    private int cacheLiveTime;

    @Autowired
    private HackerNewsRestService hackerNewsRestService;

    @Cacheable(value = "topStories")
    @Override
    public List<StoryResponseDto> getTopStories(){
        //System.out.println("Executing not from cache");
        List<StoryDto> stories = hackerNewsRestService.getTopStories();
        saveInDB(stories);
        return mapToStoryResponse(stories);
    }

    private List<StoryResponseDto> mapToStoryResponse(List<StoryDto> stories) {
        return stories.stream()
                .map(story -> this.mapper.map(story, StoryResponseDto.class))
                .collect(Collectors.toList());
    }

    private void saveInDB(List<StoryDto> stories) {
        stories.parallelStream()
                .forEach(story -> this.storyRepo.save(this.mapper.map(story, Story.class)));
    }
    @Override
    public List<StoryResponseDto> getPastStories(){
        List<StoryResponseDto> stories = new ArrayList<>();
        findPastStoriesDB(stories);
        return stories;
    }

    private void findPastStoriesDB(List<StoryResponseDto> stories) {
        this.storyRepo.findPastStories(LocalDateTime.now().minusMinutes(cacheLiveTime))
                      .forEach(story -> stories.add(this.mapper.map(story, StoryResponseDto.class)));
    }

    @Override
    public List<CommentResponseDto> getCommentsForStory(Long id) {
        return hackerNewsRestService.getCommentsForStory(id);
    }
}
