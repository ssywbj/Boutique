package ying.jie.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class GsonUser {
    public int mExcludeInt = 11;
    public long dateTime = new Date().getTime();
    public int age;
    public String name;
    public String EmailAddress;//此属性的写法仅用于测试，实际开发并不推荐这样写

    //    @SerializedName("count_time")//如果SerializedName注解只是这么写，那么只有Json串中只有“count_time” 这一个字段能为countTime属性赋值，其它字段包括"countTime"都不能
    /*
    但如果SerializedName注解这么写，那么Json串中有“count_time”、“countTime”、“time_count” 这三个字段都能为countTime属性赋值，当这三个字段同时出时，以最后一个出现的值为准；
    虽然有三个字段能为countTime赋值，但如果把对象转换成Json串时，生成的Json串字段只能是注解中“value”的值，即"count_time"，而不"countTime"或"time_count"。
    //注：属性alternate接收的是一个String数组，Gson2.4版本才有alternate属性
     */
    @SerializedName(value = "count_time", alternate = {"countTime", "time_count"})
    public String countTime;

    public GsonUser(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public GsonUser(int age, String name, String emailAddress) {
        this(age, name);
        this.EmailAddress = emailAddress;
    }

    public GsonUser(int age, String name, String emailAddress, String countTime) {
        this(age, name, emailAddress);
        this.countTime = countTime;
    }

    @Override
    public String toString() {
        return "GsonUser{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", EmailAddress='" + EmailAddress + '\'' +
                ", countTime='" + countTime + '\'' +
                '}';
    }
}
