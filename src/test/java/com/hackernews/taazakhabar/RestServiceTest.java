package com.hackernews.taazakhabar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackernews.taazakhabar.common.dto.CommentDto;
import com.hackernews.taazakhabar.common.dto.StoryDto;
import com.hackernews.taazakhabar.service.impl.HackerNewsDBService;
import com.hackernews.taazakhabar.service.impl.HackerNewsRestService;
import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit.core.SimulationSource;
import io.specto.hoverfly.junit5.HoverflyExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@ExtendWith(HoverflyExtension.class)
@TestPropertySource(properties = { "taazakhabar.client.hackernews.item.limit=1", "taazakhabar.client.hackernews.baseuri=http://hacker-news.firebaseio.com/v0"})
public class RestServiceTest {

    @MockBean
    private HackerNewsDBService dbServiceMock;

    @Autowired
    private HackerNewsRestService restService;

    @BeforeEach
    public void setup(){
        doNothing().when(dbServiceMock).saveStoriesInDB(Mockito.anyList());
    }

    private static final String DEFAULT_PATH = "src//test//resources//hoverfly//";

    private StoryDto getTopStory(){
        ObjectMapper mapper = new ObjectMapper();
        StoryDto story = new StoryDto();
        try {
            story = mapper.readValue(new File(DEFAULT_PATH + "TopStory.json"), StoryDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return story;
    }

    private CommentDto getTopComment(){
        ObjectMapper mapper = new ObjectMapper();
        CommentDto comment = new CommentDto();
        try {
            comment = mapper.readValue(new File(DEFAULT_PATH + "TopComment.json"), CommentDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comment;
    }

    @Test
    public void testWhenTopStoriesApiCalledShouldReturnTopStories(Hoverfly hoverfly){
        hoverfly.simulate(SimulationSource.defaultPath("topstory-simulation.json"), SimulationSource.defaultPath("item-simulation.json"));
        List<StoryDto> actualStories = restService.getTopStories();
        assertEquals(1, actualStories.size());
        assertEquals(getTopStory(), actualStories.get(0));
    }

    @Test
    public void testWhenCommentsApiCalledShouldReturnTopComments(Hoverfly hoverfly){
        hoverfly.simulate(SimulationSource.defaultPath("topstory-simulation.json"), SimulationSource.defaultPath("item-simulation.json"));
        List<CommentDto> actualComments = restService.getCommentsForStory(1l);
        assertEquals(1, actualComments.size());
        assertEquals(getTopComment(), actualComments.get(0));
    }


}
