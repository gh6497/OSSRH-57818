package me.len.swagger.extension.handler;

import me.len.swagger.extension.annotation.ApiJsonObject;
import me.len.swagger.extension.annotation.ApiJsonProperty;
import me.len.swagger.extension.util.ApiJsonUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;

import java.util.Collections;
import java.util.List;

/**
 * @author Len
 * 2020-02-19
 */
public class ByteBuddyClassCreator implements ClassCreator {

    @RequiredArgsConstructor
    public static class SimpleNamingStrategy implements NamingStrategy {
        private final String name;

        @Override
        public String subclass(TypeDescription.Generic generic) {
            return name;
        }

        @Override
        public String redefine(TypeDescription typeDescription) {
            return null;
        }

        @Override
        public String rebase(TypeDescription typeDescription) {
            return null;
        }
    }

    @Override
    public Class<?> create(ApiJsonObject apiJsonObject) {

        return doCreate(apiJsonObject);
    }


    private Class<?> doCreate(ApiJsonObject apiJsonObject) {
        val qualifiedName = ApiJsonUtil.getQualifiedName(apiJsonObject);
        val properties = apiJsonObject.properties();
        DynamicType.Builder<?> subclass = new ByteBuddy()
          .with(new SimpleNamingStrategy(qualifiedName))
          .subclass(Object.class)
          .annotateType(getApiModel(apiJsonObject));
        for (ApiJsonProperty property : properties) {
            val container = property.container();
            if (container.equals("List")) {
                val generic = TypeDescription.Generic.Builder.parameterizedType(List.class, property.clazz()).build();
                subclass = subclass.defineField(property.name(), generic, Visibility.PUBLIC)
                  .annotateField(Collections.singletonList(getApiModelProperty(property)));
            } else {
                subclass = subclass.defineField(property.name(), property.clazz(), Visibility.PUBLIC)
                  .annotateField(Collections.singletonList(getApiModelProperty(property)));
            }
        }
        return subclass.make()
          .load(getClass().getClassLoader()).getLoaded();

    }

    private ApiModelProperty getApiModelProperty(ApiJsonProperty apiJsonProperty) {
        val apiModelProperty = new ApiModelPropertyImpl();
        apiModelProperty.setValue(apiJsonProperty.value());
        apiModelProperty.setName(apiJsonProperty.name());
        apiModelProperty.setAllowableValues(apiJsonProperty.allowableValues());
        apiModelProperty.setNotes(apiJsonProperty.notes());
        apiModelProperty.setRequired(apiJsonProperty.required());
        apiModelProperty.setPosition(apiJsonProperty.position());
        apiModelProperty.setHidden(apiJsonProperty.hidden());
        apiModelProperty.setExample(apiJsonProperty.example());
        apiModelProperty.setAllowEmptyValue(apiJsonProperty.allowEmptyValue());
        return apiModelProperty;

    }


    private ApiModel getApiModel(ApiJsonObject apiJsonObject) {
        val description = apiJsonObject.description();
        val value = apiJsonObject.value();
        val apiModel = new ApiModelImpl();
        apiModel.setValue(value);
        apiModel.setDescription(description);
        return apiModel;
    }

}
