# Howeather-App

Hasan Çahan

HoWeather App Operating Requirements
_____________________________________________

1-Android Studio 3.0.1 Version must be installed.

2-Genymotion emulator application must be installed for testing.


*************************************************** **********************
3- SDK versions and versions should be as follows.

compileSdkVersion 30
     defaultConfig {
         applicationId "com.hasancahan.howeather"
         minSdkVersion 21
         targetSdkVersion 26
         versionCode 1
         versionName "1.0"
         testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"


*************************************************** **********************
4-The fields I provided under Dependencies should be imported.

dependencies {
     implementation fileTree(include: ['*.jar'], dir: 'libs')
     implementation "org.jetbrains.kotlin:kotlin-stdlib-jre7:$kotlin_version"
     //noinspection GradleCompatible
     implementation 'com.android.support:appcompat-v7:26.1.+'
     implementation 'com.android.support.constraint:constraint-layout:2.0.4'
     compile 'com.toptoche.searchablespinner:searchablespinnerlibrary:1.3.1'
     compile 'com.github.delight-im:Android-SimpleLocation:v1.1.0'
     implementation 'com.android.volley:volley:1.1.1'
     implementation 'pl.droidsonroids.gif:android-gif-drawable:1.2.19'
     testImplementation 'junit:junit:4.12'
     androidTestImplementation 'com.android.support.test:runner:1.0.2'
     androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'

*************************************************** **********************

5-The apk is installed and before the apk is opened immediately, location permission must be given to the application in the application permissions section of the settings.

6-Gps-Location must be turned on from the settings.

7-To view the fonts fully, Huawei Font Manager must be installed via Google Play Store.

8-There is a video link in the Attachments section of the Word file.
