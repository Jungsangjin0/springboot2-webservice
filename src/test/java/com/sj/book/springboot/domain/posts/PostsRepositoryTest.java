package com.sj.book.springboot.domain.posts;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * save,findAll 테스트
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글저장 불로오기")
    public void 게시글저장_불러오기() {
        //given 데이터가 주어지고, 저장이 됨
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("loky13@daum.net")
                .build());

        //when 리스트를 가져왔을 때
        List<Posts> postsList = postsRepository.findAll();

        //then 저장 전 썼던 값과 db에서 읽어온 값이 일치하는가.
        Posts posts = postsList.get(0);
        Assertions.assertThat(posts.getTitle()).isEqualTo(title);
        Assertions.assertThat(posts.getContent()).isEqualTo(content);


    }

}