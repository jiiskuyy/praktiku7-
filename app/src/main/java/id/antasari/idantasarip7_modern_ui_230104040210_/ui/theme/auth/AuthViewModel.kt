package id.antasari.idantasarip7_modern_ui_230104040210_.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import id.antasari.idantasarip7_modern_ui_230104040210_.AccountStorage
import id.antasari.idantasarip7_modern_ui_230104040210_.BiometricUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Update State dengan tambahan setting App Lock, Theme, dan Alerts
data class AuthUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false,
    // Biometric & Security
    val isBiometricAvailable: Boolean = false,
    val isBiometricEnabled: Boolean = false,
    val isAppLockEnabled: Boolean = true, // Default true
    // Theme
    val isDarkTheme: Boolean = false,
    // Alerts
    val loginAlertsEnabled: Boolean = true,
    val newDeviceAlertsEnabled: Boolean = true,
    val publicWifiWarningEnabled: Boolean = true,

    // Kredensial terdaftar
    val registeredEmail: String? = null,
    val registeredPassword: String? = null,
    val lastErrorMessage: String? = null
)

class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private fun updateState(block: (AuthUiState) -> AuthUiState) {
        _uiState.value = block(_uiState.value)
    }

    // --- Input Handlers ---
    fun onNameChange(v: String) { updateState { it.copy(name = v, lastErrorMessage = null) } }
    fun onEmailChange(v: String) { updateState { it.copy(email = v, lastErrorMessage = null) } }
    fun onPasswordChange(v: String) { updateState { it.copy(password = v, lastErrorMessage = null) } }
    fun onConfirmPasswordChange(v: String) { updateState { it.copy(confirmPassword = v, lastErrorMessage = null) } }
    fun clearError() { updateState { it.copy(lastErrorMessage = null) } }

    // --- Session Management ---
    fun checkSession(context: Context) {
        val stored = AccountStorage.loadAccount(context)
        val bioReady = BiometricUtils.isBiometricReady(context)

        if (stored != null) {
            updateState {
                it.copy(
                    name = stored.name,
                    registeredEmail = stored.email,
                    registeredPassword = AccountStorage.getPassword(context),
                    // Load semua setting
                    isBiometricEnabled = stored.biometricEnabled,
                    isAppLockEnabled = stored.appLockEnabled,
                    isDarkTheme = stored.isDarkTheme,
                    // Asumsi setting alert juga disimpan (akan diupdate di Step 9.2)
                    // Jika AccountStorage belum support, gunakan nilai default dulu
                    isBiometricAvailable = bioReady
                )
            }
        } else {
            updateState { it.copy(isBiometricAvailable = bioReady) }
        }
    }

    // --- Auth Actions ---
    fun signInWithPassword(onSuccess: () -> Unit) {
        val current = _uiState.value
        if (current.registeredEmail.isNullOrBlank()) {
            updateState { it.copy(lastErrorMessage = "Belum ada akun terdaftar.") }
            return
        }
        if (current.email != current.registeredEmail || current.password != current.registeredPassword) {
            updateState { it.copy(lastErrorMessage = "Email atau password salah.") }
            return
        }

        updateState { it.copy(isSignedIn = true, lastErrorMessage = null) }
        onSuccess()
    }

    fun createAccount(context: Context): Boolean {
        val s = _uiState.value
        if (s.name.isBlank() || s.email.isBlank() || s.password.isBlank()) {
            updateState { it.copy(lastErrorMessage = "Data tidak boleh kosong") }
            return false
        }
        if (s.password != s.confirmPassword) {
            updateState { it.copy(lastErrorMessage = "Password tidak sama") }
            return false
        }

        // Simpan data awal ke storage
        saveAllSettings(context, s.copy(
            isSignedIn = true,
            isBiometricEnabled = true,
            isAppLockEnabled = true,
            registeredEmail = s.email,
            registeredPassword = s.password
        ))

        return true
    }

    fun onBiometricSuccess() {
        updateState { it.copy(isSignedIn = true, lastErrorMessage = null) }
    }

    fun logout(context: Context) {
        updateState { it.copy(isSignedIn = false, password = "") }
    }

    fun deleteAccount(context: Context) {
        AccountStorage.clearAccount(context)
        _uiState.value = AuthUiState() // Reset state
    }

    // --- Settings Toggles ---

    fun setBiometricEnabled(context: Context, enabled: Boolean) {
        val newState = _uiState.value.copy(isBiometricEnabled = enabled)
        saveAllSettings(context, newState)
    }

    fun setAppLockEnabled(context: Context, enabled: Boolean) {
        val newState = _uiState.value.copy(isAppLockEnabled = enabled)
        saveAllSettings(context, newState)
    }

    fun setDarkTheme(context: Context, enabled: Boolean) {
        val newState = _uiState.value.copy(isDarkTheme = enabled)
        saveAllSettings(context, newState)
    }

    fun setLoginAlertsEnabled(enabled: Boolean) {
        // Simpan ke memori/state (opsional: simpan ke storage jika perlu)
        updateState { it.copy(loginAlertsEnabled = enabled) }
    }

    fun setNewDeviceAlertsEnabled(enabled: Boolean) {
        updateState { it.copy(newDeviceAlertsEnabled = enabled) }
    }

    fun setPublicWifiWarningEnabled(enabled: Boolean) {
        updateState { it.copy(publicWifiWarningEnabled = enabled) }
    }

    // Helper untuk menyimpan semua konfigurasi ke AccountStorage
    private fun saveAllSettings(context: Context, state: AuthUiState) {
        // Update state lokal dulu
        _uiState.value = state

        // Lalu simpan ke persistent storage
        // Note: Parameter saveAccount akan kita update di langkah 9.2 untuk mendukung semua field ini
        AccountStorage.saveAccount(
            context = context,
            name = state.name.ifBlank { state.name }, // Handle edge case saat register
            email = state.registeredEmail ?: state.email,
            pass = state.registeredPassword ?: state.password,
            bioEnabled = state.isBiometricEnabled,
            darkTheme = state.isDarkTheme,
            appLock = state.isAppLockEnabled
        )
    }
}