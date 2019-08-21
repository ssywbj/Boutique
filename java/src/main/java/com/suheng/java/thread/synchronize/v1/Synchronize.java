package com.suheng.java.thread.synchronize.v1;

/**
 * 问题：如果一个线程通过Runnable接口实现，则意味着类中的属性被多个线程共享，那么操作同一资源时可能会出现资源同步的问题。
 */
public class Synchronize implements Runnable {

    private int ticket = 5;

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            if (ticket > 0) {
                //加入延迟操作是为了让问题更好地呈现出来
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "卖票：ticket = " + (ticket--));
            }
        }
    }

    public static void main(String[] args) {
        Synchronize synchronize = new Synchronize();
        new Thread(synchronize, "A").start();
        new Thread(synchronize, "B").start();
        new Thread(synchronize, "C").start();
        //因为加入延迟操作，那么有可能在一个线程还未对票数进行减操作之前，其它线程就已经将票数减少，所以有可能出现票数为负数的情况
    }

}
