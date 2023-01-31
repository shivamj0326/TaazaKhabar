package com.hackernews.taazakhabar;

import com.hackernews.taazakhabar.common.dto.CommentDto;
import com.hackernews.taazakhabar.common.dto.StoryDto;
import com.hackernews.taazakhabar.common.dto.response.CommentResponseDto;
import com.hackernews.taazakhabar.common.dto.response.StoryResponseDto;
import com.hackernews.taazakhabar.dataaccess.StoryRepository;
import com.hackernews.taazakhabar.service.impl.HackerNewsDBService;
import com.hackernews.taazakhabar.service.impl.HackerNewsRestService;
import com.hackernews.taazakhabar.service.impl.HackerNewsService;
import io.restassured.response.Response;
import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit5.HoverflyExtension;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Equator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.http.ProblemDetail;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    @Autowired
    private StoryRepository storyRepository;

    @BeforeEach
    void setup(){
        doNothing().when(dbServiceMock).saveStoriesInDB(Mockito.anyList());
        when(dbServiceMock.findPastStoriesDB()).thenReturn(getMockedTopStories());
        when(restServiceMock.getTopStories()).thenReturn(getMockedTopStories());
        when(restServiceMock.getCommentsForStory(Mockito.anyLong())).thenReturn(getMockedTopComments());
    }

    @Test
    public void testWhenTopStoriesRequestedThenShouldReturnTopStories(){
        List<StoryResponseDto> topStoriesResponse = given()
                                    .port(applicationPort)
                                    .when()
                                    .get("/top-stories")
                                    .jsonPath()
                                    .getList(".", StoryResponseDto.class);

        assertEquals(10, topStoriesResponse.size());
        assertTrue(checkStoriesAreEquals(getMockedTopStories(), topStoriesResponse));
    }

    @Test
    public void testWhenAllCommentsRequestedThenShouldReturnAllComments(){
        List<CommentResponseDto> topComments = given()
                .queryParam("id", 1)
                .port(applicationPort)
                .when()
                .get("/comments")
                .jsonPath()
                .getList(".", CommentResponseDto.class);

        assertEquals(10, topComments.size());
        assertTrue(checkCommentsAreEquals(getMockedTopComments(), topComments));

    }

    @Test
    public void testWhenInvalidStoryIdThenShouldReturnError(){
        Response response = given()
                .queryParam("id", "bla")
                .port(applicationPort)
                .when()
                .get("/comments");

        ProblemDetail problemDetail = response.as(ProblemDetail.class);
        assertEquals(400, problemDetail.getStatus());
        assertEquals("Invalid Id", problemDetail.getTitle());
    }

    @Test
    public void testWhenAllPastStoriesRequestedThenShouldReturnAllPastStories(){
        List<StoryResponseDto> pastStories = given()
                .port(applicationPort)
                .when()
                .get("/past-stories")
                .jsonPath()
                .getList(".", StoryResponseDto.class);

        assertEquals(10, pastStories.size());
        assertTrue(checkStoriesAreEquals(getMockedTopStories(), pastStories));
    }

    private List<StoryDto> getMockedTopStories(){
        List<StoryDto> storyList = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            int randomScore = (int) (Math.random() * 100 + 1);
            storyList.add(StoryDto.builder()
                    .id((long) i)
                    .url("http://example.com")
                    .score(randomScore)
                    .title("example")
                    .score(i)
                    .userHandle("user : " + i)
                    .build());
        }
        return storyList;
    }

    private List<CommentDto> getMockedTopComments(){
        List<CommentDto> commentList = new ArrayList<>();
        Long[] childComments = new Long[(int) (Math.random() * 10 + 1)];
        for(int i = 0 ; i < 10 ; i++){
            commentList.add(CommentDto.builder()
                    .comment("sample comment : " + i)
                    .userHandle("user : " + i)
                    .childComments(childComments)
                    .build());
        }
        return commentList;
    }

    private boolean checkStoriesAreEquals(List<StoryDto> expected, List<StoryResponseDto> actual){
        List<StoryResponseDto> expectedTopStories = expected.stream().sorted((a, b) -> b.getScore() - a.getScore()).map(story -> this.mapper.map(story, StoryResponseDto.class)).collect(Collectors.toList());
        return CollectionUtils.disjunction(expectedTopStories, actual).isEmpty();
    }

    private boolean checkCommentsAreEquals(List<CommentDto> expected, List<CommentResponseDto> actual){
        List<CommentResponseDto> expectedTopComments = expected.stream().sorted(getCommentDtoComparator()).map(story -> this.mapper.map(story, CommentResponseDto.class)).collect(Collectors.toList());
        return CollectionUtils.disjunction(expectedTopComments, actual).isEmpty();
    }

    private static Comparator<CommentDto> getCommentDtoComparator() {
        return (a, b) -> (b.getChildComments() == null ? 0 : b.getChildComments().length) - (a.getChildComments() == null ? 0 : a.getChildComments().length);
    }

}
