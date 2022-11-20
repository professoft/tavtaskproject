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
    std::string api_key = "25ede74aca9d19dd73fc1b8ead5515aa";
    return env->NewStringUTF(api_key.c_str());
}