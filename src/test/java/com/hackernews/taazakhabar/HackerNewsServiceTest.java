package com.hackernews.taazakhabar;

import com.hackernews.taazakhabar.common.dto.CommentDto;
import com.hackernews.taazakhabar.common.dto.StoryDto;
import com.hackernews.taazakhabar.common.dto.response.CommentResponseDto;
import com.hackernews.taazakhabar.common.dto.response.StoryResponseDto;
import com.hackernews.taazakhabar.service.impl.HackerNewsDBService;
import com.hackernews.taazakhabar.service.impl.HackerNewsRestService;
import com.hackernews.taazakhabar.service.impl.HackerNewsService;
import io.restassured.response.Response;
import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit5.HoverflyExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HackerNewsServiceTest {

    @LocalServerPort
    private int applicationPort;

    @MockBean
    private HackerNewsRestService restServiceMock;

    @MockBean
    private HackerNewsDBService dbServiceMock;

    private ModelMapper mapper = new ModelMapper();

    private static List<StoryDto> getMockedTopStories(){
        List<StoryDto> storyList = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            storyList.add(StoryDto.builder()
                    .id((long) i)
                    .url("http://example.com")
                    .score(i)
                    .title("example")
                    .score(10)
                    .userHandle("user : " + i)
                    .submissionTime(LocalDateTime.now())
                    .build());
        }
        return storyList;
    }

    private static List<CommentDto> getMockedTopComments(){
        List<CommentDto> commentList = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            commentList.add(CommentDto.builder()
                    .comment("sample comment : " + i)
                    .userHandle("user : " + i)
                    .build());
        }
        return commentList;
    }

   void setup(){
        doNothing().when(this.dbServiceMock).saveStoriesInDB(Mockito.anyList());
        when(dbServiceMock.findPastStoriesDB()).thenReturn(getMockedTopStories());
        when(restServiceMock.getTopStories()).thenReturn(getMockedTopStories());
        when(restServiceMock.getCommentsForStory(Mockito.anyLong())).thenReturn(getMockedTopComments());
    }

    @Test
    public void testWhenTopStoriesRequestedThenShouldReturnTopStories(){
        setup();
        List<StoryResponseDto> topStoriesResponse = given()
                                    .port(applicationPort)
                                    .when()
                                    .get("/top-stories")
                                    .jsonPath()
                                    .getList(".", StoryResponseDto.class);

        assertEquals(10, topStoriesResponse.size());
    }

    @Test
    public void testWhenAllCommentsRequestedThenShouldReturnAllComments(){
        setup();
        List<CommentResponseDto> topComments = given()
                .queryParam("id", 1)
                .port(applicationPort)
                .when()
                .get("/comments")
                .jsonPath()
                .getList(".", CommentResponseDto.class);

        assertEquals(10, topComments.size());

    }

    @Test
    public void testWhenAllPastStoriesRequestedThenShouldReturnAllPastStories(){
        setup();
        List<StoryResponseDto> pastStories = given()
                .port(applicationPort)
                .when()
                .get("/past-stories")
                .jsonPath()
                .getList(".", StoryResponseDto.class);

        assertEquals(10, pastStories.size());
    }

}
