package com.github.length.swagger.extension.handler;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Extension;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.lang.annotation.Annotation;
import java.util.Collections;

/**
 * @author Len
 * 2020-02-19
 */
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuppressWarnings("all")
public class ApiModelPropertyImpl implements ApiModelProperty {

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
