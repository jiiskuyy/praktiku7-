package id.antasari.idantasarip7_modern_ui_230104040210_

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController // INI YANG PENTING
import id.antasari.idantasarip7_modern_ui_230104040210_.ui.auth.AuthViewModel
import id.antasari.idantasarip7_modern_ui_230104040210_.ui.navigation.AppNavHost

@Composable
fun SecureAuthApp(
    authViewModel: AuthViewModel,
    onBiometricClick: () -> Unit
) {
    val navController = rememberNavController()
    AppNavHost(
        navController = navController,
        authViewModel = authViewModel,
        onBiometricClick = onBiometricClick
    )
}