package com.suheng.java.thread.producer.v2;

public class Producer implements Runnable {

    private Person person;

    Producer(Person person) {
        this.person = person;
    }

    @Override
    public void run() {
        boolean flag = false;

        for (int i = 0; i < 50; i++) {
            if (flag) {
                person.set("Sushiying", "girl");
            } else {
                person.set("Weibangjie", "boy");
            }
            flag = !flag;
        }

    }

}
