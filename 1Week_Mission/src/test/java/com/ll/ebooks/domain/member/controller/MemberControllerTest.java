package com.ll.ebooks.domain.member.controller;

import com.ll.ebooks.domain.member.dto.request.JoinRequestDto;
import com.ll.ebooks.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    @DisplayName("회원가입페이지_나타난다")
    void test1() throws Exception {
        //when
        ResultActions resultActions = mockMvc
                .perform(get("/member/join"))
                .andDo(print());
        //then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("JoinForm"));
    }

    @Test
    @DisplayName("회원_가입된다")
    void test2() throws Exception {

        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/join")
                        .with(csrf())
                        .param("username", "dnjsml30")
                        .param("password", "1234")
                        .param("email", "dnjsml30@naver.com")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("Join"));


    }

    @Test
    @DisplayName("로그인_성공한다")
    void test3() throws Exception {

        //given
        memberService.join(JoinRequestDto.builder()
                .username("dnjsml30")
                .password("test123!")
                .email("dnjsml30@naver.com")
                .nickname("상원")
                .build());

        //when
        mockMvc.perform(post("/member/login")
                .param("username", "dnjsml30")
                .param("password", "test123!")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

    }
}
