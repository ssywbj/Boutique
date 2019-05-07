package com.suheng.ssy.boutique;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.suheng.ssy.boutique.dagger.AppComponent;
import com.suheng.ssy.boutique.dagger.AppModule;
import com.suheng.ssy.boutique.dagger.DaggerAppComponent;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by wbj on 2018/12/7.
 */
public class BoutiqueApp extends Application {

    public static final int NET_REQUEST_TIMEOUT = 5;
    private static AppComponent sAppComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        sAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();//强调：AppComponent只能初始化一次

        //if (isDebug()) {
        //这两行必须写在init之前，否则这些配置在init过程中将无效
        ARouter.openLog();//打印日志
        ARouter.openDebug();//开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        //}
        ARouter.init(this);//尽可能早，推荐在Application中初始化

        this.initOkGo();
    }

    public AppComponent getAppComponent() {
        //向外界的依赖提供这个AppComponent
        return sAppComponent;
    }

    private void initOkGo() {
        //-------------这里给出的是示例代码,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put("commonHeaderKey1", "commonHeaderValue1");//header不支持中文，不允许有特殊字符
        httpHeaders.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams httpParams = new HttpParams();
        httpParams.put("param1", "commonParamsValue1");//param支持中文,直接传,不要自己编码
        httpParams.put("param2", "这里支持中文参数");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //----------------------------------------------------------------------------------------//

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        //默认使用的超时时间是60（OkGo.DEFAULT_MILLISECONDS）秒，如果想改，可以自己设置
        builder.readTimeout(NET_REQUEST_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(NET_REQUEST_TIMEOUT, TimeUnit.SECONDS);
        builder.connectTimeout(NET_REQUEST_TIMEOUT, TimeUnit.SECONDS);

        //自动管理cookie（或者叫session的保持），以下几种任选一种就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));

        //-----------------------Https配置，以下几种方案根据需要设置-----------------------------//
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        try {
            //方法三：使用预埋证书，校验服务端证书（自签名证书）
            HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
            //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
            HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer")
                    , "123456", getAssets().open("yyy.cer"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        builder.hostnameVerifier(new SafeHostnameVerifier());
        //----------------------------------------------------------------------------------------//

        OkGo.getInstance().init(this)//必须调用初始化
                .setOkHttpClient(builder.build())//建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)//全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)//全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)//全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(httpHeaders)//全局公共头
                .addCommonParams(httpParams);//全局公共参数
    }

    /**
     * 随便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 随便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 随便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private class SafeTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            try {
                for (X509Certificate certificate : x509Certificates) {
                    certificate.checkValidity(); //检查证书是否过期，签名是否通过等
                }
            } catch (Exception e) {
                throw new CertificateException(e);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 随便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 随便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 随便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private class SafeHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String s, SSLSession sslSession) {
            //return hostname.equals("server.jeasonlzy.com");//验证主机名是否匹配
            return true;
        }
    }
}
