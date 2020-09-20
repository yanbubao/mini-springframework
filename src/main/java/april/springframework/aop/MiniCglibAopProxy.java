package april.springframework.aop;

import april.springframework.aop.support.MiniAdvisedSupport;

/**
 * @author yanzx
 */
public class MiniCglibAopProxy implements MiniAopProxy {

    public MiniCglibAopProxy(MiniAdvisedSupport advisedSupport) {
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
