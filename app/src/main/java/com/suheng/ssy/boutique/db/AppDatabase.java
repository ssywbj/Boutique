package com.suheng.ssy.boutique.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

//注解指定database的表映射实体数据以及版本等信息
@Database(entities = {UserEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    //RoomDatabase提供方法直接访问底层数据库，通过定义抽象方法返回具体Dao进行数据库增删改查等操作
    public abstract UserDao userDao();
}
