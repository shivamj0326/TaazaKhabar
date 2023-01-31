package com.hackernews.taazakhabar.controller;

import com.hackernews.taazakhabar.common.dto.response.CommentResponseDto;
import com.hackernews.taazakhabar.common.dto.response.StoryResponseDto;
import com.hackernews.taazakhabar.service.api.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("")
@RestController
@Slf4j
/** Provides REST Endpoints to the client to communicate with the TaazaKhabarApp **/
public class NewsController {

    private final NewsService service;
    NewsController(NewsService service){
        this.service = service;
    }

    @GetMapping(value = "/top-stories", produces = "application/json")
    public ResponseEntity<List<StoryResponseDto>> getTopStories(){
        log.debug("Received Request to fetch Top Stories");
        return new ResponseEntity<List<StoryResponseDto>>(service.getTopStories(), HttpStatus.OK);
    }

    @GetMapping(value = "/past-stories", produces = "application/json")
    public ResponseEntity<List<StoryResponseDto>> getPastStories(){
        log.debug("Received Request to fetch Past Stories");
        return new ResponseEntity<List<StoryResponseDto>>(service.getPastStories(), HttpStatus.OK);
    }

    @GetMapping(value = "/comments", produces = "application/json")
    public ResponseEntity<List<CommentResponseDto>> getCommentsForStory(@Valid @RequestParam("id") long storyId){
        log.debug("Received Request to fetch Comments for Story {}", storyId);
        return new ResponseEntity<List<CommentResponseDto>>(service.getCommentsForStory(storyId), HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ProblemDetail handleItemNotFoundException(MethodArgumentTypeMismatchException ex, WebRequest request){
        ProblemDetail body = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), ex.getLocalizedMessage());
        body.setTitle("Invalid Id");
        return body;
    }

}
