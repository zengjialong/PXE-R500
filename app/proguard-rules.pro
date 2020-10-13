# Add project specific ProGuard rules here.
# By default, the flags in this chs_file are appended to flags specified
# in C:\Users\Administrator\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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
#混合时不使用大小写混合,混合后的类名为小写
-dontusemixedcaseclassnames

#指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

#代码混淆压缩比,在0-7之间,默认为5,一般不做修改
-optimizationpasses 5

#这句话能使我们的项目混淆后产生映射文件
#包含有类名→混淆后类名的映射关系
-verbose

#指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers

#不做预校验,preverify是proguard的四个步骤之一
-dontpreverify

##
-ignorewarnings -keep class * { public private *; }