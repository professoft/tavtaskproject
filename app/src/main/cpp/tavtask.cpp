#include <jni.h>

// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_professoft_tavtask_utils_Keys_apiKey(JNIEnv *env, jobject thiz) {
    std::string api_key = "ccbcc62e0e5f4332ebf00a8042012549";
    return env->NewStringUTF(api_key.c_str());
}