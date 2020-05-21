package com.github.length.swagger.extension.plugin;

import com.github.length.swagger.extension.annotation.ApiJsonObject;
import com.github.length.swagger.extension.handler.ClassCreator;
import com.github.length.swagger.extension.util.ApiJsonUtil;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Len
 */
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ApiJsonOperationModelsProviderPlugin implements OperationModelsProviderPlugin {
    TypeResolver typeResolver;
    public static final Map<String, ResolvedType> RESOLVED_TYPE_MAP = new HashMap<>();
    ClassCreator classCreator;


    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    @Override
    public void apply(RequestMappingContext context) {
        val apiJsonObjects = ApiJsonUtil.merge(context);
        if (apiJsonObjects != null) {
            for (ApiJsonObject apiJsonObject : apiJsonObjects) {
                val className = ApiJsonUtil.getQualifiedName(apiJsonObject);
                final ResolvedType resolvedType = getResolvedType(apiJsonObject);
                RESOLVED_TYPE_MAP.put(className, resolvedType);
                context.operationModelsBuilder().addReturn(resolvedType);
            }
        }

        List<ResolvedMethodParameter> parameterTypes = context.getParameters();
        for (ResolvedMethodParameter parameterType : parameterTypes) {
            if (parameterType.hasParameterAnnotation(RequestBody.class)) {
                @SuppressWarnings("all")
                val parameterApiJsonObject = parameterType.findAnnotation(ApiJsonObject.class);
                if (parameterApiJsonObject.isPresent()) {
                    val apiJsonObject = parameterApiJsonObject.get();
                    val className = ApiJsonUtil.getQualifiedName(apiJsonObject);
                    final ResolvedType resolvedType = getResolvedType(apiJsonObject);
                    RESOLVED_TYPE_MAP.put(className, resolvedType);

                    context.operationModelsBuilder().addInputParam(resolvedType);
                }
            }
        }
    }

    private ResolvedType getResolvedType(ApiJsonObject apiJsonObject) {

        return typeResolver.resolve(classCreator.create(apiJsonObject));
    }

}
