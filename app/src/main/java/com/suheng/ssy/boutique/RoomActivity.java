package com.suheng.ssy.boutique;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.suheng.ssy.boutique.db.AppDatabase;
import com.suheng.ssy.boutique.db.UserDao;
import com.suheng.ssy.boutique.db.UserEntity;

import java.util.ArrayList;
import java.util.List;

/*import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;*/

public class RoomActivity extends BasicActivity {

    private UserDao mUserDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "UserDemo").build();
        mUserDao = database.userDao();
    }

    public void onClickInsertOne(View view) {
        /*Observable.just(new UserEntity(1210214142, "Weeee", "Baaaajie"))
                .subscribeOn(Schedulers.io())
                .map(new Func1<UserEntity, Long>() {
                    @Override
                    public Long call(UserEntity userEntity) {
                        Long userId = mUserDao.insert(userEntity);
                        Log.d(mTag, "InsertOne, map: " + userId + ", " + Thread.currentThread().getName());
                        return userId;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.d(mTag, "InsertOne, subscribe: " + Thread.currentThread().getName());
                    }
                });*/
    }

    public void onClickInsertList(View view) {
        List<UserEntity> userEntityList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            userEntityList.add(new UserEntity(1210214142 + i, "Wiii" + i, "Bangjeee" + i));
        }
        /*Observable.from(userEntityList)
                .subscribeOn(Schedulers.io())
                .map(new Func1<UserEntity, Long>() {
                    @Override
                    public Long call(UserEntity userEntity) {
                        Long userId = mUserDao.insert(userEntity);
                        Log.d(mTag, "InsertList1, map: " + userId + ", " + Thread.currentThread().getName());
                        return userId;
                    }
                })
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long userId) {
                        Log.d(mTag, "InsertList1, subscribe: " + Thread.currentThread().getName() + ", " + userId);
                    }
                });

        Observable
                .create(new Observable.OnSubscribe<List<UserEntity>>() {
                    @Override
                    public void call(Subscriber<? super List<UserEntity>> subscriber) {
                        List<UserEntity> entityList = new ArrayList<>();
                        for (int i = 0; i < 2; i++) {
                            entityList.add(new UserEntity(221021442 + i, "Wccc" + i, "Bhhhhhh" + i));
                        }
                        subscriber.onNext(entityList);
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<UserEntity>, List<Long>>() {
                    @Override
                    public List<Long> call(List<UserEntity> userEntityList) {
                        return mUserDao.insert(userEntityList);
                    }
                })
                .filter(new Func1<List<Long>, Boolean>() {
                    @Override
                    public Boolean call(List<Long> longs) {
                        return (longs != null);
                    }
                })
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> longs) {
                        for (Long userId : longs) {
                            Log.i(mTag, "InsertList2, thread: " + Thread.currentThread().getName() + ", " + userId);
                        }
                    }
                });*/
    }

    public void onClickQueryOne(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserEntity userEntity = mUserDao.getUserById(1210214142);
                Log.d(mTag, "onClickQueryOne: " + userEntity);
            }
        }).start();
    }

    public void onClickQueryList(View view) {
        /*Observable
                .create(new Observable.OnSubscribe<List<UserEntity>>() {
                    @Override
                    public void call(Subscriber<? super List<UserEntity>> subscriber) {
                        Log.d(mTag, "QueryList, create: " + Thread.currentThread().getName());
                        subscriber.onNext(mUserDao.getUserList());
                    }
                })
                .filter(new Func1<List<UserEntity>, Boolean>() {
                    @Override
                    public Boolean call(List<UserEntity> userEntityList) {
                        Log.d(mTag, "QueryList, filter: " + Thread.currentThread().getName());
                        return (userEntityList != null);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<UserEntity>>() {
                    @Override
                    public void call(List<UserEntity> userEntityList) {
                        for (UserEntity userEntity : userEntityList) {
                            Log.d(mTag, "QueryList, thread: " + Thread.currentThread().getName() + ", " + userEntity);
                        }
                    }
                });*/
    }

    public void onClickQueryUsers(View view) {
        /*Observable
                .create(new Observable.OnSubscribe<long[]>() {
                    @Override
                    public void call(Subscriber<? super long[]> subscriber) {
                        subscriber.onNext(new long[]{1210214144, 221021443, 321014142});
                    }
                })
                .map(new Func1<long[], List<UserEntity>>() {
                    @Override
                    public List<UserEntity> call(long[] longs) {
                        return mUserDao.getUserList(longs);
                    }
                })
                .filter(new Func1<List<UserEntity>, Boolean>() {
                    @Override
                    public Boolean call(List<UserEntity> userEntities) {
                        return (userEntities != null);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<List<UserEntity>>() {
                    @Override
                    public void call(List<UserEntity> userEntities) {
                        for (UserEntity userEntity : userEntities) {
                            Log.d(mTag, "Query Users, thread: " + Thread.currentThread().getName() + ", " + userEntity);
                        }
                    }
                });*/
    }

    public void onClickUpdateOne(View view) {

    }

    public void onClickUpdateList(View view) {
        /*Observable
                .create(new Observable.OnSubscribe<List<UserEntity>>() {
                    @Override
                    public void call(Subscriber<? super List<UserEntity>> subscriber) {
                        List<UserEntity> entityList = new ArrayList<>();
                        for (int i = 0; i < 4; i++) {
                            entityList.add(new UserEntity(221021442 + i, "hhhhhh" + i, "jjjjjjjjj" + i));
                        }
                        subscriber.onNext(entityList);
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<UserEntity>, Integer>() {
                    @Override
                    public Integer call(List<UserEntity> userEntities) {
                        return mUserDao.update(userEntities);
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(mTag, "UpdateList : " + integer);
                    }
                });*/
    }

    public void onClickDeleteOne(View view) {
        /*Observable.just(new UserEntity(1210214142, "Weeee", "Baaaajie"), new UserEntity(33, "hggh", "uuuuu"))
                .subscribeOn(Schedulers.io())
                .map(new Func1<UserEntity, Integer>() {
                    @Override
                    public Integer call(UserEntity userEntity) {
                        return mUserDao.delete(userEntity);
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(mTag, "DeleteOne : " + integer);
                    }
                });*/
    }

    public void onClickDeleteList(View view) {
        /*Observable
                .create(new Observable.OnSubscribe<List<UserEntity>>() {
                    @Override
                    public void call(Subscriber<? super List<UserEntity>> subscriber) {
                        List<UserEntity> entityList = new ArrayList<>();
                        for (int i = 0; i < 6; i++) {
                            entityList.add(new UserEntity(221021442 + i, "", ""));
                        }
                        subscriber.onNext(entityList);
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<UserEntity>, Integer>() {
                    @Override
                    public Integer call(List<UserEntity> userEntities) {
                        return mUserDao.delete(userEntities);
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(mTag, "DeleteList : " + integer);
                    }
                });*/
    }

}
