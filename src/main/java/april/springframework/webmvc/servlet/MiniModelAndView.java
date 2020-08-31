package april.springframework.webmvc.servlet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author yanzx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiniModelAndView {
    private String viewName;
    private Map<String, ?> model;

    public MiniModelAndView(String viewName) {
        this.viewName = viewName;
    }
}
