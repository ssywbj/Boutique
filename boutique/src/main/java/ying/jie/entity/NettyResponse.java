package ying.jie.entity;

import com.google.gson.annotations.Expose;

public class NettyResponse extends NettyBase {
    @Expose
    public int code;
    @Expose
    public String msg;
    public String tmpData;

    @Override
    public String toString() {
        return "NettyResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", tmpData='" + tmpData + '\'' +
                '}';
    }
}
