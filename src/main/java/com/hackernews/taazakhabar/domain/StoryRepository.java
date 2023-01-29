package com.hackernews.taazakhabar.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface StoryRepository extends CrudRepository<Story, Long> {

    @Query("SELECT s FROM Story s WHERE s.createdAt <= :time")
    Set<Story> findPastStories(@Param("time")LocalDateTime time);

}
