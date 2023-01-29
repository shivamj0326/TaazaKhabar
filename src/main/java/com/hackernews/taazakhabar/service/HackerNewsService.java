package com.hackernews.taazakhabar.service;

import com.hackernews.taazakhabar.common.dto.CommentDto;
import com.hackernews.taazakhabar.common.dto.CommentResponseDto;
import com.hackernews.taazakhabar.common.dto.StoryDto;
import com.hackernews.taazakhabar.common.dto.StoryResponseDto;
import com.hackernews.taazakhabar.domain.Story;
import com.hackernews.taazakhabar.domain.StoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Component
public class HackerNewsService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StoryRepository storyRepo;

    @Autowired
    private Executor executor;

    @Autowired
    private ModelMapper mapper;


    @Cacheable(value = "topStories")
    public List<StoryResponseDto> getStories(){

        Long[] ids = restTemplate.getForObject("/topstories.json", Long[].class);
        System.out.println("Executing not from cache");

        List<StoryDto> stories = Arrays.stream(ids)
                .map(id ->
                        CompletableFuture.supplyAsync(() ->
                                restTemplate.getForObject("/item/" + id + ".json", StoryDto.class), executor))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(), completableFutureList ->
                                completableFutureList.stream()
                                        .map(CompletableFuture::join).sorted((a, b) -> b.getScore() - a.getScore())
                                        .limit(10)

                ))
                .collect(Collectors.toList());
        stories.stream().forEach(story -> this.storyRepo.save(this.mapper.map(story, Story.class)));

        return stories.stream().map(story -> this.mapper.map(story, StoryResponseDto.class)).collect(Collectors.toList());
    }


    public List<StoryResponseDto> getPastStories(){
        List<StoryResponseDto> stories = new ArrayList<>();
        this.storyRepo.findPastStories(LocalDateTime.now().minusMinutes(15)).forEach(story -> stories.add(this.mapper.map(story, StoryResponseDto.class)));
        return stories;
    }


    public List<CommentResponseDto> getComment(long storyId) {
        StoryDto story = restTemplate.getForObject("/item/" + storyId + ".json", StoryDto.class);
        List<Long> commentIds = Arrays.asList(story.getCommentIds());
        return commentIds.stream()
                .map(id ->
                        CompletableFuture.supplyAsync(() ->
                                restTemplate.getForObject("/item/" + id + ".json", CommentDto.class), executor))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(), completableFutureList ->
                                completableFutureList.stream()
                                        .map(CompletableFuture::join)
                ))
                .collect(Collectors.toList()).stream()
                         .sorted((a, b) -> (b.getChildComments() == null ? 0 : b.getChildComments().length) - (a.getChildComments() == null ? 0 : a.getChildComments().length))
                         .limit(10)
                .map(comment -> this.mapper.map(comment, CommentResponseDto.class))
                         .collect(Collectors.toList());
    }

}
