package april.springframework.context;

import april.springframework.annotation.MiniAutowired;
import april.springframework.annotation.MiniController;
import april.springframework.annotation.MiniService;
import april.springframework.beans.MiniBeanWrapper;
import april.springframework.beans.config.MiniBeanDefinition;
import april.springframework.beans.support.MiniBeanDefinitionReader;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负责创建bean以及DI.
 *
 * @author yanzx
 */
public class MiniApplicationContext {

    /**
     * beanDefinitionMap
     */
    private Map<String, MiniBeanDefinition> beanDefinitionMap = new HashMap<>();

    /**
     * bean原生实例Map
     */
    private Map<String, Object> factoryBeanObjectCache = new HashMap<>();

    /**
     * beanWrapperMap
     */
    private Map<String, MiniBeanWrapper> factoryBeanWrapperCache = new HashMap<>();

    public MiniApplicationContext(String... contextConfigLocations) {

        // 01.加载配置文件
        MiniBeanDefinitionReader reader = new MiniBeanDefinitionReader(contextConfigLocations);

        try {
            // 02.解析配置文件，封装成BeanDefinition
            List<MiniBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

            // 03、缓存BeanDefinition
            doResistBeanDefinitions(beanDefinitions);

            // 04、DI 注入非Lazy的bean
            doAutowired();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doAutowired() {
        // 这一步，所有的Bean并没有真正的实例化！还只是配置阶段！
        this.beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            this.getBean(beanName);
        });
    }

    private void doResistBeanDefinitions(List<MiniBeanDefinition> beanDefinitions) throws Exception {
        for (MiniBeanDefinition beanDefinition : beanDefinitions) {
            if (beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The " + beanDefinition.getFactoryBeanName() + " is exists");
            }
            beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
            beanDefinitionMap.put(beanDefinition.getBeanClassName(), beanDefinition);
        }
    }

    /**
     * bean的实例化，DI是从这里开始的
     *
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        // 1、获取BeanDefinition配置信息
        MiniBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);

        // 2、反射实例化bean
        Object instance = instantiateBean(beanName, beanDefinition);

        // 3、将bean封装为BeanWrapper
        MiniBeanWrapper beanWrapper = new MiniBeanWrapper(instance);

        // 4、保存至ioc容器
        factoryBeanWrapperCache.put(beanName, beanWrapper);

        // 5、执行DI
        populateBean(beanName, beanDefinition, beanWrapper);

        return beanWrapper.getWrapperInstance();
    }

    public Object getBean(Class<?> beanClass) {
        return this.getBean(beanClass.getName());
    }

    private void populateBean(String beanName, MiniBeanDefinition beanDefinition, MiniBeanWrapper beanWrapper) {

        Object instance = beanWrapper.getWrapperInstance();

        Class<?> clazz = beanWrapper.getWrapperClass();
        if (!(clazz.isAnnotationPresent(MiniController.class)) || clazz.isAnnotationPresent(MiniService.class)) {
            return;
        }

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(MiniAutowired.class)) {
                return;
            }

            MiniAutowired autowired = field.getAnnotation(MiniAutowired.class);
            String autowiredValue = autowired.value().trim();
            if ("".equals(autowiredValue)) {
                autowiredValue = field.getType().getName();
            }

            field.setAccessible(true);

            try {
                if (!this.factoryBeanWrapperCache.containsKey(autowiredValue)) {
                    continue;
                }

                field.set(instance, this.factoryBeanWrapperCache.get(autowiredValue));

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private Object instantiateBean(String beanName, MiniBeanDefinition beanDefinition) {
        String beanClassName = beanDefinition.getBeanClassName();
        Object instance = null;
        try {
            Class<?> clazz = Class.forName(beanClassName);
            instance = clazz.newInstance();
            factoryBeanObjectCache.put(beanName, instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }
}
