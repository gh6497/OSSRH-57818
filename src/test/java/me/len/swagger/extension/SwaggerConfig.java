package me.len.swagger.extension;
/*
 * Created on 2020-06-03.
 */

/**
 * @author Len
 */
import com.fasterxml.classmate.TypeResolver;
import me.len.swagger.extension.handler.ByteBuddyClassCreator;
import me.len.swagger.extension.handler.ClassCreator;
import me.len.swagger.extension.plugin.ApiJsonOperationModelsProviderPlugin;
import me.len.swagger.extension.plugin.ParameterPlugin;
import me.len.swagger.extension.plugin.ResponsePlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration(proxyBeanMethods = false)
@EnableSwagger2
public class SwaggerConfig {
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
    public ParameterPlugin parameterPlugin(TypeNameExtractor typeNameExtractor,TypeResolver typeResolver) {
        return new ParameterPlugin(typeNameExtractor,typeResolver);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
          .title("swagger-extension")
          .contact(new Contact("len", "https://blog.csdn.net/csdn6497", "lencai@live.cn"))
          .version("1.0")
          .description("swagger-extension-usage")
          .build();
    }

    @Bean
    public Docket docket(TypeResolver typeResolver) {

        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage("me.len.swagger.extension"))
          .paths(PathSelectors.any())
          .build()
          .useDefaultResponseMessages(false)
          .apiInfo(apiInfo());
    }
}
