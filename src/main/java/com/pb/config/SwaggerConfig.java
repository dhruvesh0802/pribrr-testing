package com.pb.config;


import com.pb.utils.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SwaggerConfig file to use swagger for testing api
 * Link: http://localhost:9000/swagger-ui.html#/
 */

@EnableSwagger2
@Configuration
public class SwaggerConfig {

        @Bean
        public Docket PBApi() {
        ParameterBuilder aParameterBuilders = new ParameterBuilder();
        List<Parameter> aParameters = new ArrayList<>();

        aParameterBuilders.name(Constant.ACCESS_TOKEN).modelRef(new ModelRef("string"))
                .description("Description - this is for AccessToken")
                .parameterType("header")
                .defaultValue("")
                .allowEmptyValue(true)
                .required(false)
                .build();
        aParameters.add(aParameterBuilders.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.pb"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .securitySchemes(Collections.singletonList(apiKey()))
                .globalOperationParameters(aParameters);
    }

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title("PB API")
                .version("1.0.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey(Constant.API_KEY, Constant.ACCESS_TOKEN, "header");
    }

}