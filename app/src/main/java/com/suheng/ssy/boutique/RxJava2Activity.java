package com.suheng.ssy.boutique;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class RxJava2Activity extends BasicActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java2);

        /*Observer<String> observer = new Observer<String>() {
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

        Observable.just(9, 36).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return String.valueOf(Math.sqrt(integer));
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(mTag, "事件一对一变换map, call: " + s + ", " + Thread.currentThread().getName());
            }
        });

        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(Arrays.asList(new Course("111"), new Course("222"))));
        studentList.add(new Student(Arrays.asList(new Course("333"), new Course("444"))));
        studentList.add(new Student(Arrays.asList(new Course("555"), new Course("666"))));
        studentList.add(new Student(Arrays.asList(new Course("777"), new Course("888"))));
        Observable.from(studentList).subscribe(new Subscriber<Student>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Student student) {
                List<Course> courses = student.getCourseList();
                for (int i = 0; i < courses.size(); i++) {
                    Course course = courses.get(i);
                    Log.d(mTag, "用循环一对多变换：" + course.toString());
                }
            }
        });
        Observable.from(studentList).flatMap(new Func1<Student, Observable<Course>>() {
            @Override
            public Observable<Course> call(Student student) {
                return Observable.from(student.getCourseList());
            }
        }).subscribe(new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Course course) {
                Log.d(mTag, "用flatMap一对多变换：" + course.toString());
            }
        });
        Observable.from(studentList)
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        Log.d(mTag, "用flatMap一对多变换，call1，线程：" + Thread.currentThread().getName());
                        return Observable.from(student.courseList);
                    }
                })
                .flatMap(new Func1<Course, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Course course) {
                        Log.d(mTag, "用flatMap一对多变换，call2，线程：" + Thread.currentThread().getName());
                        return Observable.from(course.getScore());
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(mTag, "用flatMap一对多变换，解决多层代码嵌套：" + integer + ", call3，线程：" + Thread.currentThread().getName());
                    }
                });

        Observable.from(studentList)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        Log.w(mTag, "用flatMap一对多变换，call1，线程：" + Thread.currentThread().getName());
                        return Observable.from(student.courseList);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Course, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Course course) {
                        Log.w(mTag, "用flatMap一对多变换，call2，线程：" + Thread.currentThread().getName());
                        return Observable.from(course.getScore());
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.w(mTag, "用flatMap一对多变换，解决多层代码嵌套：" + integer + ", call3，线程：" + Thread.currentThread().getName());
                    }
                });*/

        Observable.just(1, 2, 4, 6, 8, 9)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 3;
                    }
                })
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer < 9;
                    }
                })
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return String.valueOf(integer * integer);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String result) {
                        Log.w(mTag, "filter if ：" + result);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.w(mTag, "filter else ：" + throwable);
                    }
                });
    }

    class Student {
        List<Course> courseList;

        public Student(List<Course> courseList) {
            this.courseList = courseList;
        }

        public List<Course> getCourseList() {
            return courseList;
        }
    }

    class Course {
        String name;

        public Course(String name) {
            this.name = name;
        }

        public Integer[] getScore() {
            return new Integer[]{new Random().nextInt(3), new Random().nextInt(110)};
        }

        @Override
        public String toString() {
            return "Course{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
