package april.springframework.aop.aspect.advices;

import april.springframework.aop.aspect.MiniAbstractAspectAdvice;
import april.springframework.aop.intercept.MiniMethodInterceptor;

import java.lang.reflect.Method;

/**
 * 异常通知拦截器
 *
 * @author yanzx
 */
public class MiniAfterThrowingAdviceInterceptor extends MiniAbstractAspectAdvice implements MiniMethodInterceptor {

    public MiniAfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }
}
