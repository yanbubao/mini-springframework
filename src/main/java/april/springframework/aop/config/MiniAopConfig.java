package april.springframework.aop.config;

import lombok.Data;

/**
 * @author yanzx
 */
@Data
public class MiniAopConfig {
    /**
     * 切入点
     */
    private String pointCut;
}
