package me.len.swagger.extension;
/*
 * Created on 2020-06-03.
 */

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import me.len.swagger.extension.annotation.ApiJsonObject;
import me.len.swagger.extension.annotation.ApiJsonProperty;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * @author Len
 */
@RestController
@RequestMapping("/some")
public class SomeController {
    @GetMapping("response")
    @ApiJsonObject(
      name = "Response",
      properties = {
        @ApiJsonProperty(name = "id", value = "id", clazz = Integer.class),
        @ApiJsonProperty(name = "hobbies", value = "爱好", container = "List"),
        @ApiJsonProperty(name = "subject", value = "科目", clazz = Subject.class)
      }
    )
    public Map<String, Object> response() {
        return Collections.emptyMap();
    }

    @PostMapping("parameters")
    public void parameters(@RequestBody
                           @ApiJsonObject(
                             name = "Parameter",
                             properties = {
                               @ApiJsonProperty(name = "id", value = "id", clazz = Integer.class),
                               @ApiJsonProperty(name = "hobbies", value = "爱好", container = "List"),
                               @ApiJsonProperty(name = "subject", value = "科目", clazz = Subject.class)})

                             Map<String, Object> requestBody) {

    }


    @Getter
    @Setter
    @ToString
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Subject {
        @ApiModelProperty(value = "名称")
        String name;
    }

}
