
# Arro Launcher - For ZTE B866F
Buatan Rizky - Optimized for S905X

## Build APK
1. Android Studio > Open > folder ArroLauncher
2. Tunggu Gradle sync
3. Build > Build APK(s)
4. APK di app/build/outputs/apk/debug/app-debug.apk -> rename ArroLauncher.apk

## Install di B866F
1. Copy APK ke Flashdisk
2. File Manager > Install APK > Allow Unknown Sources
3. Home > Pilih Arro Launcher > Always

Jika terkunci:
adb connect 192.168.1.xxx:5555
adb shell cmd package set-home-activity com.arro.launcher/.MainActivity

Icon ada di res/drawable/ic_launcher.png
