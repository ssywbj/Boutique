package com.suheng.java.thread.producer.v3;

public class ThreadDemo {

    public static void main(String[] args) {
        Person person = new Person();
        Producer producer = new Producer(person);
        Consumer consumer = new Consumer(person);
        new Thread(producer).start();
        new Thread(consumer).start();
    }

}
