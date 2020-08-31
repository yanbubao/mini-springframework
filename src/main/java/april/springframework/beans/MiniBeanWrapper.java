package april.springframework.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yanzx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiniBeanWrapper {
    private Object wrapperInstance;
    private Class<?> wrapperClass;

    public MiniBeanWrapper(Object instance) {
        this.wrapperInstance = instance;
        this.wrapperClass = instance.getClass();
    }
}
