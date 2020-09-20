package april.springframework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @author yanzx
 */
public abstract class MiniAbstractAspectAdvice implements MiniAdvice {

    private Method aspectMethod;

    private Object aspectTarget;

    public MiniAbstractAspectAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    public Object invokeAdviceMethod(MiniJoinPoint joinPoint, Object resultValue, Throwable e) throws Throwable {
        Class<?>[] parameterTypes = this.aspectMethod.getParameterTypes();
        if (parameterTypes.length == 0) {
            return this.aspectMethod.invoke(aspectTarget);
        }
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == MiniJoinPoint.class) {
                args[i] = joinPoint;
            } else if (parameterTypes[i] == Throwable.class) {
                args[i] = e;
            } else if (parameterTypes[i] == Object.class) {
                args[i] = resultValue;
            } else {
                throw new IllegalAccessException("parameter type error : " + parameterTypes[i]);
            }
        }
        return this.aspectMethod.invoke(aspectTarget, args);
    }

}
