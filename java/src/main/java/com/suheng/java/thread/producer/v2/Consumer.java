package com.suheng.java.thread.producer.v2;

public class Consumer implements Runnable {

    private Person person;

    Consumer(Person person) {
        this.person = person;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            person.get();
        }

    }

}
