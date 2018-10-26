package com.example.code;


import static com.example.code.PostmanSnippet.postmanImport;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebMvcTest(TestRestController.class)
public class TestGenerateCollection implements ApplicationContextAware {

  @Rule
  public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("hello-world");

  @Autowired
  private MockMvc mockMvc;

  private WebApplicationContext webApplicationContext;

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.webApplicationContext = (WebApplicationContext) applicationContext;
  }

  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(this.restDocumentation))
        .build();
  }

  @Test
  public void test() throws Exception {
    mockMvc.perform(get("/hello"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello World"))
        .andDo(postmanImport("Hello World"))
        .andReturn();
  }

}
