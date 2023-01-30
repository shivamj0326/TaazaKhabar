package com.hackernews.taazakhabar.controller;

import com.hackernews.taazakhabar.common.dto.response.CommentResponseDto;
import com.hackernews.taazakhabar.common.dto.response.StoryResponseDto;
import com.hackernews.taazakhabar.service.api.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("")
@RestController
public class NewsController {

    private final NewsService service;
    NewsController(NewsService service){
        this.service = service;
    }

    @GetMapping(value = "/top-stories", produces = "application/json")
    public ResponseEntity<List<StoryResponseDto>> getTopStories(){
        return new ResponseEntity<List<StoryResponseDto>>(service.getTopStories(), HttpStatus.OK);
    }

    @GetMapping(value = "/past-stories", produces = "application/json")
    public ResponseEntity<List<StoryResponseDto>> getPastStories(){
        return new ResponseEntity<List<StoryResponseDto>>(service.getPastStories(), HttpStatus.OK);
    }

    @GetMapping(value = "/comments", produces = "application/json")
    public ResponseEntity<List<CommentResponseDto>> getCommentsForStory(@RequestParam("id") long storyId){
        return new ResponseEntity<List<CommentResponseDto>>(service.getCommentsForStory(storyId), HttpStatus.OK);
    }

}
