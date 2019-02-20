package ying.jie.entity;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * @param <T>
 */
public class NettyRequest<T> extends NettyBase {
    @Expose
    public Date sendTime;
    @Expose
    public T data;
    
    public NettyRequest(String order, T data) {
        this.order = order;
        this.sendTime = new Date();
        this.data = data;
    }

}
