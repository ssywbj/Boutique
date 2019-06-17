package com.suheng.ssy.boutique.oom;

import android.content.Context;
import android.view.View;

public class MemoryTon {
    /*
     1.在Android开发中，最容易引发的内存泄漏问题的是Context， 比如Activity的Context，
     就包含大量的内存引用，一旦泄漏了Context，也意味泄漏它指向的所有对象。

     1a.如在类中定义静态Activity变量，把当前运行的Activity实例赋值于这个静态变量，如果这个静态变量在
     Activity生命周期结束后没有清空，就导致内存泄漏。因为static变量是贯穿这个应用的生命周期的，
     所以被泄漏的Activity就会一直存在于应用的进程中，不会被垃圾回收器回收。
     */
    static MemoryLeakageActivity sMemoryLeakageActivity;//在类中定义静态的Activity，这种代码要避免
    static View sView;//1c.同理，静态的View也是不建议的

    private static MemoryTon sMemoryTon;
    private Context mContext;

    private MemoryTon(Context context) {
        this.mContext = context;
    }

    public static MemoryTon getInstance(Context context) {
        if (sMemoryTon == null) {
            /*
            1b.单例中保存Activity
            在单例模式中，如果Activity经常被用到，那么在内存中保存一个Activity实例是很实用的。但是由于单例的
            生命周期是应用程序的生命周期，这样会强制延长Activity的生命周期，这是相当危险而且不必要的，无论如
            何都不能在单例中保存类似Activity的对象。

            如在调用Singleton的getInstance()方法时传入了Activity。那么当instance没有释放时，
            这个Activity会一直存在。因此造成内存泄露。

            解决方法:
            可以将new MemoryTon(context)改为new MemoryTon(context.getApplicationContext())即可，
            这样便和传入的Activity没关系了。
             */
            //sMemoryTon = new MemoryTon(context);
            sMemoryTon = new MemoryTon(context.getApplicationContext());
        }
        return sMemoryTon;
    }
}
