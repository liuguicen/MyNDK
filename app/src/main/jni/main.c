#include "com_example_administrator_myndk_MainActivity.h"
#include<stdio.h>
#include<stdlib.h>


JNIEXPORT jstring JNICALL Java_com_example_administrator_myndk_MainActivity_getStringFromNative
        (JNIEnv *env, jobject jobj){//复制头文件中的“接口”定义，并添加参数名称
    return (*env)->NewStringUTF(env,"This is a String for native");
}
