package april.springframework.aop.aspect;

import java.lang.reflect.Method;

/**
 * 回调
 *
 * @author yanzx
 */
public interface MiniJoinPoint {

    Object getThis();

    Object[] getArguments();

    Method getMethod();

    void setUserAttribute();

    Object getUserAttribute();
}
