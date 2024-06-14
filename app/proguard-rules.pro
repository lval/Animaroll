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

-optimizationpasses 4
-mergeinterfacesaggressively
#-dontoptimize

# Uncomment this to preserve the line number information. Useful for debugging.
#-keepattributes SourceFile, LineNumberTable
#-keepattributes LocalVariableTable, LocalVariableTypeTable

# Uncomment if using annotations to keep them.
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
#-keepattributes Exception

# Hide the original source file name
-renamesourcefileattribute SourceFile

#When not preverifing in a case-insensitive filing system, such as Windows. Because this tool unpacks your processed jars, you should then use:
-dontusemixedcaseclassnames

#Specifies not to ignore non-public library classes. As of version 4.5, this is the default setting
-dontskipnonpubliclibraryclasses

# Preverification is irrelevant for the dex compiler and the Dalvik VM
-dontpreverify

#The -optimizations option disables some arithmetic simplifications that Dalvik 1.0 and 1.5 can't
#handle. Note that the Dalvik VM also can't handle aggressive overloading (of static fields).
#To understand or change this check http://proguard.sourceforge.net/index.html#/manual/optimizations.html
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/simplification/cast

# This option will print out the entire stack trace, instead of just the exception message.
-verbose

# Repackage classes to minimize package hierarchy
-repackageclasses 'ani'

# Remove unused package names
-keeppackagenames doNotKeepAThing

# Use unique class member names to avoid conflicts
-useuniqueclassmembernames

# Allow modification of access modifiers
-allowaccessmodification

#Keep classes that are referenced on the AndroidManifest
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class com.android.vending.licensing.ILicensingService

-dontwarn afu.org.checkerframework.**
-dontwarn org.checkerframework.**
-dontwarn com.google.errorprone.**

# To remove debug logs:
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

# Maintain java native methods
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}

# Maintain enums
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}

# To keep parcelable classes (to serialize - deserialize objects to sent through Intents)
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}

# Keep the R
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}

# Keep classes that are referenced on the AndroidManifest
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class com.android.vending.licensing.ILicensingService

# Ensure the main application class is kept
#-keep class * extends android.content.Context { *; }
#-keep class * extends android.app.Application { *; }

-keep class com.lvalentin.animaroll.** { *; }
-keep class com.lvalentin.animaroll.R$layout { *; }
-keep class com.lvalentin.animaroll.R$id { *; }
-keep class com.lvalentin.animaroll.R$string { *; }

# ----------------------------------
# Security Crypto
# ----------------------------------
# Keep the required classes and methods for AndroidX Security Crypto
-keep class androidx.security.crypto.** { *; }
-keep interface androidx.security.crypto.** { *; }

# Keep necessary Java security classes and methods
-keep class java.security.** { *; }
-keep class java.security.spec.** { *; }

# ----------------------------------
# AndroidX
# ----------------------------------
#-dontwarn androidx.**
#-keep class androidx.** { *; }
#-keep interface androidx.* { *; }

# Preserve the specified AndroidX classes
#-keep class androidx.appcompat.widget.Toolbar { *; }
#-keep class androidx.appcompat.widget.AppCompatButton { *; }
#-keep class androidx.appcompat.app.AppCompatActivity { *; }
#-keep class androidx.coordinatorlayout.widget.CoordinatorLayout { *; }
#-keep class androidx.preference.PreferenceManager { *; }
#-keep class androidx.preference.PreferenceScreen { *; }
#-keep class androidx.preference.PreferenceFragmentCompat { *; }
#-keep class androidx.recyclerview.widget.DividerItemDecoration { *; }
#-keep class androidx.recyclerview.widget.LinearLayoutManager { *; }
#-keep class androidx.recyclerview.widget.RecyclerView { *; }
#-keep class androidx.appcompat.app.AlertDialog { *; }
#-keep class androidx.core.content.ContextCompat { *; }
#-keep class androidx.core.text.HtmlCompat { *; }
#-keep class androidx.activity.OnBackPressedCallback { *; }
#-keep class androidx.appcompat.widget.TooltipCompat { *; }
#-keep class androidx.core.net.toUri { *; }
#-keep class androidx.core.os.HandlerCompat { *; }
#-keep class androidx.core.view.ViewCompat { *; }
#-keep class androidx.core.view.WindowCompat { *; }
#-keep class androidx.core.view.WindowInsetsCompat { *; }
#-keep class androidx.core.view.WindowInsetsControllerCompat { *; }
#-keep class androidx.security.crypto.EncryptedSharedPreferences { *; }
#-keep class androidx.security.crypto.MasterKey { *; }

#-keep class androidx.** { *; }
#-keep interface androidx.** { *; }
#
## Keep any class annotated with @Keep and its members
#-keep @androidx.annotation.Keep class * { *; }
#-keepclassmembers class * {
#    @androidx.annotation.Keep <fields>;
#    @androidx.annotation.Keep <methods>;
#}
#
#-assumenosideeffects class androidx.** { *; }

# ----------------------------------
# Google Play Billing
# ----------------------------------
# Keep your BillingService class and all its members
-keep class com.lvalentin.animaroll.services.BillingService { *; }

# Keep the custom BillingUpdatesListener interface
-keep interface com.lvalentin.animaroll.services.BillingUpdatesListener { *; }

# Keep classes implementing the BillingUpdatesListener interface
-keep class * implements com.lvalentin.animaroll.services.BillingUpdatesListener { *; }

# Keep the required classes and methods for Google Billing Client
-keep class com.android.billingclient.api.** { *; }
-keep interface com.android.billingclient.api.** { *; }

# Keep important classes and methods from being optimized out
-keepclassmembers class * {
    @com.android.billingclient.api.PurchasesUpdatedListener <fields>;
}

# ----------------------------------
# Google Play Ads
# ----------------------------------
# Keep your AdService class and all its members
-keep class com.lvalentin.animaroll.services.AdService { *; }

# Keep the custom AdEventListener interface
-keep interface com.lvalentin.animaroll.services.AdService$AdEventListener { *; }

# Keep the required classes and methods for Google Play Services Ads
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.internal.ads.** { *; }
-keep interface com.google.android.gms.ads.** { *; }
-keep interface com.google.android.gms.common.** { *; }
-keep interface com.google.android.gms.internal.ads.** { *; }

# Keep important classes and methods from being optimized out
-keepclassmembers class * {
    @com.google.android.gms.ads.AdListener <fields>;
}

# Keep attributes used in native ad layouts
#-keepclassmembers class * {
#    public *;
#    protected *;
#}

# If using reflection, keep classes with their members
# -keepclasseswithmembernames class * {
#     native <methods>;
# }
# -keepclasseswithmembernames class * {
#     public <init>(...);
# }
