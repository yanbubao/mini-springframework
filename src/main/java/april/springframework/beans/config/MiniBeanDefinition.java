package april.springframework.beans.config;

import lombok.Builder;
import lombok.Data;

/**
 * @author yanzx
 */
@Data
@Builder
public class MiniBeanDefinition {

    private String factoryBeanName;

    private String beanClassName;

}
