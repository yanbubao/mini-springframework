package april.springframework.context.support;

/**
 * IoC容器实现的顶层设计
 *
 * @author yanzx
 */
public abstract class MiniAbstractApplicationContext {
    /**
     * 提供给子类重写
     *
     * @throws Exception
     */
    protected void refresh() throws Exception {

    }
}
