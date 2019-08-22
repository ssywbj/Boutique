package com.suheng.java.thread.synchronize.apply;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SyncException {

    private final List<Integer> mIntegers = new ArrayList<>();

    SyncException() {
        for (int i = 0; i < 20; i++) {
            mIntegers.add(i);
        }
    }

    String listString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer integer : mIntegers) {
            stringBuilder.append(integer.intValue()).append(" ");

        }
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    void listRemove() {//https://www.cnblogs.com/snowater/p/8024776.html
        for (Integer integer : mIntegers) {
            if (integer == 1 || integer == 4) {
                mIntegers.remove(integer);//java.util.ConcurrentModificationException
            }
        }
    }

    void iteratorRemove() {
        Iterator<Integer> iterator = mIntegers.iterator();
        Integer integer;
        while (iterator.hasNext()) {
            integer = iterator.next();
            if (integer == 1 || integer == 4) {
                iterator.remove();
            }
        }
    }

    void threadUnsafe() {
        Thread threadForeach = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Integer integer : mIntegers) {
                    System.out.println("thread foreach, integer = " + integer);
                    /*try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        });

        Thread threadRemove = new Thread(new Runnable() {
            @Override
            public void run() {
                Iterator<Integer> iterator = mIntegers.iterator();
                while (iterator.hasNext()) {
                    Integer next = iterator.next();
                    if (next == 2 || next == 8) {
                        iterator.remove();
                    }
                }

                System.out.println("thread remove, " + listString());
            }
        });

        threadForeach.start();
        threadRemove.start();
    }

    private void threadSafe() {
        Thread threadForeach = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (mIntegers) {//加入同步
                    for (Integer integer : mIntegers) {
                        System.out.println("thread foreach, integer = " + integer);
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        Thread threadRemove = new Thread(new Runnable() {
            @Override
            public void run() {
                //syncRemove();
                synchronized (mIntegers) {
                    Iterator<Integer> iterator = mIntegers.iterator();
                    while (iterator.hasNext()) {
                        Integer next = iterator.next();
                        if (next == 2 || next == 8) {
                            iterator.remove();
                        }
                    }

                    System.out.println("thread remove, " + listString());
                }
            }
        });

        threadForeach.start();
        threadRemove.start();
    }

    private synchronized void syncRemove() {
        Iterator<Integer> iterator = mIntegers.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            if (next == 2 || next == 8) {
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) {
        SyncException syncException = new SyncException();
        syncException.threadUnsafe();
        //syncException.threadSafe();
    }

}
