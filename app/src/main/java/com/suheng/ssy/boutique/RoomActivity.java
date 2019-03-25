package com.suheng.ssy.boutique;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.suheng.ssy.boutique.db.AppDatabase;
import com.suheng.ssy.boutique.db.UserDao;

public class RoomActivity extends BasicActivity {

    private UserDao mUserDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "UserDemo").build();
        mUserDao = database.userDao();
    }

}
