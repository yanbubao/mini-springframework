package april.demo.service.impl;

import april.demo.service.IModifyService;
import april.springframework.annotation.MiniService;

/**
 * @author yanzx
 */
@MiniService
public class ModifyService implements IModifyService {
    @Override
    public String add(String name, String addr) throws Exception {
        throw new Exception("测试afterThrowing所抛出的异常！");
       // return "class:modifyService, method:add, param:name=" + name + ", addr=" + addr;
    }

    @Override
    public String edit(Integer id, String name) {
        return "class:modifyService, method:edit, param:id=" + id + ", name=" + name;
    }

    @Override
    public String remove(Integer id) {
        return "class:modifyService, method:remove, param:id=" + id;
    }
}
