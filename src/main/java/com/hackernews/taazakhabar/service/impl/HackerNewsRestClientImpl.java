package com.hackernews.taazakhabar.service.impl;

import com.hackernews.taazakhabar.common.dto.CommentDto;
import com.hackernews.taazakhabar.common.dto.StoryDto;
import com.hackernews.taazakhabar.common.dto.response.CommentResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Component
public class HackerNewsRestClientImpl {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private Executor executor;
    @Value("${taazakhabar.client.hackernews.endpoints.topstory}")
    private String TOP_STORY_API = "";

    @Value("${taazakhabar.client.hackernews.endpoints.item}")
    private String ITEM_API = "";

    @Value("${taazakhabar.client.hackernews.cache.ttl:15}")
    private int cacheLiveTime;

    @Value("${taazakhabar.client.hackernews.item.limit}")
    private long LIMIT_ITEM = 0l ;

    public List<StoryDto> getTopStories() {
        Long[] storyIds = getAllTopStoryIds();
        return fetchStoryMetaData(storyIds);
    }

    public List<CommentResponseDto> getCommentsForStory(Long storyId) {
        StoryDto story = getStory(storyId);
        List<Long> commentIds = Arrays.asList(story.getCommentIds());
        return fetchCommentsMetaData(commentIds);
    }

    private List<StoryDto> fetchStoryMetaData(Long[] storyIds) {
        return Arrays.stream(storyIds)
                .map(id ->
                        getCompletableFutureStory(id))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(), completableFutureList ->
                                completableFutureList.stream()
                                        .map(CompletableFuture::join)
                                        .sorted((a, b) -> b.getScore() - a.getScore())
                                        .limit(LIMIT_ITEM)

                ))
                .collect(Collectors.toList());
    }

    private List<CommentResponseDto> fetchCommentsMetaData(List<Long> commentIds) {
        return commentIds.stream()
                .map(id ->
                        getCompletableFutureComment(id))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(), completableFutureList ->
                                completableFutureList.stream()
                                        .map(CompletableFuture::join)
                                        .sorted(getCommentDtoComparator())
                                        .limit(LIMIT_ITEM)
                                        .map(comment -> this.mapper.map(comment, CommentResponseDto.class))
                ))
                .collect(Collectors.toList());
    }

    private CompletableFuture<StoryDto> getCompletableFutureStory(Long storyId) {
        return CompletableFuture.supplyAsync(() ->
                getStory(storyId), executor);
    }
    private CompletableFuture<CommentDto> getCompletableFutureComment(Long commentId) {
        return CompletableFuture.supplyAsync(() ->
                getComment(commentId), executor);
    }

    private StoryDto getStory(Long storyId) {
        return restTemplate.getForObject(ITEM_API, StoryDto.class, storyId);
    }

    private CommentDto getComment(Long commentId) {
        return restTemplate.getForObject(ITEM_API, CommentDto.class, commentId);
    }

    private Long[] getAllTopStoryIds() {
        return restTemplate.getForObject(TOP_STORY_API, Long[].class);
    }

    private static Comparator<CommentDto> getCommentDtoComparator() {
        return (a, b) -> (b.getChildComments() == null ? 0 : b.getChildComments().length) - (a.getChildComments() == null ? 0 : a.getChildComments().length);
    }

}
