package com.example.wbj;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxJava2Activity extends AppCompatActivity {
    public static final String TAG = RxJava2Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rxjava2_aty);
        this.initRxJava2();
    }

    public void initRxJava2() {
        /*******************************分布调用 begin*********************************/
        /*Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                Log.d(TAG, "observable, subscribe: " + Thread.currentThread().getName());
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        });//创建被观察者

        Observer observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "observer, onSubscribe: " + Thread.currentThread().getName());
            }

            @Override
            public void onNext(Integer data) {
                Log.d(TAG, "observer, onNext: " + data + ", " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "observer, onError: " + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "observer, onComplete: " + Thread.currentThread().getName());
            }
        };//创建观察者

        observable.subscribe(observer);//订阅*/
        /*******************************分布调用 end*********************************/

        /*Observable.create(new ObservableOnSubscribe<Integer>() {//链式调用
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Integer integer) {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });*/

        //just方法用于创建被观察者并发送事件，事件的数量不可以超过10个
        /*Observable.just(2, 4, 6).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "---just, onSubscribe: " + Thread.currentThread().getName());
            }

            @Override
            public void onNext(Integer data) {
                Log.d(TAG, "---just, onNext: " + data + ", " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "---just, onError: " + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "---just, onComplete: " + Thread.currentThread().getName());
            }
        });*/

        //fromArray和just方法类似，不同的是它传入的是可变参数，所以可以传入数组
        Observable.fromArray(new Integer[]{5, 4, 7}).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "****fromArray, onSubscribe: " + Thread.currentThread().getName());
            }

            @Override
            public void onNext(Integer data) {
                Log.d(TAG, "****fromArray, onNext: " + data + ", " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "****fromArray, onError: " + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "****fromArray, onComplete: " + Thread.currentThread().getName());
            }
        });

        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                Bitmap value = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_right);
                emitter.onNext(value);
                emitter.onComplete();
                Log.e(TAG, "========Bitmap value: " + value + ", " + Thread.currentThread().getName());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "=========onSubscribe, Bitmap value:" + ", " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        Log.e(TAG, "==========onNext, Bitmap value: " + bitmap + ", " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "========onError, Bitmap value:" + ", " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "========onComplete, Bitmap value:" + ", " + Thread.currentThread().getName());
                    }
                });

        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                Bitmap value = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_right);
                emitter.onNext(value);
                emitter.onComplete();
                Log.e(TAG, "--------subscribe value: " + value + ", " + Thread.currentThread().getName());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        Log.e(TAG, "---------accept value: " + bitmap + ", " + Thread.currentThread().getName());
                    }
                });
    }

}
