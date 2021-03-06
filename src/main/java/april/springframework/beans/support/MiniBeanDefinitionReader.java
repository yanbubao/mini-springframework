package april.springframework.beans.support;

import april.springframework.beans.config.MiniBeanDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author yanzx
 */
@Slf4j
public class MiniBeanDefinitionReader {

    private static final String SCAN_PACKAGE_KEY = "scanPackage";

    private final Properties contextConfig = new Properties();

    public Properties getConfig() {
        return this.contextConfig;
    }

    /**
     * 保存扫描到的类名称.
     */
    private List<String> classNames = Lists.newArrayList();

    public MiniBeanDefinitionReader(String... contextConfigLocations) {

        doLoadConfig(contextConfigLocations[0]);

        doScanner(contextConfig.getProperty(SCAN_PACKAGE_KEY));
    }

    /**
     * 扫描相关的类
     *
     * @param scanPackage
     */
    private void doScanner(String scanPackage) {

        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));

        String basePath = Objects.requireNonNull(url).getFile();

        File classPath = new File(basePath);

        // classPath为/april/spring/的项目根目录，可理解为一个文件夹
        for (File file : Objects.requireNonNull(classPath.listFiles())) {

            if (file.isDirectory()) {

                doScanner(scanPackage + "." + file.getName());

            } else {

                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                // 完整限定类名
                String className = (scanPackage + "." + file.getName().replace(".class", ""));

                classNames.add(className);
            }
        }
    }

    /**
     * 加载配置文件props
     *
     * @param contextConfigLocation
     */
    private void doLoadConfig(String contextConfigLocation) {

        log.info("MiniBeanDefinitionReader - contextConfigLocation: {}", contextConfigLocation);

        try (InputStream resourceAsStream = this.getClass().getClassLoader()
                .getResourceAsStream(contextConfigLocation.replaceAll("classpath:", ""));
        ) {

            contextConfig.load(resourceAsStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析配置文件中扫描的class文件，并封装为BeanDefinition
     *
     * @return
     */
    public List<MiniBeanDefinition> loadBeanDefinitions() {

        List<MiniBeanDefinition> beanDefinitions = Lists.newArrayList();

        classNames.forEach(className -> {

            try {
                Class<?> beanClass = Class.forName(className);
                if(beanClass.isInterface()) return;
                // 保存beanName和对应的className（全类名）
                // beanName分为几种
                // 1、首字母小写
                beanDefinitions.add(MiniBeanDefinition.builder()
                        .factoryBeanName(toLowerFirstCase(beanClass.getSimpleName())).beanClassName(beanClass.getName()).build());

                // 2、自定义的beanName

                // 3、获取接口
                for (Class<?> i : beanClass.getInterfaces()) {

                    beanDefinitions.add(MiniBeanDefinition.builder()
                            .factoryBeanName(i.getName()).beanClassName(beanClass.getName()).build());
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        return beanDefinitions;
    }

    /**
     * 自定义手写字母转小写函数
     *
     * @param simpleClassName
     * @return
     */
    private String toLowerFirstCase(String simpleClassName) {
        char[] chars = simpleClassName.toCharArray();
        chars[0] += 32;

        return String.valueOf(chars);
    }
}
