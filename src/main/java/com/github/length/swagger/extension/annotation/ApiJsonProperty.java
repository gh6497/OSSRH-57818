package com.github.length.swagger.extension.annotation;

import io.swagger.annotations.ApiModelProperty;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Len
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiJsonProperty {
    /**
     * @see ApiModelProperty#dataType().
     */
    Class<?> clazz() default String.class;

    /**
     * @see ApiModelProperty#value().
     */
    String value() default "";

    /**
     * @see ApiModelProperty#name().
     */
    String name() default "";

    /**
     * @see ApiModelProperty#allowableValues().
     */
    String allowableValues() default "";

    /**
     * @see ApiModelProperty#notes().
     */
    String notes() default "";

    /**
     * @see ApiModelProperty#required().
     */
    boolean required() default true;

    /**
     * @see ApiModelProperty#example().
     */
    String example() default "";

    /**
     * @see ApiModelProperty#position().
     */
    int position() default 0;

    /**
     * @see ApiModelProperty#hidden().
     */
    boolean hidden() default false;

    /**
     * @see ApiModelProperty#allowableValues().
     */
    boolean allowEmptyValue() default false;

    /**
     * Declares a container wrapping the response.
     * <p>
     * Valid values are "List".Any other value will be ignored.
     */
    String container() default "";

}
