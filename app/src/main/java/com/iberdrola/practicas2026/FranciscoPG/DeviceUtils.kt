package com.iberdrola.practicas2026.FranciscoPG

import android.os.Build
import android.util.Log

object DeviceUtils {
    fun isEmulator(): Boolean {
        val result = (Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.startsWith("unknown") ||
                Build.FINGERPRINT.contains("emulator") ||
                Build.FINGERPRINT.contains("sdk_gphone") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for x86") ||
                Build.MANUFACTURER.contains("Genymotion") ||
                Build.BRAND.startsWith("generic") ||
                Build.DEVICE.startsWith("generic") ||
                Build.PRODUCT.contains("sdk") ||
                Build.PRODUCT.contains("emulator") ||
                Build.HARDWARE == "goldfish" ||
                Build.HARDWARE == "ranchu")

        Log.d("🌐 DeviceUtils", "isEmulator=$result | " +
                "FINGERPRINT=${Build.FINGERPRINT} | " +
                "MODEL=${Build.MODEL} | " +
                "MANUFACTURER=${Build.MANUFACTURER} | " +
                "HARDWARE=${Build.HARDWARE}")
        return result
    }
}