# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Programs\AndroidSDK/tools/proguard/proguard-android.txt
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
#-ignorewarnings

#-keep class * {
 #   public private *;
#}

-dontwarn okio.**
-dontwarn retrofit2.**
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
-dontwarn com.google.errorprone.annotations.*

#skip every public class that extends com.orm.SugarRecord
 #and their public/protected members
 #-keep public class * extends com.orm.SugarRecord {
  #public protected *;
#}
#-keep public class * extends com.orm.SugarRecord
#-keep class com.orm.** { *; }
#-keep class com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.** { *; }
##-keep class com.** { *; }