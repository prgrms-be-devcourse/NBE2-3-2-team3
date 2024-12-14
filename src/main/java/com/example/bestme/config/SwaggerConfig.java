package com.example.bestme.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI OpenAPI() {
        Info info = new Info()
                .title("Team3")
                .description("프로그래머스 데브코스 프로젝트2 Team3의 API 문서입니다.")
                .version("1.0.0");


        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(info);
    }
}