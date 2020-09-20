package april.springframework.aop.aspect.advices;

import april.springframework.aop.aspect.MiniAbstractAspectAdvice;
import april.springframework.aop.aspect.MiniJoinPoint;
import april.springframework.aop.intercept.MiniMethodInterceptor;
import april.springframework.aop.intercept.MiniMethodInvocation;

import java.lang.reflect.Method;

/**
 * 前置通知拦截器
 *
 * @author yanzx
 */
public class MiniMethodBeforeAdviceInterceptor extends MiniAbstractAspectAdvice implements MiniMethodInterceptor {

    private MiniJoinPoint joinPoint;

    public MiniMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    public void before(Method method, Object[] args, Object target) throws Throwable {
        // 传送了给织入参数
        super.invokeAdviceMethod(this.joinPoint, null, null);
    }

    @Override
    public Object invoke(MiniMethodInvocation mi) throws Throwable {
        // 从被织入的代码中才能拿到
        this.joinPoint = mi;
        before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }
}
