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


}
