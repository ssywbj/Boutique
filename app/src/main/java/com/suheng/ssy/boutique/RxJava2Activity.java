package com.suheng.ssy.boutique;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RxJava2Activity extends BasicActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java2);

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onNext(String s) {
                Log.d(mTag, "observer, onNext: " + s + ", " + Thread.currentThread().getName());
            }

            @Override
            public void onCompleted() {
                Log.d(mTag, "observer, onCompleted====" + ", " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(mTag, "observer, onError: " + e + ", " + Thread.currentThread().getName());
            }
        };

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Log.d(mTag, "subscriber, onNext: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(mTag, "subscriber, onCompleted----");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(mTag, "subscriber, onError: " + e);
            }
        };

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });

        Observable<String> observable1 = Observable.just("Hello", "Hi", "Aloha");
        Observable<String> observable2 = Observable.from(new String[]{"Hello", "Hi", "Aloha"});

        observable.subscribe(observer);
        observable1.subscribe(subscriber);

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(mTag, "onNextAction, call: " + s + ", " + Thread.currentThread().getName());
            }
        };

        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Log.d(mTag, "onCompletedAction, call::::");
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d(mTag, "onErrorAction::::");
            }
        };

        observable2.subscribe(onNextAction);//不完整定义的回调：只订阅onNext方法
        observable2.subscribe(onNextAction, onErrorAction);//不完整定义的回调
        observable2.subscribe(onNextAction, onErrorAction, onCompletedAction);

        String[] name = {"ffffff", "dddddd"};
        Observable.from(name).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(mTag, "chain invoke, call: " + s);
            }
        });

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("CCCCCC");
                Log.d(mTag, "chain invoke, CCCCCC: " + Thread.currentThread().getName());
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(mTag, "chain invoke, onCompleted: " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(mTag, "chain invoke, onError: " + e + ", " + Thread.currentThread().getName());
            }

            @Override
            public void onNext(String s) {
                Log.d(mTag, "chain invoke, onNext: " + s + ", " + Thread.currentThread().getName());
            }
        });

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("BBBBBB");
                Log.d(mTag, "chain invoke, BBBBBB: " + Thread.currentThread().getName());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()) //指定subscribe()发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) //指定Subscriber的回调发生在主线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(mTag, "chain invoke, onCompleted: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(mTag, "chain invoke, onError: " + e + ", " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(mTag, "chain invoke, onNext: " + s + ", " + Thread.currentThread().getName());
                    }
                });
    }

}
