package me.len.swagger.extension;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Len
 */
@Setter
@Getter
@ToString
public class BaseResponse<T> {

    int code = 200;

    String message = "OK";

    T data;

}
