package april.demo.action;

import april.demo.service.IModifyService;
import april.demo.service.IQueryService;
import april.springframework.annotation.MiniAutowired;
import april.springframework.annotation.MiniController;
import april.springframework.annotation.MiniRequestMapping;
import april.springframework.annotation.MiniRequestParam;
import april.springframework.webmvc.servlet.MiniModelAndView;
import com.google.common.collect.Maps;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author yanzx
 */
@MiniController
@MiniRequestMapping("/web")
public class MyAction {

    @MiniAutowired
    private IQueryService queryService;
    @MiniAutowired
    private IModifyService modifyService;

    @MiniRequestMapping("/query.json")
    public MiniModelAndView query(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @MiniRequestParam("name") String name) {
        String result = queryService.query(name);
        return out(response, result);
    }

    @MiniRequestMapping("/add*.json")
    public MiniModelAndView add(HttpServletRequest request,
                                HttpServletResponse response,
                                @MiniRequestParam("name") String name, @MiniRequestParam("addr") String addr) {
        try {
            String result = modifyService.add(name, addr);
            return out(response, result);
        } catch (Throwable e) {
            Map<String, String> model = Maps.newHashMap();
            model.put("detail", e.getCause().getMessage());
            model.put("stackTrace", Arrays.toString(e.getStackTrace()));
            return new MiniModelAndView("500", model);
        }
    }

    @MiniRequestMapping("/remove.json")
    public MiniModelAndView remove(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @MiniRequestParam("id") Integer id) {
        String result = modifyService.remove(id);
        return out(response, result);
    }

    @MiniRequestMapping("/edit.json")
    public MiniModelAndView edit(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @MiniRequestParam("id") Integer id,
                                 @MiniRequestParam("name") String name) {
        String result = modifyService.edit(id, name);
        return out(response, result);
    }


    private MiniModelAndView out(HttpServletResponse resp, String str) {
        try {
            resp.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
