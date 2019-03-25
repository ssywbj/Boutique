package com.suheng.ssy.boutique.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao//注解配置sql语句
public interface UserDao {

    //query
    @Query("SELECT * FROM user")
    List<UserEntity> getUserList();//查询user表所有的column

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<UserEntity> getUserList(long[] userIds);//根据条件查询，方法参数和注解的sql语句参数一一对应

    @Query("SELECT * FROM user WHERE uid IN (:userId)")
    UserEntity getUserById(long userId);

    @Query("SELECT * FROM user WHERE first_name LIKE :firstName AND last_name LIKE :lastName LIMIT 1")
    UserEntity getUserByName(String firstName, String lastName);

    //insert
    /*REPLACE表示如果已经有数据，那么就覆盖掉；数据的判断通过主键进行匹配，
    也就是uid，非整个user对象；返回Long表示插入条目的主键值（uid）*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(UserEntity userEntity);

    List<Long> insert(List<UserEntity> userEntityList);

    //update
    /*更新已有数据，根据主键（uid）匹配，而非整个user对象；返回类型int代表更新的条目数目，而非主键uid的值*/
    @Update()
    int update(UserEntity... userEntity);

    @Update()
    int update(List<UserEntity> userEntityList);

    //delete
    /*删除user数据，数据的匹配通过主键uid实现；返回int数据表示删除了多少条，非主键uid值。*/
    @Delete
    int delete(UserEntity... userEntity);

    @Delete
    int delete(List<UserEntity> userEntityList);
}
