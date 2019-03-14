package com.suheng.ssy.boutique;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class RxJava2Activity extends BasicActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java2);

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onNext(String s) {
                Log.d(mTag, "observer, onNext: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(mTag, "observer, onCompleted==");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(mTag, "observer, onError: " + e);
            }
        };

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Log.d(mTag, "subscriber, onNext: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(mTag, "subscriber, onCompleted---");
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

            }
        };

        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {

            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        };

        observable2.subscribe(onNextAction);
        observable2.subscribe(onNextAction, onErrorAction);
        observable2.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }

}
