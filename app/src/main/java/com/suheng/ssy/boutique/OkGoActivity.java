package com.suheng.ssy.boutique;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.Arrays;

public class OkGoActivity extends BasicActivity {

    public static final String SERVER = "http://server.jeasonlzy.com/OkHttpUtils/";
    //public static final String SERVER = "http://192.168.1.121:8080/OkHttpUtils/";
    public static final String URL_METHOD = "http://gank.io/api/data/福利/50/1";//SERVER + "method";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_go);

        OkGo.<String>get(URL_METHOD)
                .tag(this)//请求的tag，主要用于取消对应的请求
                //.isMultipart(true)//post请求方法的属性，表示是否强制使用multipart/form-data表单上传，因为该框架在有文件的时候，无论你是否设置这个参数，默认都是multipart/form-data格式上传，但是如果参数中不包含文件，默认使用application/x-www-form-urlencoded格式上传，如果你的服务器要求无论是否有文件，都要使用表单上传，那么可以用这个参数设置为true。
                //.isSpliceUrl(true)//post请求方法的属性，表示是否强制将params的参数拼接到url后面，默认false不拼接。一般来说，post、put等有请求体的方法应该把参数都放在请求体中，不应该放在url上，但是有的服务端可能不太规范，url和请求体都需要传递参数，那么这时候就使用该参数，他会将你所有使用.params()方法传递的参数，自动拼接在url后面。
                .retryCount(3)//配置超时重连次数，也可以在全局初始化的时候设置，默认使用全局的配置，即为3次，也可以在这里为你的这个请求特殊配置一个，并不会影响全局其他请求的超时重连次数。
                .cacheKey("cacheKey")
                .cacheTime(10 * 1000)
                .headers("header1", "headValue1")//传递服务端需要的http请求头
                .headers("header2", "headValue2")
                .params("param1", "paramValue1", false)//传递键值对参数，格式也是http协议中的格式，最后一个isReplace为可选参数,默认为true，即代表相同key的时候，后添加的会覆盖先前添加的
                .params("param2", "paramValue2")
                //.params("file1", new File("filePath1"))
                //.params("file2", new File("filePath2"))
                .addUrlParams("key", Arrays.asList(new String[]{"key1,key2"}))
                //.addFileParams("key", Arrays.asList(new File[]{new File("file1"), new File("file2")}))//post请求方法的属性
                //.addFileWrapperParams("key", null)//post请求方法的属性
                .execute(new Callback<String>() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        Log.d(mTag, "onStart-->" + request + ", " + request.getUrl());
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d(mTag, "onSuccess-->" + response + ", " + response.message() + "-->" + response.body());
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        Log.d(mTag, "onCacheSuccess-->" + response + ", " + response.message());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.d(mTag, "onError-->" + response + ", " + response.message());
                    }

                    @Override
                    public void onFinish() {
                        Log.d(mTag, "onFinish-->");
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        Log.d(mTag, "uploadProgress-->" + progress);
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        Log.d(mTag, "downloadProgress-->" + progress);
                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        Log.d(mTag, "downloadProgress-->" + response);
                        return null;
                    }
                });

        /*每个请求都有一个.client()方法可以传递一个OkHttpClient对象，表示当前这个请求将使用外界传入的这个OkHttpClient对象，
        其他的请求还是使用全局的保持不变。那么至于这个OkHttpClient你想怎么配置，或者配置什么东西，那就随意了是不，
        改个超时时间，加个拦截器什么的统统都是可以的。
        特别注意： 如果你的当前请求使用的是你传递的OkHttpClient对象的话，那么当你调用OkGo.getInstance().cancelTag(tag)的时候
        ，是取消不了这个请求的，因为OkGo只能取消使用全局配置的请求，不知道你这个请求是用你自己的OkHttpClient的，
        如果一定要取消，可以是使用OkGo提供的重载方法*/
    }

}
