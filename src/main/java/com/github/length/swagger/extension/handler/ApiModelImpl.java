package com.github.length.swagger.extension.handler;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.lang.annotation.Annotation;

/**
 * @author Len
 * 2020-02-19
 */
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuppressWarnings("all")
public class ApiModelImpl implements ApiModel {
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
