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
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field
-keep public class android.webkit.JavascriptInterface {}
-keep class android.support.v7.widget.SearchView { *; }
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }



-keepattributes InnerClasses
-keep class com.android.volley.** {*;}
-dontwarn com.android.volley.**

# To support Enum type of class members
-keepclassmembers enum * { *; }

-keepattributes Signature
-keepattributes *Annotation*




-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepattributes JavascriptInterface
-keepattributes *Annotation*

-dontwarn com.razorpay.**
-keep class com.razorpay.** {*;}

-optimizations !method/inlining


# For mediation
-keepattributes *Annotation*

#encryptor
-keep class com.jetsynthesys.encryptor.** { *; }

#contactlib
-keep class jagerfield.mobilecontactslibrary.** { *; }

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#Fabric crashlytics
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
#-keepresourcexmlelements manifest/application/meta-data@name=io.fabric.ApiKey
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

#Lottie
-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.** {*;}


#Firebase
-keepattributes EnclosingMethod
-keepattributes InnerClasses

-keep class com.google.firebase.quickstart.database.viewholder.** {
    *;
}

-keepclassmembers class com.google.firebase.quickstart.database.models.** {
    *;
}


-keepattributes Signature,*Annotation*

-dontwarn javax.xml.stream.events.**
-dontwarn org.codehaus.jackson.**
-dontwarn org.apache.commons.logging.impl.**
-dontwarn org.apache.http.conn.scheme.**


#LifeCycle
-keepattributes *Annotation*

-ignorewarnings

-dontwarn org.apache.commons.**
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**

-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }

-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
-assumenosideeffects class * implements org.slf4j.Logger {
    public *** trace(...);
    public *** debug(...);
    public *** info(...);
    public *** warn(...);
    public *** error(...);
}

-printmapping mapping.txt
#Fabric crashlytics
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
#-keepresourcexmlelements manifest/application/meta-data@name=io.fabric.ApiKey
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

##
#
# If you are using the In-App Purchasing APIs,
# and are obfuscating your code before release, you will need
# to add these three lines into your Proguared.cfg file to ensure
# that the proguard obfuscator does not interfere with the In-App Purchasing APIs.
#
#

-dontwarn com.amazon.**
-keep class com.amazon.** {*;}
-keepattributes *Annotation*

-keeppackagenames org.jsoup.nodes


-keep class com.facebook.ads.** { *; }
-dontwarn com.facebook.ads.**

-keepnames class com.facebook.FacebookActivity
-keepnames class com.facebook.CustomTabActivity

-keep class com.facebook.all.All

-keep class android.support.v7.app.MediaRouteActionProvider{
  *;
}
-keep class androidx.mediarouter.app.MediaRouteActionProvider{
*;
}

-keep class com.naver.android.helloyako.imagecrop.** { *; }
-dontwarn com.naver.android.helloyako.imagecrop.**

-keep class com.google.obf.** { *; }
-keep interface com.google.obf.** { *; }
-keep class com.google.ads.interactivemedia.** { *; }
-keep interface com.google.ads.interactivemedia.** { *; }

-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.


-keep class org.xmlpull.v1.** { *;}
 -dontwarn org.xmlpull.v1.**


 -keepclasseswithmembers class * {
     public <init>(android.content.Context);
 }

-keep public class com.google.j2objc.** { public *; }
-dontwarn com.google.j2objc.**-keep


-keep class com.google.android.gms.maps.** { *; }
-keep interface com.google.android.gms.maps.** { *; }

-dontwarn javax.naming.**
-dontwarn javax.servlet.**
-dontwarn org.slf4j.**

# The FlexboxLayoutManager may be set from a layout xml, in that situation the RecyclerView
# tries to instantiate the layout manager using reflection.
# This is to prevent the layout manager from being obfuscated.
-keepnames public class com.google.android.flexbox.FlexboxLayoutManager

-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

-keepclassmembers class * {
   @com.google.gson.annotations.SerializedName <fields>;
}

-keepclassmembers class net.sourceforge.zbar.ImageScanner { *; }
-keepclassmembers class net.sourceforge.zbar.Image { *; }

# Smartech Base SDK
-dontwarn com.netcore.android.**
-keep class com.netcore.android.**{*;}
-keep class * implements com.netcore.android.**.* {*;}
-keep class * extends com.netcore.android.**.* {*;}

# Smartech Push SDK
-dontwarn com.netcore.android.smartechpush.**
-keep class com.netcore.android.smartechpush.**{*;}
-keep class * implements com.netcore.android.smartechpush.**.* {*;}
-keep class * extends com.netcore.android.smartechpush.**.* {*;}

#Smartech Product Experience
-dontwarn io.hansel.**
-keep class io.hansel.**{*;}
-keep class * implements io.hansel.**.* {*;}
-keep class * extends io.hansel.**.* {*;}
-keep class com.netcore.views.** { *; }



