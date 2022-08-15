package com.sj.book.springboot.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class) // Junit에 내장된 실행자 외에 다른 실행자로 실행시킨다. ::SpringRunner
@WebMvcTest(controllers = HelloController.class) //Web(Spring MVC)에 집중할 수 있는 어노테이션
    //선언할 경우 @controller, @controllerAdvice 사용가능
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("hello 문자열이 리턴된다.")
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    /**
     * jsonPath jSON응답값을 필드별로 검증할 수 있는 메소드입니다.
     * $를 기준으로 필드염을 명시합니다.
     */
    @Test
    @DisplayName("helloDto가 리턴된다")
    public void helloDto가_리턴된다() throws Exception {

        String name = "hello";
        int amount = 1000;

        mvc.perform(
                    get("/hello/dto")
                            .param("name", name)
                            .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }

}