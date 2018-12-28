# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class com.lion.ccpay.** { *;}
-keep class com.lion.ccsdk.** { *;}
-keep class com.sagittarius.sdk.base.bean.** { *;}
-keep class android.** {*;}
-keep class com.UCMobile.** {*;}
-keep class com.alipay.** {*;}
-keep class com.dataeye.** {*;}
-keep class com.lion.android.http.** {*;}
-keep class com.nostra13.universalimageloader.** {*;} 
-keep class com.ta.** { *;}
-keep class com.tencent.** { *;} 
-keep class com.unionpay.** { *;} 
-keep class com.ut.** { *;}
-keep class com.xbfxmedia.player.** { *;}
-keep class org.** {*;}
-keep class com.alibaba.fastjson.** {*;}
-keep class cn.** {*;}
  