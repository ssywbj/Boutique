package ying.jie.jni;

public class Jni {

    static {
        System.loadLibrary("JniTest");
    }

    public native int getCInt();// 直接调用C/C++中的getCInt()函数

    public native String getCString();// 直接调用C/C++中的getCString()函数

    public native int getStatus(String title, String msg);

    public native int getResponseCode(String title);
}
