package april.springframework.webmvc.servlet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author yanzx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiniHandlerMapping {

    /**
     * url pattern
     */
    private Pattern pattern;

    /**
     * controller中对应的方法
     */
    private Method method;

    /**
     * controller对应的实例bean
     */
    private Object controller;
}
