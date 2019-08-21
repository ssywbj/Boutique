package com.suheng.java.thread.producer.v1;

public class ThreadDemo {

    public static void main(String[] args) {
        Person person = new Person();
        Producer producer = new Producer(person);
        Consumer consumer = new Consumer(person);
        new Thread(producer).start();
        new Thread(consumer).start();
        /*
         * 以上程序存在两个问题：
         * 1.Person对象的内容匹配错误，即"Sushiying"的性别应该是"girl"，有时却变成了"boy"；
         * 2.Person的内容重复出现，理应是生产一次就取走一次。
         *
         * 原因：
         * 问题1：生产者线程Person对象的姓名属性赋值后还没有来得及向该对象的性别属性赋值（因为两个赋值操作之间加入延迟操作），
         * 程序就切换到了消费者线程，消费者会将Person对象的姓名和上一个Person对象的性别联系在一起，所以产生了性别错乱问题；
         * 问题2：生产者放了若干次的数据，消费者才开始取数据，或者是消费者取完一个数据后还没有等到生产者放入新的数据，又重复取出已取过的数据。
         */
    }

}
