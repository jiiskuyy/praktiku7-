package id.antasari.idantasarip7_modern_ui_230104040210_

import android.content.Context
import androidx.biometric.BiometricManager

object BiometricUtils {
    /**
     * Mengecek apakah perangkat mendukung dan siap melakukan autentikasi biometrik.
     */
    fun isBiometricReady(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.BIOMETRIC_WEAK
        return biometricManager.canAuthenticate(authenticators) == BiometricManager.BIOMETRIC_SUCCESS
    }

    /**
     * (Opsional) Mengambil pesan status detail tentang ketersediaan biometrik.
     */
    fun getBiometricStatusMessage(context: Context): String {
        val biometricManager = BiometricManager.from(context)
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.BIOMETRIC_WEAK

        return when (biometricManager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> "Biometrik siap digunakan."
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "Perangkat ini tidak memiliki sensor biometrik."
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> "Sensor biometrik sementara tidak tersedia."
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "Belum ada biometrik yang didaftarkan di perangkat."
            else -> "Biometrik tidak dapat digunakan."
        }
    }
}