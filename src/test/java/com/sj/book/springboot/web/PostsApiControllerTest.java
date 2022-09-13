package com.sj.book.springboot.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sj.book.springboot.domain.posts.Posts;
import com.sj.book.springboot.domain.posts.PostsRepository;
import com.sj.book.springboot.web.dto.PostsSaveRequestDto;
import com.sj.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
//                .apply(SecurityMockMvcConfigurers.springSecurity())
                .apply(springSecurity())
                .build();

    }

    @AfterEach
    public void tearDown() {
        postsRepository.deleteAll();
    }

    /**
     * webMvcTest의 경우 jpa 기능이 작동하지 않기 때문에
     */
    @Test
    @DisplayName("게시판이 등록된다.")
    @WithMockUser(roles = "USER")
    public void Posts_등록된다() throws Exception {
        //given
        String title = "title";
        String content = "content";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        mvc.perform(MockMvcRequestBuilders.post(url)
                                          .contentType(MediaType.APPLICATION_JSON_UTF8)
                                          .content(new ObjectMapper().writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk());

        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();

        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

    }

    @Test
    @DisplayName("게시판이 수정된다.")
    @WithMockUser(roles = "USER")
    public void Posts_수정된다() throws Exception {
        //given saved data
        Posts savedPosts = postsRepository.save(Posts.builder()
                                          .title("title")
                                          .content("content")
                                          .author("author")
                                          .build());
        //saved entityId
        Long updatedId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                                                                .title(expectedTitle)
                                                                .content(expectedContent)
                                                                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updatedId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        //exchange 사용이유 put은 return 이 void여서 test check 불가능
        //exchange를 통해 responseEntity return 받아서 test check
//        ResponseEntity<Long> responseEntity = restTemplate
//                                              .exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        mvc.perform(MockMvcRequestBuilders.put(url)
                                          .contentType(MediaType.APPLICATION_JSON_UTF8)
                                          .content(new ObjectMapper().writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk());

        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }
}