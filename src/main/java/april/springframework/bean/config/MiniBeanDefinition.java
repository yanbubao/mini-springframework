package april.springframework.bean.config;

import lombok.Builder;
import lombok.Data;

/**
 * @author yanzx
 */
@Data
@Builder
public class MiniBeanDefinition {

    private String factoryBeanName;

    private String classBeanName;

}
