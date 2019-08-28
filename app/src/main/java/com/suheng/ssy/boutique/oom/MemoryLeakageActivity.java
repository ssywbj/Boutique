package com.suheng.ssy.boutique.oom;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.suheng.ssy.boutique.BasicActivity;
import com.suheng.ssy.boutique.R;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MemoryLeakageActivity extends BasicActivity {
    private int mMemoryRemain = 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory_leakage_aty);
        /*new InnerOne();
        new InnerStatic(this);

        this.test();
        testStatic();
        this.testStaticRunnable();*/

        //mHandler.sendEmptyMessage(-2);
        //mHandlerStatic.sendEmptyMessage(-1);

        //this.communicationBetweenThreads();
        this.createThreadPool();
    }

    private void createThreadPool() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 5, 1, TimeUnit.SECONDS
                , new LinkedBlockingDeque<Runnable>(100));

        for (int i = 0; i < 7; i++) {
            final int index = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    Log.w(mTag, Thread.currentThread().getName() + ", thread pool: index = " + index);
                }
            };

            threadPoolExecutor.execute(runnable);
        }

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    Log.w(mTag, Thread.currentThread().getName() + ", i = " + i);
                    SystemClock.sleep(500);
                }
            }
        });

        //FixedThreadPool: 可重用固定线程数，只有核心线程，无非核心线程，并且阻塞队列无界
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);//4：核心线程数
        //CachedThreadPool：按需创建，没有核心线程，只有非核心线程，并且每个非核心线程空闲等待的时间为60s，采用SynchronousQueue队列
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        //SingleThreadPool：单个核线的fixed，
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        //ScheduledThreadPool：定时延时执行
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
    }

    /*
     1d.Inner Classes
     内部类的优势可以提高可读性和封装性，而且可以访问外部类，不幸的是，导致内存泄漏的原因，
     就是内部类持有外部类实例的强引用。 例如在内部类中持有Activity对象，如InnerOne类

     解决方法（如InnerStatic类）：
     1.将内部类变成静态内部类;
     2.如果有强引用Activity中的属性，则将该属性的引用方式改为弱引用;
     3.在业务允许的情况下，当Activity执行onDestroy时，结束这些耗时任务;
     */
    private final class InnerOne {
        InnerOne() {
            Log.d(mTag, "memory remain: " + mMemoryRemain);
        }
    }

    private static final class InnerStatic {
        WeakReference<MemoryLeakageActivity> mWeakReference;

        InnerStatic(MemoryLeakageActivity activity) {
            mWeakReference = new WeakReference<>(activity);
            Log.d(mWeakReference.get().mTag, "static class, memory remain: " + mWeakReference.get().mMemoryRemain);
        }
    }

    /*
    匿名类也维护了外部类的引用。当你在匿名类中执行耗时任务，如果用户退出，
    会导致匿名类持有的Activity实例就不会被垃圾回收器回收，直到异步任务结束。

    类似的还有其他的匿名类实例，如TimerTask、Threads等执行耗时任务时持有Activity的引用，都可能导致内存泄漏
     */
    public void test() { //这儿发生泄漏
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {//为true，线程会一直运行，哪怕Activity退出onDestroy后，线程还会在后台跑，除非强杀应用
                    try {
                        Thread.sleep(2000);
                        Log.d(mTag, "anonymous class: " + mMemoryRemain);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void testStatic() {//加上static，变成静态匿名内部类
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                        //Log.d(mTag, "static anonymous class: " + mMemoryRemain);//静态方法中无法访问非静态成员变量和方法
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void testStaticRunnable() {//匿名内部类变成显式内部类，通过软引用的方式访问非静态成员变量和方法
        new Thread(new RunnableStatic(this)).start();
    }

    private boolean mIsRunningThread = true;

    static class RunnableStatic implements Runnable {

        WeakReference<MemoryLeakageActivity> mWeakReference;

        RunnableStatic(MemoryLeakageActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            if (mWeakReference.get() == null) {
                return;
            }
            while (mWeakReference.get().mIsRunningThread) {//加一个控制线程运行的变量，在Activity退出时置为false，让线程停止运行
                try {
                    Thread.sleep(2000);
                    Log.d(mWeakReference.get().mTag, "static inner runnable class: " + mWeakReference.get().mMemoryRemain);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
    1e.Handler
    handler中，Runnable内部类会持有外部类的隐式引用，被传递到Handler的消息队列MessageQueue中，
    在Message消息没有被处理之前，Activity实例不会被销毁了，于是导致内存泄漏。（如对象mHandler）
    解决方法（如HandlerStatic对象）：
    1.可以把Handler类放在单独的类文件中，或者使用静态内部类便可以避免泄露;
    2.如果想在Handler内部去调用所在的Activity,那么可以在handler内部使用弱引用的方式去指向所在Activity.
    使用Static + WeakReference的方式来达到断开Handler与Activity之间存在引用关系的目的.
    3.在界面销毁是，释放handler资源
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(mTag, "anonymous Handler class: " + mMemoryRemain);
            sendEmptyMessageDelayed(-2, 1000);
        }
    };

    HandlerStatic mHandlerStatic = new HandlerStatic(this);

    static class HandlerStatic extends Handler {
        WeakReference<MemoryLeakageActivity> mWeakReference;

        HandlerStatic(MemoryLeakageActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWeakReference.get() != null) {
                Log.w(mWeakReference.get().mTag, "static Handler class: " + mWeakReference.get().mMemoryRemain);
                sendEmptyMessageDelayed(-1, 1000);
            }
        }
    }

    /*
    1f.总结：要解决Activity的长期持有造成的内存泄漏，可以通过以下方法：
    1.传入Application的Context，因为Application的生命周期就是整个应用的生命周期，所以这没有任何问题。
    2.如果此时传入的是Activity的Context，当这个Context所对应的Activity退出时，主动结束执行的任务并释放Activity资源。
    3.将线程的内部类，改为静态内部类。
    因为非静态内部类会自动持有一个所属类的实例，如果所属类的实例已经结束生命周期，但内部类的方法仍在执行，
    就会hold其主体（引用），使主体不能被释放，亦即内存泄露。静态类编译后和非内部类是一样的，有自己独立的类名，
    不会悄悄引用所属类的实例，所以就不容易泄露。
    4.如果需要引用Activity，使用弱引用。
    5.谨慎对context使用static关键字。
    */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsRunningThread = false;

        if (mHandlerStatic != null) {
            //也可以使用removeCallbacks或mHandler.removeMessages来移除指定的Runnable和Message
            mHandlerStatic.removeCallbacksAndMessages(null);
        }

        /*
        2.Bitmap没调用recycle()
        Bitmap对象在不使用时，应该调用recycle()释放内存，然后才设置为null.
        */

        /*
        3.集合中对象没清理造成的内存泄露
        我们通常把一些对象的引用加入到了集合中，当我们不需要该对象时，并没有把它的引用从集合中清理掉，
        这样这个集合就会越来越大。如果这个集合是static的话，那情况就更严重了。
        解决方案：
        在Activity退出之前，将集合里的东西clear，然后置为null，再退出程序。
         */

        /*
        4.注册没取消造成的内存泄露
        这种Android的内存泄露比纯Java的内存泄漏还要严重，因为其他一些Android程序可能引用系统的Android程序的
        对象（比如注册机制）。即使Android程序已经结束了，但是别的应用程序仍然还有对Android程序的某个对象的引用，
        泄漏的内存依然不能被垃圾回收。
        解决方案：
        1.使用ApplicationContext代替ActivityContext;
        2.在Activity执行onDestory时，调用反注册;
        */

        /*
        5.资源对象没关闭造成的内存泄露
        资源性对象比如（Cursor，File文件等）往往都用了一些缓冲，我们在不使用的时候，应该及时关闭它们，
        以便它们的缓冲及时回收内存。而不是等待GC来处理。
        */

        /*
        6.占用内存较多的对象(图片过大)造成内存溢出
        因为Bitmap占用的内存实在是太多了，特别是分辨率大的图片，如果要显示多张那问题就更显著了。
        Android分配给Bitmap的大小只有8M。
        解决方法：
        1.等比例缩小图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一

        2.对图片采用软引用，及时地进行recycle()操作
         SoftReference<Bitmap> bitmap = new SoftReference<Bitmap>(pBitmap);//软引用
        //回收操作
         if(bitmap != null) {
               if(bitmap.get() != null && !bitmap.get().isRecycled()){
                   bitmap.get().recycle();
                   bitmap = null;
               }
        }
        */

        /*
        7.WebView内存泄露（影响较大）
        用新的进程调起含有WebView的Activity,并且在该Activity的onDestroy()最后加上System.exit(0);杀死当前进程。
        */
    }

    private Handler mHandlerThreadA;
    private Handler mHandlerThreadB;

    //android子线程与主线程通信主要有四种方式：Handler、View.post(Runnable)、runOnUiThread(Runnable)、AsyncTask
    //android子线程与子线程通信方式：把looper绑定到子线程中并且创建一个handler
    private void communicationBetweenThreads() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*for (int i = 0; i < 10; i++) {
                    SystemClock.sleep(1000);
                    if (i == 5 || i == 8) {
                        if (mHandlerThreadB != null) {
                            mHandlerThreadB.sendEmptyMessage(-125);
                        }
                    }
                }*/

                Looper.prepare();
                mHandlerThreadA = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.d(mTag, Thread.currentThread().getName() + ", receive msg = " + msg);
                    }
                };
                Looper.loop();
            }
        }, "Thread-A").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                /*Looper.prepare();
                mHandlerThreadB = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.d(mTag, Thread.currentThread().getName() + ", receive msg = " + msg);
                    }
                };
                Looper.loop();*/

                for (int i = 0; i < 13; i++) {
                    SystemClock.sleep(500);
                    Log.d(mTag, Thread.currentThread().getName() + " run, i = " + i);
                    if (i == 5 || i == 9) {
                        if (mHandlerThreadA != null) {
                            Message msg = mHandlerThreadA.obtainMessage();
                            msg.what = i * 10;
                            mHandlerThreadA.sendMessage(msg);
                        }
                    }
                }
            }
        }, "Thread-B").start();
    }

    //android进程间通信：1.Bundle；2.ContentProvider;3.文件；4.AIDL;5.Socket
}
