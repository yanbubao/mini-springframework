package april.springframework.aop.config;

import lombok.Builder;
import lombok.Data;

/**
 * @author yanzx
 */
@Data
@Builder
public class MiniAopConfig {
    /**
     * 切入点
     */
    private String pointCut;

    /**
     * 切面
     */
    private String aspectClass;

    /**
     * 切面类before方法名称
     */
    private String aspectBefore;

    /**
     * 切面类after方法名称
     */
    private String aspectAfter;

    /**
     * 切面类afterThrowing方法名称
     */
    private String aspectAfterThrow;

    /**
     * value is java.lang.Exception
     */
    private String aspectAfterThrowingName;

}
