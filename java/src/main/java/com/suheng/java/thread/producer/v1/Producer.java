package com.suheng.java.thread.producer.v1;

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
                person.setName("Sushiying");
                //加入延迟操作是为了让问题更好地呈现出来
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                person.setSex("girl");
            } else {
                person.setName("Weibangjie");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                person.setSex("boy");
            }
            flag = !flag;
        }

    }

}
