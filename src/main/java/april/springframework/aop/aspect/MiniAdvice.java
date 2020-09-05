package april.springframework.aop.aspect;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author yanzx
 */
@Data
@Builder
public class MiniAdvice {

    /**
     * 切面实例
     */
    private Object aspectInstance;

    /**
     * 切面类方法
     */
    private Method adviceMethod;

    /**
     * throwName
     */
    private String throwName;

}
