package april.springframework.context;

import april.springframework.bean.config.MiniBeanDefinition;
import april.springframework.beans.support.MiniBeanDefinitionReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负责创建bean以及DI.
 *
 * @author yanzx
 */
public class MiniApplicationContext {

    private Map<String, MiniBeanDefinition> beanDefinitionMap = new HashMap<>();


    public MiniApplicationContext(String... contextConfigLocations) {

        // 01.加载配置文件
        MiniBeanDefinitionReader reader = new MiniBeanDefinitionReader(contextConfigLocations);

        try {
            // 02.解析配置文件，封装成BeanDefinition
            List<MiniBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

            // 03、缓存BeanDefinition
            doResistBeanDefinitions(beanDefinitions);

            // 04、getBean触发DI
            doAutowired();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doAutowired() {
    }

    private void doResistBeanDefinitions(List<MiniBeanDefinition> beanDefinitions) throws Exception {
        for (MiniBeanDefinition beanDefinition : beanDefinitions) {
            if (beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The " + beanDefinition.getFactoryBeanName() + " is exists");
            }
            beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
            beanDefinitionMap.put(beanDefinition.getClassBeanName(), beanDefinition);
        }
    }

    public Object getBean(String beanName) {
        // 1、获取BeanDefinition配置信息
        // 2、反射实例化bean
        // 3、将bean封装为BeanWrapper
        // 4、保存至ioc容器
        // 5、执行DI
        return null;
    }
}
