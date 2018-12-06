package com.suheng.ssy.boutique.dagger.coffee;

import javax.inject.Inject;

/**
 * Created by wbj on 2018/12/6.
 * 这是一个制作Coffee的例子
 * CoffeeMaker是对制作Coffee过程的一个封装，制作Coffee需要实现CoffeeMarker的makeCoffee方法
 */
public class CoffeeMachine {

    @Inject
    CoffeeMaker mCoffeeMaker;

    /*public CoffeeMachine() {
        mCoffeeMaker = new SimpleMaker();
    }*/

    /*
    CoffeeMachine()-->CoffeeMachine(Cooker) 还是需new
    SimpleMaker的业务升级导致CoffeeMachine的制作过程也要变动，这明显不合理。这是因为CoffeeMachine里的CoffeeMaker是自
    己new出来的,这种糟糕的实例引用的方式我们称之为硬初始化（Hard init），和硬编码（Hard coding）一样都是滋生糟糕代码的好方法，
    Hard init不仅增加了各个模块的耦合，还让单元测试变得更加困难。
     */
    //@Inject
    public CoffeeMachine(Cooker cooker) {
        mCoffeeMaker = new SimpleMaker(cooker);
    }

    /*
    CoffeeMachine(Cooker)-->CoffeeMachine(CoffeeMaker) 还是需new，而是方法注入
    依赖注入的由来：不要在需要依赖的类中通过new来创建依赖而是通过方法提供的参数注入进来，
    这样我们需要依赖的类和提供依赖的类的实现方法就分隔开了。
    依赖注入的3种常见形式：No.1 构造函数注入
     */
    @Inject
    public CoffeeMachine(CoffeeMaker coffeeMaker) {
        mCoffeeMaker = coffeeMaker;
    }

    /*
    依赖注入的3种常见形式：No.2 Setter注入
    public void setCoffeeMaker(CoffeeMaker coffeeMaker) {
        mCoffeeMaker = coffeeMaker;
    }*/

    /*
    依赖注入的3种常见形式：No.3 接口注入（还需实现某个自定义的接口）
    后记：而Dagger2的引入就是为了让这些手动的依赖注入变得简单起来，它通过注解来实现依赖注入。
    @Override
    public void injectMaker(CoffeeMaker coffeeMaker) {
        mCoffeeMaker = coffeeMaker;
    }*/

    public String makeCoffee() {
        return mCoffeeMaker.makeCoffee();
    }

}
