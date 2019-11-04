package com.ying.ql;

import com.ql.util.express.IExpressContext;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class QLExpressContext extends HashMap<String, Object> implements IExpressContext<String, Object> {

    private ApplicationContext context;

    public QLExpressContext(ApplicationContext context) {
        this.context = context;
    }

    public QLExpressContext(Map<String, Object> map, ApplicationContext context) {
        super(map);
        this.context = context;

    }

    /**
     * 根据名称从属性列表中提取属性值
     */
    public Object get(Object name) {
        Object result = super.get(name);
        try {
            if (result == null && this.context != null && this.context.containsBean((String) name)) {
                //如果在Spring容器中包含bean，则返回String的Bean
                result = this.context.getBean((String) name);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Object put(String name, Object object) {
        return super.put(name, object);
    }
}
