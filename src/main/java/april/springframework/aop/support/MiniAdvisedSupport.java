package april.springframework.aop.support;

import april.springframework.aop.aspect.MiniAdvice;
import april.springframework.aop.config.MiniAopConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yanzx
 */
@Slf4j
public class MiniAdvisedSupport {

    /**
     * aop config
     */
    private final MiniAopConfig aopConfig;

    /**
     * 目标对象
     */
    private Object target;

    /**
     * 目标类Class对象
     */
    private Class<?> targetClass;

    /**
     * 切入点匹配的正则
     */
    private Pattern pointCutClassPattern;

    /**
     * <目标Method, <切面方法名, 通知>>
     *   Spring源码中是Map<MethodCacheKey, List<Object>> methodCache 这里为了获取时方便 做了调整
     */
    private Map<Method, Map<String, MiniAdvice>> methodCache;

    public MiniAdvisedSupport(MiniAopConfig aopConfig) {
        this.aopConfig = aopConfig;
    }

    public boolean pointCutMath() {
        log.info("AdvisedSupport - targetClass : {}", this.targetClass.toString());
        return this.pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }

    /**
     * 解析配置文件中AOP
     */
    private void parse() {

        // 将spring切面表达式变成Java能够识别的正则表达式
        String pointCut = aopConfig.getPointCut()
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");

        String pointCutForClassRegex = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        log.info("MiniAdvisedSupport - pointCutForClassRegex : {}", pointCutForClassRegex);

        this.pointCutClassPattern = Pattern.compile("class " + pointCutForClassRegex.substring(pointCutForClassRegex.lastIndexOf(" ") + 1));

        // 享元的Advice通知缓存池
        methodCache = new HashMap<>();

        // 切面表达式的正则对象
        Pattern pointCutPattern = Pattern.compile(pointCut);

        try {
            // 切面Class
            Class<?> aspectClazz = Class.forName(this.aopConfig.getAspectClass());

            // 切面中方法Map
            Map<String, Method> aspectMethodMap = new HashMap<>(aspectClazz.getMethods().length);
            for (Method method : aspectClazz.getMethods()) {
                aspectMethodMap.put(method.getName(), method);
            }

            // 以上都是初始化工作，准备阶段
            // 从这里开始封装Advice
            for (Method method : this.targetClass.getMethods()) {
                String targetMethodName = method.toString();
                log.info("MiniAdvisedSupport - targetMethodName : {}", targetMethodName);
                if (targetMethodName.contains("throws")) {
                    targetMethodName = targetMethodName.substring(0, targetMethodName.lastIndexOf("throws")).trim();
                }

                Matcher matcher = pointCutPattern.matcher(targetMethodName);
                if (matcher.matches()) {
                    Map<String, MiniAdvice> adviceMap = new HashMap<>();
                    if (StringUtils.isNotBlank(this.aopConfig.getAspectBefore())) {
                        adviceMap.put("before", MiniAdvice.builder()
                                .aspectInstance(aspectClazz.newInstance())
                                .adviceMethod(aspectMethodMap.get(this.aopConfig.getAspectBefore())).build());
                    }

                    if (StringUtils.isNotBlank(this.aopConfig.getAspectAfter())) {
                        adviceMap.put("after", MiniAdvice.builder()
                                .aspectInstance(aspectClazz.newInstance())
                                .adviceMethod(aspectMethodMap.get(this.aopConfig.getAspectAfter())).build());
                    }

                    if (StringUtils.isNotBlank(this.aopConfig.getAspectAfterThrow())) {
                        adviceMap.put("afterThrow", MiniAdvice.builder()
                                .aspectInstance(aspectClazz.newInstance())
                                .adviceMethod(aspectMethodMap.get(this.aopConfig.getAspectAfterThrow()))
                                .throwName(this.aopConfig.getAspectAfterThrowingName()).build());
                    }

                    // 跟目标代理类的业务方法和Advices建立一对多个关联关系，以便在Proxy类中获得
                    methodCache.put(method, adviceMap);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param method 目标方法
     * @param o
     * @return 切面AdviceMap
     */
    public Map<String, MiniAdvice> getAdvices(Method method, Object o) throws Exception {
        log.info("method : {}", method);
        Map<String, MiniAdvice> adviceMap = methodCache.get(method);
        if (adviceMap == null) {
            Method m = this.targetClass.getMethod(method.getName(), method.getParameterTypes());
            log.info("m : {}", m);
            adviceMap = methodCache.get(m);
            this.methodCache.put(m, adviceMap);
        }
        return adviceMap;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
        parse();
    }


    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

}