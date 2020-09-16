package april.springframework.aop.aspect.advices;

import april.springframework.aop.aspect.MiniAbstractAspectAdvice;
import april.springframework.aop.intercept.MiniMethodInterceptor;

import java.lang.reflect.Method;

/**
 * 前置通知拦截器
 *
 * @author yanzx
 */
public class MiniMethodBeforeAdviceInterceptor extends MiniAbstractAspectAdvice implements MiniMethodInterceptor {

    public MiniMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }
}
