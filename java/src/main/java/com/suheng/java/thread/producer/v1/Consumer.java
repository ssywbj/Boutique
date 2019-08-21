package com.suheng.java.thread.producer.v1;

public class Consumer implements Runnable {

    private Person person;

    Consumer(Person person) {
        this.person = person;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            //加入延迟操作是为了让问题更好地呈现出来
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("i = " + i + ", " + person);
        }

    }

}
