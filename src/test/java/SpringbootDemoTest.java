import com.ql.util.express.*;
import com.ying.ApplicationDemo;
import com.ying.ql.QlExpressUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationDemo.class)
public class SpringbootDemoTest {

    @Autowired
    private QlExpressUtil qlExpressUtil;

    @Test
    public void testQL1() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        //数据的预先加载
        ExpressRemoteCacheRunner cacheRunner = new LocalExpressCacheRunner(runner);
        cacheRunner.loadCache("计算平均成绩", "(语文+数学+英语)/3.0");
        cacheRunner.loadCache("是否优秀", "计算平均成绩>90");

        IExpressContext<String, Object> context = new DefaultContext<String, Object>();
        context.put("语文", 88);
        context.put("数学", 99);
        context.put("英语", 95);
        //ExpressRemoteCacheRunner都只能执行自己原有的脚本内容，而且相互之间隔离，保证最高的脚本安全性
        System.out.println(cacheRunner.execute("计算平均成绩", context, null, false, false, null));
        try {
            System.out.println(cacheRunner.execute("计算平均成绩>90", context, null, false, false, null));
        } catch (Exception e) {
            System.out.println("ExpressRemoteCacheRunner只支持预先加载的脚本内容");
        }
        try {
            System.out.println(cacheRunner.execute("是否优秀", context, null, false, false, null));
        } catch (Exception e) {
            System.out.println("ExpressRemoteCacheRunner不支持脚本间的相互调用");
        }
    }

    @Test
    public void operateLoopTest() throws Exception {
        String express = "int sum=0;int i = 0;" +
                "for(i=0;i<n;i++){" +
                "sum=sum+i;" +
                "}" +
                "return sum;";
        Map<String, Object> map = new HashMap<>();
        map.put("n", 10);
        System.out.println(qlExpressUtil.execute(express, map));
    }

    @Test
    public void testQL2() throws Exception {
//        String express = "max(a,b)";
//        String express = "min(a,b)";
//        String express = "round(19.08,1)";
        String express = "a>b?a:b";
//        String express = "a+b";
        Map<String, Object> map = new HashMap<>();
        map.put("a", 8);
        map.put("b", 2);
        Object re = qlExpressUtil.execute(express, map);
        System.out.println(re);
    }
}
