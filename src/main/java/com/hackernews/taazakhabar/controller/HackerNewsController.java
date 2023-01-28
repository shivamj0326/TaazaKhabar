package com.hackernews.taazakhabar.controller;

import com.hackernews.taazakhabar.common.dto.CommentDto;
import com.hackernews.taazakhabar.common.dto.CommentResponseDto;
import com.hackernews.taazakhabar.common.dto.StoryDto;
import com.hackernews.taazakhabar.common.dto.StoryResponseDto;
import com.hackernews.taazakhabar.service.HackerNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("")
@RestController
public class HackerNewsController {

    @Autowired
    private HackerNewsService service;

    @GetMapping("/top-stories")
    public ResponseEntity<List<StoryResponseDto>> getTopStories(){
        return new ResponseEntity<List<StoryResponseDto>>(service.getStories(), HttpStatus.OK);
    }

    @GetMapping("/past-stories")
    public ResponseEntity<List<StoryResponseDto>> getPastStories(){
        return new ResponseEntity<List<StoryResponseDto>>(service.getPastStories(), HttpStatus.OK);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(@RequestParam("id") long storyId){
        return new ResponseEntity<List<CommentResponseDto>>(service.getComment(storyId), HttpStatus.OK);
    }

}
