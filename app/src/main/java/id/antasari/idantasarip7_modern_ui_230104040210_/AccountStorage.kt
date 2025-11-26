package id.antasari.idantasarip7_modern_ui_230104040210_

import android.content.Context

// Update model data dengan field tambahan untuk settings
data class StoredAccount(
    val name: String,
    val email: String,
    val biometricEnabled: Boolean,
    val isDarkTheme: Boolean,
    val appLockEnabled: Boolean,
    val loginAlertsEnabled: Boolean,
    val newDeviceAlertsEnabled: Boolean,
    val publicWifiWarningEnabled: Boolean
)

object AccountStorage {
    private const val PREF_NAME = "secure_auth_pref"
    private const val KEY_NAME = "user_name"
    private const val KEY_EMAIL = "user_email"
    private const val KEY_PASS = "user_password"
    private const val KEY_BIO_ENABLED = "bio_enabled"
    private const val KEY_DARK_THEME = "dark_theme"
    private const val KEY_APP_LOCK_ENABLED = "app_lock_enabled"
    private const val KEY_LOGIN_ALERTS = "login_alerts_enabled"
    private const val KEY_NEW_DEVICE_ALERTS = "new_device_alerts_enabled"
    private const val KEY_WIFI_WARNING = "public_wifi_warning_enabled"

    fun saveAccount(
        context: Context,
        name: String,
        email: String,
        pass: String,
        bioEnabled: Boolean,
        darkTheme: Boolean,
        appLock: Boolean,
        loginAlerts: Boolean = true,
        newDeviceAlerts: Boolean = true,
        wifiWarning: Boolean = false
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString(KEY_NAME, name)
            putString(KEY_EMAIL, email)
            putString(KEY_PASS, pass)
            putBoolean(KEY_BIO_ENABLED, bioEnabled)
            putBoolean(KEY_DARK_THEME, darkTheme)
            putBoolean(KEY_APP_LOCK_ENABLED, appLock)
            putBoolean(KEY_LOGIN_ALERTS, loginAlerts)
            putBoolean(KEY_NEW_DEVICE_ALERTS, newDeviceAlerts)
            putBoolean(KEY_WIFI_WARNING, wifiWarning)
            apply()
        }
    }

    fun loadAccount(context: Context): StoredAccount? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val email = prefs.getString(KEY_EMAIL, null) ?: return null

        return StoredAccount(
            name = prefs.getString(KEY_NAME, "") ?: "",
            email = email,
            biometricEnabled = prefs.getBoolean(KEY_BIO_ENABLED, false),
            isDarkTheme = prefs.getBoolean(KEY_DARK_THEME, false),
            appLockEnabled = prefs.getBoolean(KEY_APP_LOCK_ENABLED, true),
            loginAlertsEnabled = prefs.getBoolean(KEY_LOGIN_ALERTS, true),
            newDeviceAlertsEnabled = prefs.getBoolean(KEY_NEW_DEVICE_ALERTS, true),
            publicWifiWarningEnabled = prefs.getBoolean(KEY_WIFI_WARNING, false)
        )
    }

    fun getPassword(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_PASS, null)
    }

    fun clearAccount(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply()
    }
}