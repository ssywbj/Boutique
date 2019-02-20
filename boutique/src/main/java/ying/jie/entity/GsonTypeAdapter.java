package ying.jie.entity;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class GsonTypeAdapter {
    @Expose
    private Date inSchool;
    @Expose
    private int numInt;
    @Expose
    private double numDouble;

    public GsonTypeAdapter(Date inSchool, int numInt, double numDouble) {
        this.inSchool = inSchool;
        this.numInt = numInt;
        this.numDouble = numDouble;
    }

    @Override
    public String toString() {
        return "GsonTypeAdapter{" +
                "inSchool=" + inSchool +
                ", numInt=" + numInt +
                ", numDouble=" + numDouble +
                '}';
    }
}
