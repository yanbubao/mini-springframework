package april.springframework.context;

import april.springframework.annotation.MiniAutowired;
import april.springframework.annotation.MiniController;
import april.springframework.annotation.MiniService;
import april.springframework.aop.MiniJdkDynamicAopProxy;
import april.springframework.aop.config.MiniAopConfig;
import april.springframework.aop.support.MiniAdvisedSupport;
import april.springframework.beans.MiniBeanWrapper;
import april.springframework.beans.config.MiniBeanDefinition;
import april.springframework.beans.support.MiniBeanDefinitionReader;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 负责创建bean以及DI.
 *
 * @author yanzx
 */
@Slf4j
public class MiniApplicationContext {

    private MiniBeanDefinitionReader reader;

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
        reader = new MiniBeanDefinitionReader(contextConfigLocations);

        try {
            // 02.解析配置文件，封装成BeanDefinition
            List<MiniBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

            log.info("all beanDefinitions : {}", JSON.toJSONString(beanDefinitions));

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

    /**
     * 可能涉及到循环依赖
     * A{ B b}
     * B{ A b}
     * 用两个缓存，循环两次
     * 1、把第一次读取结果为空的BeanDefinition存到第一个缓存
     * 2、等第一次循环之后，第二次循环再检查第一次的缓存，再进行赋值
     *
     * @param beanName
     * @param beanDefinition
     * @param beanWrapper
     */
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

                field.set(instance, this.factoryBeanWrapperCache.get(autowiredValue).getWrapperInstance());

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private Object instantiateBean(String beanName, MiniBeanDefinition beanDefinition) {
        String beanClassName = beanDefinition.getBeanClassName();
        Object instance = null;
        try {
            // 默认单例bean，ioc中存在的话直接获取，不再实例化
            if (factoryBeanObjectCache.containsKey(beanName)) {
                instance = factoryBeanObjectCache.get(beanName);
            } else {
                Class<?> clazz = Class.forName(beanClassName);
                instance = clazz.newInstance();

                //==================AOP开始=========================

                // 1、加载AOP的配置文件
                MiniAdvisedSupport advisedSupport = loadAopConfig(beanDefinition);
                advisedSupport.setTargetClass(clazz);
                advisedSupport.setTarget(instance);

                // 2、是否对bean进行AOP
                // 如果bean需要AOP，则将增强后的实例添加到IOC的bean容器中；不需要增强直接put原生bean
                if (advisedSupport.pointCutMath()) {
                    instance = new MiniJdkDynamicAopProxy(advisedSupport).getProxy();
                }

                //===================AOP结束========================

                factoryBeanObjectCache.put(beanName, instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    private MiniAdvisedSupport loadAopConfig(MiniBeanDefinition beanDefinition) {
        Properties config = this.reader.getConfig();

        MiniAopConfig aopConfig = MiniAopConfig.builder()
                .pointCut(config.getProperty("pointCut"))
                .aspectClass(config.getProperty("aspectClass"))
                .aspectBefore(config.getProperty("aspectBefore"))
                .aspectAfter(config.getProperty("aspectAfter"))
                .aspectAfterThrow(config.getProperty("aspectAfterThrow"))
                .aspectAfterThrowingName(config.getProperty("aspectAfterThrowingName"))
                .build();

        return new MiniAdvisedSupport(aopConfig);
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }
}
