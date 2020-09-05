package april.springframework.aop;

import april.springframework.aop.aspect.MiniAdvice;
import april.springframework.aop.support.MiniAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author yanzx
 */
public class MiniJdkDynamicAopProxy implements InvocationHandler {

    private final MiniAdvisedSupport advisedSupport;

    public MiniJdkDynamicAopProxy(MiniAdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Map<String, MiniAdvice> adviceMap = this.advisedSupport.getAdvices(method, null);

        Object returnValue;
        try {
            invokeAdvice(adviceMap.get("before"));

            returnValue = method.invoke(this.advisedSupport.getTarget(), args);

            invokeAdvice(adviceMap.get("after"));

        } catch (Exception e) {
            invokeAdvice(adviceMap.get("afterThrow"));
            throw e;
        }

        return returnValue;
    }

    private void invokeAdvice(MiniAdvice advice) throws Exception {
        advice.getAdviceMethod().invoke(advice.getAspectInstance());
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                this.advisedSupport.getTargetClass().getInterfaces(),
                this);
    }
}
