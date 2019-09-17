//
// Created by bin.ren on 2019/9/16.
// https://blog.csdn.net/qq_34106574/article/details/84298944
// https://blog.csdn.net/cloverjf/article/details/78676881
//

#include <jni.h>
#include <string>

const char *KEY = "renbin";

const char *RELEASE_SIGN = "3082032f30820217a0030201020204447f5584300d06092a864886f70d01010b05003048310a30080603550406130141310a30080603550408130141310a30080603550407130141310a3008060355040a130141310a3008060355040b130141310a30080603550403130141301e170d3139303931363033323631385a170d3434303930393033323631385a3048310a30080603550406130141310a30080603550408130141310a30080603550407130141310a3008060355040a130141310a3008060355040b130141310a3008060355040313014130820122300d06092a864886f70d01010105000382010f003082010a0282010100ac4225fe5c04c67df9208168311624508e1feb8210e5298d60f16e3be8d1e5d6d1274bf2d991c2e8089d010e29b66a6ddacd2697fbfd3cbc2c36367532ed63ef10d2d36c49e92d2e550aec2357f4d5d6200d8b1009a8826b85f59789c43c75e9ee47a5eff594a2d6105cc5f553eaebff25b6feda9e3c078adb8db6d56e53ac3c56eda2a8a9b4997e6a693d4bb125a830d3b6959216fbedcd2699b69f2b89386facc37b7d4cb4b4f5c80354424587f05458dec488b5d6daf3be24546e09eba8733c22d342b080c91c9a9aab3288a5d0fc29688a19e17b4a75d12649d01f26942b5b7c401ecd1cbe3b943610042b0fc25403f8f31a24eebaff0032f42d32c920fb0203010001a321301f301d0603551d0e041604140bfe43c11a959a23ee8453e26def7d000f9e4d62300d06092a864886f70d01010b0500038201010031daa7d9829a1272370711b3237520bb99412e3bff2a633a476a35e7a9218ea575f9a12bbd898bc11101fc8703281705cc959612001e519129d23145c885f2679e3e05facf97c90c5c575876e3e8830fb961de1390f7352b969c103e10e2d77950740d2c97b063422c56483fd43f5b4ba9dea47c54eb2a1c6021b7fabe980166f287d35105697f75eb2e822e97d8f619d4666ddbe5b418065f3ba7156ccc03302c8ef5e4cf92a75b1e7f48dc5decc50549ab042633e9c1b958b7a08dd7cb218f6957bec4b558329e407bd0e3d9f131b1dc56c953223b0a68c665a03724e40329e5a1afdfba3c240b1b0519471968023b42eda31d81511c2dda3dc201aa0610b6";
const char *RELEASE_PACKAGE = "com.warm.encryptdemo";


bool veriPackage(JNIEnv *env,
                 jclass clazz, jobject context_object) {
    jclass context_class = env->GetObjectClass(context_object);

    //context.getPackageManager()
    jmethodID methodId = env->GetMethodID(context_class, "getPackageManager",
                                          "()Landroid/content/pm/PackageManager;");
    jobject package_manager_object = env->CallObjectMethod(context_object, methodId);
    if (package_manager_object == NULL) {

        return NULL;
    }

    //context.getPackageName()
    methodId = env->GetMethodID(context_class, "getPackageName", "()Ljava/lang/String;");
    jstring package_name_string = (jstring) env->CallObjectMethod(context_object, methodId);
    if (package_name_string == NULL) {

        return NULL;
    }

    const char *c_package = (char *) env->GetStringUTFChars(package_name_string, 0);

    return strcmp(c_package, RELEASE_PACKAGE) == 0;
}


extern "C" JNIEXPORT jstring
JNICALL
Java_com_warm_encryptdemo_GetSignature_getKey
        (JNIEnv *env, jclass clazz, jobject context_object) {

    if (!veriPackage(env, clazz, context_object)) {
        return (env)->NewStringUTF("sign error");
    }

    jclass context_class = env->GetObjectClass(context_object);

    //context.getPackageManager()
    jmethodID methodId = env->GetMethodID(context_class, "getPackageManager",
                                          "()Landroid/content/pm/PackageManager;");
    jobject package_manager_object = env->CallObjectMethod(context_object, methodId);
    if (package_manager_object == NULL) {
        return NULL;
    }

    //context.getPackageName()
    methodId = env->GetMethodID(context_class, "getPackageName", "()Ljava/lang/String;");
    jstring package_name_string = (jstring) env->CallObjectMethod(context_object, methodId);
    if (package_name_string == NULL) {
        return NULL;
    }

    env->DeleteLocalRef(context_class);

    //PackageManager.getPackageInfo(Sting, int)
    jclass pack_manager_class = env->GetObjectClass(package_manager_object);
    methodId = env->GetMethodID(pack_manager_class, "getPackageInfo",
                                "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    env->DeleteLocalRef(pack_manager_class);
    jobject package_info_object = env->CallObjectMethod(package_manager_object, methodId,
                                                        package_name_string, 64);
    if (package_info_object == NULL) {
        return NULL;
    }

    env->DeleteLocalRef(package_manager_object);

    //PackageInfo.signatures[0]
    jclass package_info_class = env->GetObjectClass(package_info_object);
    jfieldID fieldId = env->GetFieldID(package_info_class, "signatures",
                                       "[Landroid/content/pm/Signature;");
    env->DeleteLocalRef(package_info_class);
    jobjectArray signature_object_array = (jobjectArray) env->GetObjectField(package_info_object,
                                                                             fieldId);
    if (signature_object_array == NULL) {
        return NULL;
    }
    jobject signature_object = env->GetObjectArrayElement(signature_object_array, 0);

    env->DeleteLocalRef(package_info_object);

    //Signature.toCharsString()
    jclass signature_class = env->GetObjectClass(signature_object);
    methodId = env->GetMethodID(signature_class, "toCharsString", "()Ljava/lang/String;");
    env->DeleteLocalRef(signature_class);
    jstring signature_string = (jstring) env->CallObjectMethod(signature_object, methodId);

    const char *c_sign = (char *) env->GetStringUTFChars(signature_string, 0);

    //签名一致  返回合法的 api key，否则返回错误
    if (strcmp(c_sign, RELEASE_SIGN) == 0) {
        return (env)->NewStringUTF(KEY);
    } else {
        return (env)->NewStringUTF("sign error");

    }
}