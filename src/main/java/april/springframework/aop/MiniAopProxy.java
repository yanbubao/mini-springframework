package april.springframework.aop;

/**
 * @author yanzx
 */
public interface MiniAopProxy {
    Object getProxy();

    Object getProxy(ClassLoader classLoader);
}
