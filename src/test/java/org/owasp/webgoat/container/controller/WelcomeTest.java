package org.owasp.webgoat.container.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

class WelcomeTest {

  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    mockMvc = standaloneSetup(new Welcome()).build();
  }

  @Test
  void firstVisitShowsWelcomePage() throws Exception {
    mockMvc
        .perform(get("/welcome.mvc"))
        .andExpect(status().isOk())
        .andExpect(view().name("welcome"));
  }

  @Test
  void subsequentVisitForwardsToAttack() throws Exception {
    MockHttpSession session = new MockHttpSession();
    session.setAttribute("welcomed", "true");
    mockMvc
        .perform(get("/welcome.mvc").session(session))
        .andExpect(status().isOk())
        .andExpect(view().name("forward:/attack?start=true"));
  }

  @Test
  void postProceedSetsSessionAndRedirects() throws Exception {
    MockHttpSession session = new MockHttpSession();
    mockMvc
        .perform(post("/welcome.mvc").session(session))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/attack?start=true"));
  }
}
