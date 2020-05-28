package me.len.swagger.extension.annotation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ResponseHeader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Len
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface ApiJsonObject {

    /**
     * Provide a longer description of the class.
     */
    String description() default "";

    /** 默认生成包 */
    String DEFAULT_PACKAGE = "io.swagger.model.temp";

    /**
     * 类名
     */
    String name();

    /**
     * 包名
     */
    String packageName() default DEFAULT_PACKAGE;

    /**
     * @see ApiModel#value()
     */
    String value() default "";


    ApiJsonProperty[] properties();

    int code() default 200;

    String message() default "OK";

    ResponseHeader[] responseHeaders() default @ResponseHeader(name = "");

    /**
     * Declares a container wrapping the response.
     * <p>
     * Valid values are "List".Any other value will be ignored.
     */
    String container() default "";
}
