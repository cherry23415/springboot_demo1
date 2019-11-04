package com.ying.ql;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class QlExpressUtil implements ApplicationContextAware {

    private static ExpressRunner runner;

    static {
        runner = new ExpressRunner();
    }

    private static boolean isInitialRunner = false;
    private ApplicationContext applicationContext;

    private void initRunner(ExpressRunner runner) {
        if (isInitialRunner == true) {
            return;
        }
        synchronized (runner) {
            if (isInitialRunner == true) {
                return;
            }
            try {
                //在此可以加入预定义函数
            } catch (Exception e) {
                throw new RuntimeException("初始化失败表达式", e);
            }
        }
        isInitialRunner = true;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Object execute(String statement, Map<String, Object> context) throws Exception {
        initRunner(runner);
        IExpressContext expressContext = new QLExpressContext(context, applicationContext);
        return runner.execute(statement, expressContext, null, true, false);
    }

}
