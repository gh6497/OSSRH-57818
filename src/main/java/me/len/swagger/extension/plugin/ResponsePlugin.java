package me.len.swagger.extension.plugin;

import me.len.swagger.extension.annotation.ApiJsonObject;
import me.len.swagger.extension.util.ApiJsonUtil;
import com.fasterxml.classmate.ResolvedType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.readers.operation.ResponseHeaders;

import java.util.Collections;
import java.util.HashSet;

/**
 * @author Len
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ResponsePlugin implements OperationBuilderPlugin {
    TypeNameExtractor nameExtractor;

    @Override
    public void apply(OperationContext context) {
        val apiJsonObjects = ApiJsonUtil.merge(context);
        if (apiJsonObjects != null) {
            val responseMessages = new HashSet<ResponseMessage>(apiJsonObjects.size());
            for (ApiJsonObject apiJsonObject : apiJsonObjects) {
                val className = ApiJsonUtil.getQualifiedName(apiJsonObject);
                ResolvedType resolvedType = ApiJsonOperationModelsProviderPlugin.RESOLVED_TYPE_MAP.get(className);

                ModelContext modelContext = ModelContext.returnValue(
                  context.getGroupName(),
                  resolvedType,
                  context.getDocumentationType(),
                  context.getAlternateTypeProvider(),
                  context.getGenericsNamingStrategy(),
                  context.getIgnorableParameterTypes());
                String responseTypeName = nameExtractor.typeName(modelContext);
                ModelRef modelRef = new ModelRef(responseTypeName);
                if (apiJsonObject.container().equals("List")) {
                    modelRef = new ModelRef("List", modelRef);
                }
                val responseHeaders = apiJsonObject.responseHeaders();
                val headers = ResponseHeaders.headers(responseHeaders);

                val responseMessage = new ResponseMessage(apiJsonObject.code(), apiJsonObject.message(), modelRef, headers, Collections.emptyList());
                responseMessages.add(responseMessage);
            }
            context.operationBuilder().responseMessages(responseMessages);
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }


}
