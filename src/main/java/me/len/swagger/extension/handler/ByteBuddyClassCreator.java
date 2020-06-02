package me.len.swagger.extension.handler;

import io.swagger.annotations.Extension;
import lombok.*;
import lombok.experimental.FieldDefaults;
import me.len.swagger.extension.annotation.ApiJsonObject;
import me.len.swagger.extension.annotation.ApiJsonProperty;
import me.len.swagger.extension.util.ApiJsonUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;

import java.lang.annotation.Annotation;
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

    @Setter
    @ToString
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @SuppressWarnings("all")
    private static class ApiModelImpl implements ApiModel {
        String value = "";

        String description = "";

        Class<?> parent = Void.class;

        String discriminator = "";

        String reference = "";

        Class<?>[] subTypes = new Class<?>[0];

        @Override
        public String value() {
            return value;
        }

        @Override
        public String description() {
            return description;
        }

        @Override
        public Class<?> parent() {
            return parent;
        }

        @Override
        public String discriminator() {
            return discriminator;
        }

        @Override
        public Class<?>[] subTypes() {
            return subTypes;
        }

        @Override
        public String reference() {
            return reference;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return ApiModel.class;
        }
    }
    @Setter
    @ToString
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @SuppressWarnings("all")
    private static class ApiModelPropertyImpl implements ApiModelProperty {

        String value = "";

        String name = "";

        String allowableValues = "";

        String access = "";

        String notes = "";

        String dataType = "";

        boolean required;

        int position;

        boolean hidden;

        String example = "";

        boolean readOnly;

        AccessMode accessMode = AccessMode.AUTO;

        String reference = "";

        boolean allowEmptyValue;

        Extension[] extensions = new Extension[0];

        @Override
        public String value() {
            return value;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String allowableValues() {
            return allowableValues;
        }

        @Override
        public String access() {
            return access;
        }

        @Override
        public String notes() {
            return notes;
        }

        @Override
        public String dataType() {
            return dataType;
        }

        @Override
        public boolean required() {
            return required;
        }

        @Override
        public int position() {
            return position;
        }

        @Override
        public boolean hidden() {
            return hidden;
        }

        @Override
        public String example() {
            return example;
        }

        @Override
        public boolean readOnly() {
            return readOnly;
        }

        @Override
        public AccessMode accessMode() {
            return accessMode;
        }

        @Override
        public String reference() {
            return reference;
        }

        @Override
        public boolean allowEmptyValue() {
            return allowEmptyValue;
        }

        @Override
        public Extension[] extensions() {
            return extensions;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return ApiModelProperty.class;
        }
    }

}
