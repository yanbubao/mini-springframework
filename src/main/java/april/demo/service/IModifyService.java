package april.demo.service;

/**
 * @author yanzx
 */
public interface IModifyService {
    String add(String name, String addr) throws Exception;
    String edit(Integer id, String name);
    String remove(Integer id);
}
