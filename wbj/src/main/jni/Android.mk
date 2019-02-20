LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := app
LOCAL_SRC_FILES := com_example_wbj_jni_Jni.cpp

include $(BUILD_SHARED_LIBRARY)