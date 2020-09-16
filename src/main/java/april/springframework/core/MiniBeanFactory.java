package april.springframework.core;

/**
 * 单例工厂的顶层设计
 *
 * @author yanzx
 */
public interface MiniBeanFactory {
    Object getBean(String beanName) throws Exception;

    Object getBean(Class<?> beanClass) throws Exception;
}
