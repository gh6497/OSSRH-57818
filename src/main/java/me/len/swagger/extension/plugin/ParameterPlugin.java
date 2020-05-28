package me.len.swagger.extension.plugin;


import me.len.swagger.extension.annotation.ApiJsonObject;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.util.List;

import static springfox.documentation.schema.ResolvedTypes.modelRefFactory;
import static springfox.documentation.spi.schema.contexts.ModelContext.inputParam;

/**
 * @author Len
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ParameterPlugin implements ParameterBuilderPlugin {
    TypeNameExtractor nameExtractor;
    TypeResolver typeResolver;

    @Override
    public void apply(ParameterContext context) {
        val resolvedMethodParameter = context.resolvedMethodParameter();
        val parameterAnnotation = resolvedMethodParameter.hasParameterAnnotation(RequestBody.class);
        if (parameterAnnotation) {
            val annotation = resolvedMethodParameter.findAnnotation(ApiJsonObject.class);
            if (annotation.isPresent()) {
                val apiJsonObject = annotation.get();
                val className = apiJsonObject.packageName() + "." + apiJsonObject.name();
                ResolvedType parameterType = ApiJsonOperationModelsProviderPlugin.RESOLVED_TYPE_MAP.get(className);
                val parameterBuilder = context.parameterBuilder();
                parameterBuilder.parameterType("body");
                if (apiJsonObject.container().equals("List")) {
                    parameterType = typeResolver.resolve(List.class, parameterType);
                }
                ModelContext modelContext = inputParam(
                  context.getGroupName(),
                  parameterType,
                  context.getDocumentationType(),
                  context.getAlternateTypeProvider(),
                  context.getGenericNamingStrategy(),
                  context.getIgnorableParameterTypes());
                context.parameterBuilder()
                  .type(parameterType)
                  .modelRef(modelRefFactory(modelContext, nameExtractor).apply(parameterType));
            }
        }


    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
