package id.antasari.idantasarip7_modern_ui_230104040210_

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
// IMPORT PENTING DI BAWAH INI
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
// ---------------------------
import id.antasari.idantasarip7_modern_ui_230104040210_.ui.auth.AuthViewModel
import id.antasari.idantasarip7_modern_ui_230104040210_.ui.navigation.AppNavHost
import id.antasari.idantasarip7_modern_ui_230104040210_.ui.theme.P7ModernUiTheme

class MainActivity : FragmentActivity() {
    private var lastBackgroundTime: Long = 0L
    private val LOCK_TIMEOUT_MS = 5000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authViewModel: AuthViewModel = viewModel()
            val uiState by authViewModel.uiState.collectAsState()
            val navController = rememberNavController()

            authViewModel.checkSession(this)

            P7ModernUiTheme(darkTheme = uiState.isDarkTheme) {
                // Pastikan SecureAuthApp sudah ada (lihat point 5 jika belum)
                SecureAuthApp(
                    authViewModel = authViewModel,
                    onBiometricClick = { showBiometricPrompt(authViewModel) }
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        lastBackgroundTime = System.currentTimeMillis()
    }

    override fun onResume() {
        super.onResume()
        val authViewModel: AuthViewModel = androidx.lifecycle.ViewModelProvider(this)[AuthViewModel::class.java]
        val state = authViewModel.uiState.value
        if (state.isSignedIn && state.isAppLockEnabled && lastBackgroundTime != 0L) {
            if (System.currentTimeMillis() - lastBackgroundTime > LOCK_TIMEOUT_MS) {
                showBiometricPrompt(authViewModel)
            }
        }
    }

    private fun showBiometricPrompt(viewModel: AuthViewModel) {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    viewModel.onBiometricSuccess()
                    Toast.makeText(applicationContext, "Sukses", Toast.LENGTH_SHORT).show()
                }
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode != BiometricPrompt.ERROR_USER_CANCELED && errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        Toast.makeText(applicationContext, "Error: $errString", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verifikasi Keamanan")
            .setSubtitle("Pindai sidik jari")
            .setNegativeButtonText("Batal")
            .build()

        if (BiometricUtils.isBiometricReady(this)) {
            biometricPrompt.authenticate(promptInfo)
        }
    }
}