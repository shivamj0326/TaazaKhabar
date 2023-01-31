package com.hackernews.taazakhabar;

import com.hackernews.taazakhabar.common.dto.StoryDto;
import com.hackernews.taazakhabar.dataaccess.Story;
import com.hackernews.taazakhabar.dataaccess.StoryRepository;
import com.hackernews.taazakhabar.service.impl.HackerNewsDBService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DBServiceTest {

    @Autowired
    private HackerNewsDBService dbService;

    @Autowired
    private StoryRepository storyRepository;

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

    private static StoryDto getMockedStory(){
        return StoryDto.builder()
                .id(1l)
                .url("http://example.com")
                .score(10)
                .title("example")
                .score(10)
                .submissionTime(LocalDateTime.now())
                .userHandle("user")
                .build();
    }

    @Test
    public void testSaveStoriesInDB(){
        List<StoryDto> storiesToBeSaved = getMockedTopStories();
        dbService.saveStoriesInDB(storiesToBeSaved);
        assertEquals(storiesToBeSaved.size(), storyRepository.count(), "will fail stories not saved in DB");
    }

    @Test
    public void testWhenPastStoriesNotAvailableInDb(){
        List<StoryDto> storiesToBeSaved = getMockedTopStories();
        storiesToBeSaved.stream().forEach(story -> storyRepository.save(this.mapper.map(story, Story.class)));
        List<StoryDto> storiesFound = dbService.findPastStoriesDB();
        assertEquals(0l, storiesFound.size(), "will fail if stories found does not match the expected count");
    }

}
