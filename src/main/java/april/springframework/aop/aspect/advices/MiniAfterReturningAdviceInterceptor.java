package april.springframework.aop.aspect.advices;

import april.springframework.aop.aspect.MiniAbstractAspectAdvice;
import april.springframework.aop.intercept.MiniMethodInterceptor;

import java.lang.reflect.Method;

/**
 * 后置通知拦截器
 *
 * @author yanzx
 */
public class MiniAfterReturningAdviceInterceptor extends MiniAbstractAspectAdvice implements MiniMethodInterceptor {

    public MiniAfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }
}
