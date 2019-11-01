import com.ql.util.express.*;
import com.ql.util.express.instruction.op.OperatorBase;
import com.ying.ApplicationDemo;
import com.ying.ql.OperatorContextPut;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationDemo.class)
public class SpringbootDemoTest {

    @Test
    public void testQL1() throws Exception {
        ExpressRunner runner = new ExpressRunner();
//        DefaultContext<String, Object> context = new DefaultContext<>();
//        context.put("a", 1);
//        context.put("b", 2);
//        String express = "a+b";
//        Object r = runner.execute(express, context, null, true, false);
//        Assert.assertEquals(3, r);

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
        final String express = "int n=10;" +
                "int sum=0;int i = 0;" +
                "for(i=0;i<n;i++){" +
                "sum=sum+i;" +
                "}" +
                "return sum;";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        Object r = runner.execute(express, context, null, true, false);
        System.out.println(r);
    }

    @Test
    public void logicalTernaryOperationsTest() throws Exception {
//        final String express = "int a=1;int b=2;int max = a>b?a:b;";
        final String express = "a=1;b=2;max(a,b);";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        Object r = runner.execute(express, context, null, true, false);
        System.out.println(r);
    }

    @Test
    public void testQL2() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        OperatorBase op = new OperatorContextPut("contextPut");
        runner.addFunction("contextPut", op);
        String exp = "contextPut('success','false');contextPut('error','错误信息');contextPut('warning','提醒信息')";
        IExpressContext<String, Object> context = new DefaultContext<String, Object>();
        context.put("success", "true");
        Object result = runner.execute(exp, context, null, false, true);
        System.out.println(result);
        System.out.println(context);
    }
}
