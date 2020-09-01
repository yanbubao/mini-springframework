package april.springframework.beans.config;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author yanzx
 */
@Data
@Builder
@ToString
public class MiniBeanDefinition {

    private String factoryBeanName;

    private String beanClassName;

}
