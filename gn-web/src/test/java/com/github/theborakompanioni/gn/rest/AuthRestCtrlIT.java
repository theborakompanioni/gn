package com.github.theborakompanioni.gn.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.theborakompanioni.gn.Application;
import com.github.theborakompanioni.gn.ElasticsearchConfguration;
import com.github.theborakompanioni.gn.ShiroConfiguration;
import gn.elastic.repository.UserElasticRepository;
import model.User;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes
        = {Application.class, ElasticsearchConfguration.class, ShiroConfiguration.class})
@WebIntegrationTest("server.port:0")
public class AuthRestCtrlIT extends AbstractJUnit4SpringContextTests {

    private static final Logger log = LoggerFactory.
            getLogger(AuthRestCtrlIT.class);

    private final String ADMIN_USER_EMAIL = "admin@example.com";
    private final String ADMIN_USER_PASSWORD = "admin";

    @Autowired
    private UserElasticRepository userRepo;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private AbstractShiroFilter shiroFilter;

    private MockMvc mockMvc;

    @Before
    public void beforeTest() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .dispatchOptions(true).addFilters(shiroFilter)
                .build();
    }

    @Test
    public void test_mental_sanity() {
        final User admin = userRepo.findByEmail_(ADMIN_USER_EMAIL).get();
        assertThat(admin, notNullValue());
        assertThat(admin.getEmail(), Matchers.equalTo(ADMIN_USER_EMAIL));
    }

    @Test
    public void test_authenticate_success() throws Exception {
        final User admin = userRepo.findByEmail_(ADMIN_USER_EMAIL).get();

        final String json = new ObjectMapper().writeValueAsString(
                new UsernamePasswordToken(admin.getEmail(), ADMIN_USER_PASSWORD));

        mockMvc.perform(post("/auth/login").content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test_authenticate_failure() throws Exception {
        final String json = new ObjectMapper().writeValueAsString(
                new UsernamePasswordToken(ADMIN_USER_EMAIL, "wrong password"));

        mockMvc.perform(post("/auth/login").content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}
