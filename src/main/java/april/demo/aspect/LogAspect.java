package april.demo.aspect;

import lombok.extern.slf4j.Slf4j;

/**
 * 切面
 *
 * @author yanzx
 */
@Slf4j
public class LogAspect {
    public void before() {
        log.info("Invoker Before Method.");
    }

    public void after() {
        log.info("Invoker After Method.");
    }

    public void afterThrowing() {
        log.info("出现异常");
    }
}
