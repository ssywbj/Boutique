package com.suheng.ssy.boutique.dagger.ab;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wbj on 2018/12/6.
 */
@Module(includes = BModule.class)
public class MainModule {

    /*@Provides
    B providerB() {
        return new B();
    }*/

    @Provides
    A providerA() {
        return new A(/*this.providerB()*/);
    }

    /*@Provides
    A providerA(B b) {
        //return new A(); 使用dagger框架后，如果此时改了A的构造方法，我们只需要在这里改一下A的实例化方法即可
        return new A(b);
    }*/

    //或这样实例化
    /*@Provides
    A providerA() {
        return new A(new B());
    }*/

}
