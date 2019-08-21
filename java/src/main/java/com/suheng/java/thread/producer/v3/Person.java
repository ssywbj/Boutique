package com.suheng.java.thread.producer.v3;

public class Person {

    //给变量赋初始值是为了防止第一次执行时若是消费者先抢到CPU资源而先运行，导致内容没有赋值而为空的情况。
    private String name = "Sushiying";
    private String sex = "girl";
    private boolean flag;

    /**
     * 解决问题1：加入synchronized关键字，让设置姓名和性别的操作在同一个同步方法中完成。
     */
    synchronized void set(String name, String sex) {
        /*
         * 解决问题2：加入Object类的唤醒（notify）与等待(wait)机制。
         */
        if (!flag) {//如果为false，说明已经执行过一次set方法，即生产者线程生产过一次信息
            try {
                super.wait();//此时如果生产者线程再执行到set方法，就需要等待————等待消费者线程把对象取走，即把flag设置为true才能继续生产。
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        setName(name);
        //加入延迟操作是为了让问题更好地呈现出来
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setSex(sex);

        flag = false;//生产完信息后，标示位置为false

        super.notify();//此时唤醒正在等待的线程（消费者线程）取走消息
    }

    /**
     * 解决问题1：加入synchronized关键字让取出姓名和内容的操作在同一个同步方法中完成。
     */
    synchronized void get() {
        /*
         * 解决问题2：加入Object类的唤醒（notify）与等待(wait)机制————与生产者等待与唤醒的原理相同。
         */
        if (flag) {
            try {
                super.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //加入延迟操作是为了让问题更好地呈现出来
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(toString());

        flag = true;

        super.notify();
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
