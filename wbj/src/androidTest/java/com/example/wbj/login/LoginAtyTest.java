package com.example.wbj.login;

import android.os.Looper;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by wbj on 2018/1/19.
 */
@RunWith(AndroidJUnit4.class)
public class LoginAtyTest {
    private LoginAty mLoginAty = new LoginAty();

    @Test
    public void emptyNameTest() {//1.@Test;2.public;3.不能为参数
        mLoginAty.validate("", "");
    }

    @Test
    public void emptyPwdTest() {
        mLoginAty.validate("dddd", "");
    }

    @Test
    public void loginSuccessTest() {
        //Can't create handler inside thread that has not called Looper.prepare()
        /*new Thread(new Runnable() {
            @Override
            public void run() {*/
                Looper.prepare();
                mLoginAty.validate("Ssywbj", "123456");
                Looper.loop();
         /*   }
        }).start();*/

    }

    @Test
    public void loginFailTest() {
        mLoginAty.validate("Ssyw", "1234");
    }

    @Test
    public void loginProgressTest() {
    }

}
