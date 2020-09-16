package april.springframework.beans.support;

import april.springframework.beans.config.MiniBeanDefinition;
import april.springframework.context.support.MiniAbstractApplicationContext;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author yanzx
 */
public class MiniDefaultListableBeanFactory extends MiniAbstractApplicationContext {
    /**
     * 存储BeanDefinition aka伪IoC容器
     */
    protected final Map<String, MiniBeanDefinition> beanDefinitionMap = Maps.newConcurrentMap();
}
