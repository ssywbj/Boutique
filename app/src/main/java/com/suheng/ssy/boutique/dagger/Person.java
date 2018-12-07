package com.suheng.ssy.boutique.dagger;

import javax.inject.Inject;

/**
 * Created by wbj on 2018/12/7.
 */
public class Person {

    String name;

    @Inject
    public Person() {
        name = "nameSSSSS";
    }

    /**
     * 用@Inject只能标记一个构造方法，如果我们标记两个构造方法，编译的时候就会报错，因为不知道到底要用哪一个构造提供实例。
     * 用@Inject标记的构造函数如果有参数，那么这个参数也需要其它地方提供依赖，但是@Inject还有一个缺陷，
     * 就是对于第三方的类无能为力，因为我们不能修改第三方的构造函数。
     * 所以对于String还有其他的一些我们不能修改的类，只能用@Module中的@Provides来提供实例了。
     * （以下带参的构造函数在编辑会报错）
     */
    /*@Inject
    public Person(String name) {
        this.name = name;
    }*/

    public String getName() {
        return name;
    }
}
