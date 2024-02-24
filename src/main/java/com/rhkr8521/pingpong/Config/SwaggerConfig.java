package com.rhkr8521.pingpong.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
    .info(new Info()
    .title("탁구 게임 서비스 API")
    .description("유저를 등록하고 게임 참여, 나가기, 시작, 팀변경 기능을 제공합니다. - Prography 9th rhkr8521")
    .version("1.1.0")
    .contact(new Contact().name("곽태근").email("kwack1357@gmail.com")));
  }
}