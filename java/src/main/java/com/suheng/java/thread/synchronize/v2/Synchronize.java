package com.suheng.java.thread.synchronize.v2;

/**
 * 解决资源同步的问题：可以使用synchronized关键字声明同步代码块或方法这两种方式完成。
 * 同步：多个线程操作在同一个时间内只能有线程一个在进行，其它线程要等待此线程完成之后才可以继续执行。
 */
public class Synchronize implements Runnable {

    private int ticket = 5;

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            //第一种同步方式：加入同步代码块————使用同步代码块时，必须指定一个需要同步的对象，一般将当前对象(this)设置成同步对象。
            /*synchronized (this) {
                if (ticket > 0) {
                    //加入延迟操作是为了让问题更好地呈现出来
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getName() + "卖票：ticket = " + (ticket--));
                }
            }*/

            this.sale();//第二种同步方式：使用同步方法
        }
    }

    private synchronized void sale() {
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

    public static void main(String[] args) {
        Synchronize rDemo = new Synchronize();
        new Thread(rDemo, "A").start();
        new Thread(rDemo, "B").start();
        new Thread(rDemo, "C").start();
    }

}
