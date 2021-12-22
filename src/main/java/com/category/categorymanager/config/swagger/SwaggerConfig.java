package com.category.categorymanager.config.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${api.key}")
    private String apiKey;

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
            .title("Category Manager")
            .description("")
            .build();
    }

    @Bean
    public Docket commonApi() {

        ParameterBuilder parameterBuilder = new ParameterBuilder();
        parameterBuilder.name("Authorization")
            .description("Access API KEY")
            .defaultValue(apiKey)
            .modelRef(new ModelRef("string"))
            .parameterType("header")
            .required(false)
            .build();

        List<Parameter> parameterList = new ArrayList<>();
        parameterList.add(parameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
            .globalOperationParameters(parameterList)
            .groupName("category-manager")
            .apiInfo(this.apiInfo())
            .select()
            .apis(RequestHandlerSelectors
                .basePackage("com.category.categorymanager.category.controller"))
            .paths(PathSelectors.ant("/**"))
            .build();
    }

}
