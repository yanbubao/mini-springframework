package april.springframework.context;

import april.springframework.bean.config.MiniBeanDefinition;
import april.springframework.beans.support.MiniBeanDefinitionReader;

import java.util.List;

/**
 * 负责创建bean以及DI.
 *
 * @author yanzx
 */
public class MiniApplicationContext {


    public MiniApplicationContext(String... contextConfigLocations) {

        // 01.加载配置文件
        MiniBeanDefinitionReader reader = new MiniBeanDefinitionReader(contextConfigLocations);

        // 02.解析配置文件，封装成BeanDefinition
        List<MiniBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();


    }
}
