package me.len.swagger.extension.handler;

import me.len.swagger.extension.annotation.ApiJsonObject;

/**
 * @author Len
 * 2020-02-19
 */
public interface ClassCreator {
    Class<?> create(ApiJsonObject apiJsonObject);

}
