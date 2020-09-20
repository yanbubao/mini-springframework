package april.springframework.aop.intercept;

import april.springframework.aop.aspect.MiniJoinPoint;

import java.lang.reflect.Method;

/**
 * @author yanzx
 */
public class MiniMethodInvocation implements MiniJoinPoint {

    public Object proceed() throws Throwable {
        return null;
    }

    @Override
    public Object getThis() {
        return null;
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public Method getMethod() {
        return null;
    }

    @Override
    public void setUserAttribute() {

    }

    @Override
    public Object getUserAttribute() {
        return null;
    }
}
