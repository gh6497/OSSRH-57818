package me.len.swagger.extension.util;

import me.len.swagger.extension.annotation.ApiJsonObject;
import me.len.swagger.extension.annotation.ApiJsonObjects;
import com.google.common.base.Optional;
import lombok.experimental.UtilityClass;
import lombok.val;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Len
 */
@UtilityClass

public class ApiJsonUtil {

    public String getQualifiedName(ApiJsonObject apiJsonObject) {
        return apiJsonObject.packageName() + "." + apiJsonObject.name();

    }

    @SuppressWarnings("all")
    public List<ApiJsonObject> merge(RequestMappingContext context) {


        val apiJsonObjectOptional = context.findAnnotation(ApiJsonObject.class);
        val apiJsonObjectsOptional = context.findAnnotation(ApiJsonObjects.class);

        return doMerge(apiJsonObjectOptional, apiJsonObjectsOptional);
    }

    @SuppressWarnings("all")
    private List<ApiJsonObject> doMerge(Optional<ApiJsonObject> apiJsonObjectOptional, Optional<ApiJsonObjects> apiJsonObjectsOptional) {
        if (!apiJsonObjectOptional.isPresent() && !apiJsonObjectsOptional.isPresent()) {
            return Collections.EMPTY_LIST;
        }
        val apiJsonObjects = new ArrayList<ApiJsonObject>();
        if (apiJsonObjectOptional.isPresent()) {
            apiJsonObjects.add(apiJsonObjectOptional.get());
        }

        if (apiJsonObjectsOptional.isPresent()) {
            ApiJsonObject[] value = apiJsonObjectsOptional.get().value();
            apiJsonObjects.addAll(Arrays.asList(value));
        }
        return apiJsonObjects;
    }

    @SuppressWarnings("all")
    public List<ApiJsonObject> merge(OperationContext context) {


        val apiJsonObjectOptional = context.findAnnotation(ApiJsonObject.class);
        val apiJsonObjectsOptional = context.findAnnotation(ApiJsonObjects.class);
        return doMerge(apiJsonObjectOptional, apiJsonObjectsOptional);
    }

}
