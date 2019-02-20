package ying.jie.entity;

/**
 * Gson框架泛型的使用
 *
 * @param <T>
 */
public class GsonGeneric<T> {
    public int code;
    public String msg;
    public T data;

    public GsonGeneric(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public String toString() {
        return "GsonGeneric{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
