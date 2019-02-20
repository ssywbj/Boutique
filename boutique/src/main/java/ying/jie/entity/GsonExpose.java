package ying.jie.entity;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * Expose注解的使用：导出的字段上加上Expose 注解，不导出的字段不加。注意的是该注解在使用new Gson()
 * 时不会发生作用（毕竟最常用的API要最简单），必须和GsonBuilder配合使用才能发挥它的作用。
 */
public class GsonExpose {
    @Expose//相当于@Expose(deserialize = true, serialize = true), 序列化（toJson：对象变Json）和反序列化（fromJson：Json变对象）都生效
    public int score;
    //@Expose(serialize = true, deserialize = false)//序列化时生效
    @Expose
    public String dateBirth;
    //@Expose(serialize = false, deserialize = true)//反序列化时生效
    @Expose
    public String dateGraduate;
    //不写，相当于@Expose(deserialize = false, serialize = false)，序列化和反序列化都无效
    @Expose
    public String school;

    public GsonExpose(int score, String dateBirth, String dateGraduate, String school) {
        this.score = score;
        this.dateBirth = dateBirth;
        this.dateGraduate = dateGraduate;
        this.school = school;
    }

    @Override
    public String toString() {
        return "GsonExpose{" +
                "score=" + score +
                ", dateBirth='" + dateBirth + '\'' +
                ", dateGraduate='" + dateGraduate + '\'' +
                ", school='" + school + '\'' +
                '}';
    }
}
