package com.github.length.swagger.extension.handler;

import com.github.length.swagger.extension.annotation.ApiJsonObject;

/**
 * @author Len
 * 2020-02-19
 */
public interface ClassCreator {
    Class<?> create(ApiJsonObject apiJsonObject);

}
