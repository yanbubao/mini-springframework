package april.springframework.webmvc.servlet;

import april.springframework.annotation.MiniController;
import april.springframework.annotation.MiniRequestMapping;
import april.springframework.context.MiniApplicationContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 委派，负责任务分发
 *
 * @author yanzx
 */
@Slf4j
public class MiniDispatcherServlet extends HttpServlet {

    private MiniApplicationContext applicationContext;

    /**
     * Spring中使用List存储handlerMapping
     */
    private List<MiniHandlerMapping> handlerMappings = Lists.newArrayList();

    /**
     * handlerMapping 和 HandlerAdapter 一对一映射
     */
    private Map<MiniHandlerMapping, MiniHandlerAdapter> handlerAdapterMap = new HashMap<>();

    /**
     * viewResolvers 每个视图对应一个解析器
     */
    private List<MiniViewResolver> viewResolvers = Lists.newArrayList();

    private static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            log.info("uri : {}",req.getRequestURI());
            doDispatch(req, resp);
        } catch (Exception e) {
            try {
                processDispatchResult(req, resp, new MiniModelAndView("500"));
            } catch (Exception e1) {
                e1.printStackTrace();
                resp.getWriter().write("500 Exception, Detail : " + Arrays.toString(e.getStackTrace()));
            }
        }

    }


    /**
     * inlet
     *
     * @param req
     * @param resp
     */
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 1、通过url获取 handlerMapping
        MiniHandlerMapping handlerMapping = getHandler(req);
        if (handlerMapping == null) {
            log.error("handlerMapping is null");
            processDispatchResult(req, resp, new MiniModelAndView("404"));
            return;
        }

        // 2、根据handlerMapping 获取handlerAdapter
        MiniHandlerAdapter ha = handlerAdapterMap.get(handlerMapping);

        // 3、解析某个方法的形参和返回值后，统一封装为ModelAndView对象
        MiniModelAndView mv = ha.handler(req, resp, handlerMapping);

        // 4、modelAndView转换为 ViewResolver
        this.processDispatchResult(req, resp, mv);
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp,
                                       MiniModelAndView modelAndView) throws Exception {
        if (modelAndView == null) return;
        if (this.viewResolvers.isEmpty()) return;
        for (MiniViewResolver viewResolver : this.viewResolvers) {
            MiniView view = viewResolver.resolveViewName(modelAndView.getViewName());
            // 输出至浏览器
            view.render(modelAndView.getModel(), req, resp);
            return;
        }

    }

    private MiniHandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappings.isEmpty()) return null;
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        uri = uri.replaceAll(contextPath, "").replaceAll("/+", "/");

        for (MiniHandlerMapping handlerMapping : this.handlerMappings) {
            Matcher matcher = handlerMapping.getPattern().matcher(uri);
            if (!matcher.matches()) continue;
            return handlerMapping;
        }
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        // 初始化SpringIoC容器
        applicationContext = new MiniApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));

        // mvc流程
        // 初始化mvc组件
        initStrategies(applicationContext);

        log.info("Mini Spring finish init! ");
    }


    /**
     * spring mvc 中 initStrategies函数用于初始化九大组件，此demo中只演示三种组件
     *
     * @param context
     */
    private void initStrategies(MiniApplicationContext context) {
        initHandlerMappings(context);
        initHandlerAdapters(context);
        initViewResolvers(context);
    }

    private void initViewResolvers(MiniApplicationContext context) {
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath =
                Objects.requireNonNull(this.getClass().getClassLoader().getResource(templateRoot)).getFile();

        File templateRootDir = new File(templateRootPath);
        for (File file : Objects.requireNonNull(templateRootDir.listFiles())) {
            this.viewResolvers.add(new MiniViewResolver(templateRoot));
        }
    }

    private void initHandlerAdapters(MiniApplicationContext context) {
        for (MiniHandlerMapping handlerMapping : this.handlerMappings) {
            handlerAdapterMap.put(handlerMapping, new MiniHandlerAdapter());
        }
    }

    private void initHandlerMappings(MiniApplicationContext context) {
        if (context.getBeanDefinitionCount() == 0) return;
        for (String beanName : context.getBeanDefinitionNames()) {
            Object instance = context.getBean(beanName);
            Class<?> beanClass = instance.getClass();

            if (!beanClass.isAnnotationPresent(MiniController.class)) continue;

            // 获取@RequestMapping中属性值
            String baseUrl = "";
            if (beanClass.isAnnotationPresent(MiniRequestMapping.class)) {
                MiniRequestMapping requestMapping = beanClass.getAnnotation(MiniRequestMapping.class);
                baseUrl = requestMapping.value();
            }

            for (Method method : beanClass.getMethods()) {
                if (!method.isAnnotationPresent(MiniRequestMapping.class)) continue;
                MiniRequestMapping methodRequestMapping = method.getAnnotation(MiniRequestMapping.class);
                String regex = ("/" + baseUrl + "/" + methodRequestMapping.value().replaceAll("\\*", ".*")).replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);

                handlerMappings.add(new MiniHandlerMapping(pattern, method, instance));
                log.info("mvc mapped : " + regex + ", " + method);
            }
        }
    }
}
