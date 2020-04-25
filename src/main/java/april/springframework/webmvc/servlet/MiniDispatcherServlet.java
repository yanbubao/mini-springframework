package april.springframework.webmvc.servlet;

import april.springframework.annotation.MiniController;
import april.springframework.annotation.MiniRequestMapping;
import april.springframework.context.MiniApplicationContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yanzx
 */
@Slf4j
public class MiniDispatcherServlet extends HttpServlet {

    private static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private MiniApplicationContext applicationContext;

    private Map<String, Object> ioc = new HashMap<>();

    private Map<String, Method> handlerMapping = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // TODO
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        // 初始化SpringIoC容器
        applicationContext = new MiniApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));

        // mvc流程
        // 初始化处理器映射器HandlerMapping
        doInitHandlerMapping();

        log.info("Mini Spring finish init! ");
    }

    /**
     * 初始化处理器映射器HandlerMapping
     */
    private void doInitHandlerMapping() {

        if (ioc.isEmpty()) {
            log.info("IoC容器为空！无法初始化HandlerMapping！");
            return;
        }

        for (Map.Entry<String, Object> bean : ioc.entrySet()) {

            Class<?> beanClazz = bean.getValue().getClass();

            if (!beanClazz.isAnnotationPresent(MiniController.class)) {
                continue;
            }

            String baseUrl = "";
            if (beanClazz.isAnnotationPresent(MiniRequestMapping.class)) {
                baseUrl = beanClazz.getAnnotation(MiniRequestMapping.class).value();
            }

            // 只获取public方法
            for (Method method : beanClazz.getMethods()) {

                if (!method.isAnnotationPresent(MiniRequestMapping.class)) {
                    continue;
                }

                MiniRequestMapping requestMapping = method.getAnnotation(MiniRequestMapping.class);

                String url = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("/+", "/");

                handlerMapping.put(url, method);

                log.info("mapping: {}, {}", url, method);
            }


        }

    }
}
