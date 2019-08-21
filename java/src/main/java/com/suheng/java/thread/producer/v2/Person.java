package com.suheng.java.thread.producer.v2;

public class Person {

    //给变量赋初始值是为了防止第一次执行时若是消费者先抢到CPU资源而先运行，导致内容没有赋值而为空的情况。
    private String name = "Sushiying";
    private String sex = "girl";

    /**
     * 解决问题1：加入synchronized关键字，让设置姓名和性别的操作在同一个同步方法中完成。
     */
    synchronized void set(String name, String sex) {
        setName(name);
        //加入延迟操作是为了让问题更好地呈现出来
        try {
            Thread.sleep(130);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setSex(sex);
    }

    /**
     * 解决问题1：加入synchronized关键字让取出姓名和内容的操作在同一个同步方法中完成。
     */
    synchronized void get() {
        //加入延迟操作是为了让问题更好地呈现出来
        try {
            Thread.sleep(130);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(toString());
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", sex=" + sex + "]";
    }

}
