package me.len.swagger.extension.util;
/*
 * Created at 2020-03-17.
 */

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import lombok.experimental.UtilityClass;
import springfox.documentation.RequestHandler;

/**
 * @author Len
 */
@UtilityClass
@SuppressWarnings("all")
public class RequestHandlerSelectorsUtil {
    public static final String SEPARATOR = ";";

    /**
     * 重写basePackage方法，使能够实现多包访问，复制贴上去
     */
    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(SEPARATOR)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }
}
