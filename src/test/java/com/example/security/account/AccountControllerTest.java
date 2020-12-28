package com.example.security.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    public void beforeEach() {
        accountRepository.deleteAll();
    }

    @Test
    public void index_anonymous() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("가짜 유저 로그인 한 상태 테스트")
    public void index_user() throws Exception {
        mockMvc.perform(get("/")
                .with(user("kiseok").roles("USER")))  //이미 로그인 한 상태를 가정했기때문에 패스워드는 필요 x
                .andDo(print())
                .andExpect(status().isOk());
        //Hello kiseok
    }

    @Test
    @DisplayName("가짜 유저 user 로그인 한 상태에서 admin페이지 테스트")
    public void admin_user() throws Exception {
        mockMvc.perform(get("/admin")
                .with(user("kiseok").roles("USER")))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    @DisplayName("가짜 유저 admin 로그인 한 상태 admin 테스트")
    public void admin_admin() throws Exception {
        mockMvc.perform(get("/admin")
                .with(user("kiseok").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 성공")
    public void login() throws Exception{
        Account user = createUser();
        mockMvc.perform(formLogin().user(user.getUsername()).password("123456"))
                .andExpect(authenticated());
    }
    @Test
    @DisplayName("로그인실패")
    public void login_fail() throws Exception{
        Account user = createUser();
        mockMvc.perform(formLogin().user(user.getUsername()).password("1234567"))
                .andExpect(unauthenticated());
    }

    private Account createUser() {
        Account account = Account.builder()
                .username("kiseok")
                .password("123456")
                .role("USER")
                .build();
        return accountService.createNew(account);
    }


}