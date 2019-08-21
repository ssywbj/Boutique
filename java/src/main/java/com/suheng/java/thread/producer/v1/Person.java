package com.suheng.java.thread.producer.v1;

public class Person {

    //给变量赋初始值是为了防止第一次执行时若是消费者先抢到CPU资源而先运行，导致内容没有赋值而为空的情况。
    private String name = "Sushiying";
    private String sex = "girl";

    void setName(String name) {
        this.name = name;
    }

    void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", sex=" + sex + "]";
    }

}
