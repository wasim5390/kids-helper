# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class com.google.** {*;}
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

#okhttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.*

-keep class com.google.gson.** { *; }
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }

# Parse
-keep class com.parse.** { *; }
-dontwarn com.parse.**
-keepattributes SourceFile,LineNumberTable
-keepnames class com.parse.** { *; }
-dontwarn com.squareup.**
-dontwarn android.net.SSLCertificateSocketFactory
-dontwarn android.app.Notification
-keep class bolts.** { *; }
-keepnames class bolts.** { *; }
-dontwarn bolts.**

# okio
-dontwarn okio.**
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn java.nio.file.Path
-dontwarn java.nio.file.OpenOption
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

-dontwarn com.squareup.okhttp.**

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-dontnote okhttp3.**, okio.**, retrofit2.**, pl.droidsonroids.** bolts.**