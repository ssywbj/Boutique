package com.suheng.ssy.boutique;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runners.model.Statement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.core.CombinableMatcher.both;
import static org.hamcrest.core.CombinableMatcher.either;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Created by wbj on 2019/1/2.
 */

public class AssertThatTest {

    @Test
    public void testAssertThat1() throws Exception {
        Assert.assertThat(1, nullValue());
    }

    @Test
    public void testAssertThat2() throws Exception {
        Assert.assertThat("Hello AUT", both(startsWith("Hel")).and(endsWith("UT")));
    }

    @Test
    public void testAssertThat3() throws Exception {
        Assert.assertThat("Hello AUT", either(startsWith("Heol")).or(endsWith("UT")));
    }

    @Test
    public void testAssertThat4() throws Exception {
        Assert.assertThat(6, equalTo(6));//相等
    }

    @Test
    public void testAssertThat5() throws Exception {
        Assert.assertThat("jj556wtrr666", new IsPhoneMatcher());
    }

    @Test
    public void testAssertThat6() throws Exception {
        Assert.assertThat("jj556wtrr666".equals("ffff"), is(true));
        //Assert.assertEquals(true, "jj556wtrr666".equals("ffff"));
    }

    public class IsPhoneMatcher extends BaseMatcher<String> {

        @Override
        public boolean matches(Object item) {
            if (item == null) {
                return false;
            }

            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher((String) item);
            return matcher.find();//查找字符串中是否包含数字
            //return matcher.matches();//精确匹配字符串是不是纯数字
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("预计此字符串是包含数字！");
        }

        @Override
        public void describeMismatch(Object item, Description description) {
            super.describeMismatch(item, description);
            description.appendText(item.toString() + "不包含数字！");
        }
    }

    @Rule
    public MyRule mMyRule = new MyRule();

    public class MyRule implements TestRule {

        @Override
        public Statement apply(final Statement base, final org.junit.runner.Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    //evaluate前执行方法相当于@Before
                    String methodName = description.getMethodName();//获取测试方法的名字
                    System.out.println(methodName + "方法测试开始！");

                    base.evaluate();//运行的测试方法

                    //evaluate后执行方法相当于@After
                    System.out.println(methodName + "方法测试结束！");
                }
            };
        }
    }

}
