package april.springframework.aop.aspect.advices;

import april.springframework.aop.aspect.MiniAbstractAspectAdvice;
import april.springframework.aop.intercept.MiniMethodInterceptor;
import april.springframework.aop.intercept.MiniMethodInvocation;
import lombok.Setter;

import java.lang.reflect.Method;

/**
 * 异常通知拦截器
 *
 * @author yanzx
 */
public class MiniAfterThrowingAdviceInterceptor extends MiniAbstractAspectAdvice implements MiniMethodInterceptor {

    @Setter
    private String throwingName;

    public MiniAfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MiniMethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        } catch (Throwable e) {
            invokeAdviceMethod(mi, null, e.getCause());
            throw e;
        }
    }

}
