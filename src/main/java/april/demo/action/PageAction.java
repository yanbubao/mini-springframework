package april.demo.action;

import april.demo.service.IQueryService;
import april.springframework.annotation.MiniAutowired;
import april.springframework.annotation.MiniController;
import april.springframework.annotation.MiniRequestMapping;
import april.springframework.annotation.MiniRequestParam;
import april.springframework.webmvc.servlet.MiniModelAndView;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author yanzx
 */
@MiniController
@MiniRequestMapping("/")
public class PageAction {

    @MiniAutowired
    private IQueryService queryService;

    @MiniRequestMapping("/first.html")
    public MiniModelAndView query(@MiniRequestParam("teacher") String teacher) {
        String result = queryService.query(teacher);
        Map<String, Object> model = Maps.newHashMap();
        model.put("teacher", teacher);
        model.put("data", result);
        model.put("token", "123456");
        return new MiniModelAndView("first.html", model);
    }
}
