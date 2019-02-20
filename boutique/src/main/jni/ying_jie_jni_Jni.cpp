#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "ying_jie_jni_Jni.h"

#ifdef __cplusplus
extern "C" {
#endif

int add2() {
    int x, y;
    x = 870;
    y = 18;
    x += y;
    return x;
}

JNIEXPORT jint
JNICALL Java_ying_jie_jni_Jni_getCInt(JNIEnv *env,
                                      jobject thiz) {
    return add2();
}

JNIEXPORT jstring
JNICALL Java_ying_jie_jni_Jni_getCString(JNIEnv *env,
                                         jobject thiz) {
    return env->NewStringUTF("handle by cpp ");
}

JNIEXPORT jint
JNICALL Java_ying_jie_jni_Jni_getStatus(JNIEnv *env,
                                        jobject thiz, jstring strTitle, jstring strMessage) {
    jclass cls = env->FindClass("ying/jie/boutique/JniActivity");
    if (cls != NULL) {
        //直接调用JNIActivity的responseInfo(String, String)静态方法；I表示这个方法的返回类型为int
        jmethodID id = env->GetStaticMethodID(cls, "responseInfo",
                                              "(Ljava/lang/String;Ljava/lang/String;)I");
        if (id != NULL) {
            return env->CallStaticIntMethod(cls, id, strTitle, strMessage);
        }
    }
    return -1;
}

JNIEXPORT jint
JNICALL Java_ying_jie_jni_Jni_getResponseCode(JNIEnv *env,
                                              jobject thiz, jstring strTitle) {
    jclass cls = env->FindClass("ying/jie/boutique/JniActivity");
    if (cls != NULL) {
        jmethodID constuctor_id = env->GetMethodID(cls, "<init>", "()V"); // 获取类的实例
        if (constuctor_id != NULL) {
            jobject obj = env->NewObject(cls, constuctor_id);
            if (obj != NULL) {
                //调用JNIActivity的responseInfo(String)方法；V表示这个方法的返回类型为void
                jmethodID showMessage_id = env->GetMethodID(cls, "responseInfo",
                                                            "(Ljava/lang/String;)V");
                if (showMessage_id != NULL) {
                    env->CallVoidMethod(obj, showMessage_id, strTitle);
                    return 0;
                }
            }
        }
    }
    return -1;
}

#ifdef __cplusplus
}
#endif
