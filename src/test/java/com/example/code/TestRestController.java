package com.example.code;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {

  @RequestMapping(path = "/hello", method = {RequestMethod.GET})
  public String greeting() {
    return "Hello World";
  }

}
