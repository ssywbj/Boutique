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

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
        Observable.just(new UserEntity(1210214142, "Wiii", "Bangjie"))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<UserEntity, Long>() {
                    @Override
                    public Long call(UserEntity userEntity) {
                        Long userId = mUserDao.insert(userEntity);
                        Log.d(mTag, "InsertOne: " + userId);
                        return userId;
                    }
                })
                .subscribe();
    }

    public void onClickInsertList(View view) {
        final List<UserEntity> userEntityList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userEntityList.add(new UserEntity(1210214142 + i, "Wiii" + i, "Bangjeee" + i));
        }
        Observable.from(userEntityList)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<UserEntity, List<Long>>() {
                    @Override
                    public List<Long> call(UserEntity userEntity) {
                        List<Long> userIdList = mUserDao.insert(userEntityList);
                        for (Long userId : userIdList) {
                            Log.d(mTag, "InsertList: " + userId);
                        }
                        return userIdList;
                    }
                })
                .subscribe();
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

    }

    public void onClickQueryUsers(View view) {

    }

    public void onClickUpdateOne(View view) {

    }

    public void onClickUpdateList(View view) {

    }

    public void onClickDeleteOne(View view) {

    }

    public void onClickDeleteList(View view) {

    }

}
