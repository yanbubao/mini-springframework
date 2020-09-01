package april.springframework.webmvc.servlet;

import java.io.File;
import java.util.Objects;

/**
 * @author yanzx
 */
public class MiniViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private final File templateRootDir;

    public MiniViewResolver(String templateRoot) {
        String templateRootPath =
                Objects.requireNonNull(this.getClass().getClassLoader().getResource(templateRoot)).getFile();
        templateRootDir = new File(templateRootPath);
    }

    public MiniView resolveViewName(String viewName) {
        if (null == viewName || "".equals(viewName.trim())) {
            return null;
        }
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));
        return new MiniView(templateFile);
    }
}
