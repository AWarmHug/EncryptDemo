//
// Created by bin.ren on 2019/9/16.
// https://blog.csdn.net/qq_34106574/article/details/84298944
// https://blog.csdn.net/cloverjf/article/details/78676881
//

#include <jni.h>
#include <string>

const char *KEY = "0iXda6/Izo6WEED8vZINlTMvnnGwICYcq8cNvcOPLjM=";

const char *RELEASE_SIGN = "308201dd30820146020101300d06092a864886f70d010105050030373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b3009060355040613025553301e170d3139303430393130343834335a170d3439303430313130343834335a30373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b300906035504061302555330819f300d06092a864886f70d010101050003818d0030818902818100d28570a6d6982db20256c974ca85aefa68a7a3e7999193ef27415dd3a49a19ec6f04b61734d7573daa323ef23485126a2bb0d9215c81b224fdf94f3a0b3e72d26d0a97c4e6f3cdbfa5cde09360afebae4aeaaf7c06a08750b43aa101b64582cf8b98ccea705d1ae748d2f0b01a82df0f3c75ac5aa876182940a0d912ba5737970203010001300d06092a864886f70d010105050003818100ba2b854fbc79f873c21e221bcf9b219c3899641a8a4f3d6dea3fb3cbec539030cafdcfe00e461b65fab45522b70a40f8cd4008366820de82c5393e095a6088f1b325f46b05863fd9c2d76c366e10b05148389e5c371660409a3f6bb78c4d81f4b945387f3c29f86558db28c3977accc40089b8b9916bb0b35b610a7f40433809";
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