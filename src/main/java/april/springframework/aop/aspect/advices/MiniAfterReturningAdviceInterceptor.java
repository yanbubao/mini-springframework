package april.springframework.aop.aspect.advices;

import april.springframework.aop.aspect.MiniAbstractAspectAdvice;
import april.springframework.aop.aspect.MiniJoinPoint;
import april.springframework.aop.intercept.MiniMethodInterceptor;
import april.springframework.aop.intercept.MiniMethodInvocation;

import java.lang.reflect.Method;

/**
 * 后置通知拦截器
 *
 * @author yanzx
 */
public class MiniAfterReturningAdviceInterceptor extends MiniAbstractAspectAdvice implements MiniMethodInterceptor {

    private MiniJoinPoint joinPoint;

    public MiniAfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MiniMethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.joinPoint = mi;
        afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
        return retVal;
    }

    private void afterReturning(Object retVal, Method method, Object[] arguments, Object aThis) throws Throwable {
        super.invokeAdviceMethod(this.joinPoint, retVal, null);
    }
}
