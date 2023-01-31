package com.hackernews.taazakhabar.service.impl;

import com.hackernews.taazakhabar.common.dto.CommentDto;
import com.hackernews.taazakhabar.common.dto.StoryDto;
import com.hackernews.taazakhabar.domain.Story;
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
/** Responsible for making rest Calls to HackerNews API **/
public class HackerNewsRestService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Executor executor;
    @Value("${taazakhabar.client.hackernews.endpoints.topstory}")
    private String TOP_STORY_API;

    @Value("${taazakhabar.client.hackernews.endpoints.item}")
    private String ITEM_API;

    @Value("${taazakhabar.client.hackernews.cache.ttl:15}")
    private int cacheLiveTime;

    @Value("${taazakhabar.client.hackernews.item.limit}")
    private long LIMIT_ITEM ;

    /**
     * Gets the #Ids of All the Top Stories from the HackerNews API
     * @see #getAllTopStoryIds()
     * Iterate through the Ids and fetch Complete StoryMetaData using the #Ids obtained
     * @see #fetchStoryMetaData(Long[])
     * @return List of {@link StoryDto} top stories fetched from the HackerNews API
     */
    public List<StoryDto> getTopStories() {
        Long[] storyIds = getAllTopStoryIds();
        return fetchStoryMetaData(storyIds);
    }

    /**
     * Fetches all #commentIds for a given story identified by #storyId
     * @see #getStory(Long)
     * Fetches Complete Comment Metadata for each commentId in the #commentIds
     * @see #fetchCommentsMetaData(List)
     * @param storyId Unique Identifier of the {@link Story} for which the Comments are to be fetched
     * @return List of {@link CommentDto} top comments fetched from the HackerNews API
     */
    public List<CommentDto> getCommentsForStory(Long storyId) {
        StoryDto story = getStory(storyId);
        List<Long> commentIds = Arrays.asList(story.getCommentIds());
        return fetchCommentsMetaData(commentIds);
    }

    private List<StoryDto> fetchStoryMetaData(Long[] storyIds) {
        return Arrays.stream(storyIds)
                .distinct()
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


    private List<CommentDto> fetchCommentsMetaData(List<Long> commentIds) {
        return commentIds.stream()
                .distinct()
                .map(id ->
                        getCompletableFutureComment(id))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(), completableFutureList ->
                                completableFutureList.stream()
                                        .map(CompletableFuture::join)
                                        .sorted(getCommentDtoComparator())
                                        .limit(LIMIT_ITEM)
                ))
                .collect(Collectors.toList());
    }

    //Async call using CompletableFuture to fetch story based on #storyId
    private CompletableFuture<StoryDto> getCompletableFutureStory(Long storyId) {
        return CompletableFuture.supplyAsync(() ->
                getStory(storyId), executor);
    }
    //Async call using CompletableFuture to fetch comment based on #commentId
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

    //Custom Comparator for Sorting Comments based on childComments length
    private static Comparator<CommentDto> getCommentDtoComparator() {
        return (a, b) -> (b.getChildComments() == null ? 0 : b.getChildComments().length) - (a.getChildComments() == null ? 0 : a.getChildComments().length);
    }

}
