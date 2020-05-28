package me.len.swagger.extension;

import me.len.swagger.extension.annotation.ApiJsonObject;
import me.len.swagger.extension.annotation.ApiJsonObjects;
import me.len.swagger.extension.annotation.ApiJsonProperty;
import me.len.swagger.extension.handler.ByteBuddyClassCreator;
import me.len.swagger.extension.plugin.ApiJsonOperationModelsProviderPlugin;
import me.len.swagger.extension.plugin.ParameterPlugin;
import me.len.swagger.extension.plugin.ResponsePlugin;
import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ResponseHeader;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

@SpringBootApplication
@EnableSwagger2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/test")
public class ExtensionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExtensionApplication.class, args);
    }

    //    @GetMapping("/parameter")
    public void testParameter(
      @RequestBody
      @ApiJsonObject(name = "Parameter", properties =
        {
          @ApiJsonProperty(name = "username"),
          @ApiJsonProperty(name = "name", clazz = TestBean.class, container = "List"),
        })
        Map<String, Object> stringObjectMap) {

    }

    @ApiJsonObject(name = "ReturnType", properties =
      {
        @ApiJsonProperty(name = "username"),
        @ApiJsonProperty(name = "age", clazz = Integer.class),
        @ApiJsonProperty(name = "name", clazz = TestBean.class)
      })
//    @GetMapping("/return-type")
    public Map<String, Object> returnType() {

        return Collections.EMPTY_MAP;
    }


    //    @GetMapping("entity")
    public ResponseEntity<TestBean> entity() {
        val testBean = new TestBean();
        testBean.setUsername("username");
        return ResponseEntity.ok(testBean);
    }


    //    @GetMapping("/parameter-return-type")
    public Map<String, Object> parameterReturnType(@RequestBody Map<String, Object> stringObjectMap) {
        return Collections.EMPTY_MAP;
    }


    @ApiJsonObjects(
      {
        @ApiJsonObject(name = "Objects1", properties = {@ApiJsonProperty(name = "username1")}, code = 400,
          responseHeaders = {@ResponseHeader(name = "hhh", response = String.class)}, message = "Bad Request"),
        @ApiJsonObject(name = "Objects2", properties = {@ApiJsonProperty(name = "username2")}, code = 401)}
    )
    @ApiJsonObject(name = "Objects0", properties =
      {
        @ApiJsonProperty(name = "username", value = "用户名"),
        @ApiJsonProperty(name = "name", clazz = TestBean.class)
      })
//    @GetMapping("/api-json-objects")
    public Map<String, Object> apiJsonObjects() {
        return Collections.EMPTY_MAP;
    }


    //    @GetMapping("base-response")
    public BaseResponse<TestBean> baseResponse() {
        val testBeanBaseResponse = new BaseResponse<TestBean>();
        val data = new TestBean();
        data.setUsername("username");
        testBeanBaseResponse.setData(data);
        return testBeanBaseResponse;
    }

    @ApiJsonObject(name = "BaseResponseList", container = "List",
      properties = {
        @ApiJsonProperty(name = "username"),
        @ApiJsonProperty(name = "id", clazz = Long.class),
      })
    @GetMapping("base-response-list")
    public BaseResponse<TestBean> baseResponseList() {
        val testBeanBaseResponse = new BaseResponse<TestBean>();
        val data = new TestBean();
        data.setUsername("username");
        testBeanBaseResponse.setData(data);
        return testBeanBaseResponse;
    }


    @GetMapping("base-response-list2")
    public List<TestBean> baseResponseList2() {
        val testBeanBaseResponse = new BaseResponse<TestBean>();
        val data = new TestBean();
        data.setUsername("username");
        testBeanBaseResponse.setData(data);
        return Collections.singletonList(data);
    }

    @PostMapping("base-response-list3")
    public void baseResponseList3(
      @RequestBody
      @ApiJsonObject(
        name = "BaseResponseList3", container = "List",
        properties = {@ApiJsonProperty(name = "name", clazz = Long.class)}
      ) List<Map<String, Object>> d) {
        val testBeanBaseResponse = new BaseResponse<TestBean>();
        val data = new TestBean();
        data.setUsername("username");
        testBeanBaseResponse.setData(data);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
          .title("swagger-extension")
          .contact(new Contact("len.cai", "https://blog.csdn.net/csdn6497", "len.cai@yunlsp.com"))
          .version("1.0")
          .description("swagger-extension")
          .build();
    }

    @Bean
    public Docket docket(TypeResolver typeResolver) {

        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage("cn.len.swagger.extension"))
          .paths(PathSelectors.any())
          .build()
          .useDefaultResponseMessages(false)
          .alternateTypeRules(newRule(typeResolver.resolve(BaseResponse.class, WildcardType.class),
            typeResolver.resolve(WildcardType.class)))
          .apiInfo(apiInfo());
    }


    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
    @Bean
    public ResponsePlugin responsePlugin(TypeNameExtractor typeNameExtractor) {
        return new ResponsePlugin(typeNameExtractor);
    }

    @Bean
    public ApiJsonOperationModelsProviderPlugin apiJsonOperationModelsProviderPlugin(TypeResolver typeResolver) {
        return new ApiJsonOperationModelsProviderPlugin(typeResolver, new ByteBuddyClassCreator());
    }

    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
    @Bean
    public ParameterPlugin parameterPlugin(TypeNameExtractor typeNameExtractor, TypeResolver typeResolver) {
        return new ParameterPlugin(typeNameExtractor, typeResolver);
    }

    @Getter
    @Setter
    @ToString
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class TestBean {
        @ApiModelProperty(value = "用户名", required = true)
        String username;

        List<String> stringList;
    }

}
