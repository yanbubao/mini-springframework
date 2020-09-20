package april.springframework.aop.intercept;

/**
 * @author yanzx
 */
public interface MiniMethodInterceptor {
    Object invoke(MiniMethodInvocation invocation) throws Throwable;
}
