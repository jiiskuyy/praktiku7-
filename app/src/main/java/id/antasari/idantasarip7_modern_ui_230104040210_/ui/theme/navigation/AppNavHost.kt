package id.antasari.idantasarip7_modern_ui_230104040210_.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import id.antasari.idantasarip7_modern_ui_230104040210_.*
import id.antasari.idantasarip7_modern_ui_230104040210_.ui.auth.AuthViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val SECURITY_DETAILS = "security_details"
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onBiometricClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by authViewModel.uiState.collectAsState()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier
    ) {
        // --- LOGIN ROUTE ---
        composable(Routes.LOGIN) {
            LaunchedEffect(uiState.isSignedIn) {
                if (uiState.isSignedIn) {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            }

            LoginScreen(
                state = uiState,
                onEmailChange = authViewModel::onEmailChange,
                onPasswordChange = authViewModel::onPasswordChange,
                onSignInClick = { authViewModel.signInWithPassword {} },
                onCreateAccountClick = { navController.navigate(Routes.REGISTER) },
                onBiometricClick = onBiometricClick
            )
        }

        // --- REGISTER ROUTE (PERBAIKAN DI SINI) ---
        composable(Routes.REGISTER) {
            CreateAccountScreen(
                state = uiState,
                onNameChange = authViewModel::onNameChange,
                onEmailChange = authViewModel::onEmailChange,
                onPasswordChange = authViewModel::onPasswordChange,
                onConfirmPasswordChange = authViewModel::onConfirmPasswordChange,
                onSignUpClick = {
                    // Panggil fungsi createAccount di ViewModel (data sudah ada di state)
                    if (authViewModel.createAccount(context)) {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                },
                onSignInClick = { navController.popBackStack() }
            )
        }

        // --- HOME ROUTE ---
        composable(Routes.HOME) {
            HomeScreen(
                userName = uiState.name.ifEmpty { "User" },
                onLogoutClick = {
                    authViewModel.logout(context)
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                onOpenSecurityDetailsClick = { navController.navigate(Routes.SECURITY_DETAILS) },
                onOpenSettingsClick = { navController.navigate(Routes.SETTINGS) }
            )
        }

        // --- SETTINGS ROUTE ---
        composable(Routes.SETTINGS) {
            SettingsScreen(
                state = uiState,
                onToggleBiometric = { authViewModel.setBiometricEnabled(context, it) },
                onToggleAppLock = { authViewModel.setAppLockEnabled(context, it) },
                onToggleTheme = { authViewModel.setDarkTheme(context, it) },
                onToggleLoginAlerts = { authViewModel.setLoginAlertsEnabled(it) },
                onToggleNewDeviceAlerts = { authViewModel.setNewDeviceAlertsEnabled(it) },
                onToggleWifiWarning = { authViewModel.setPublicWifiWarningEnabled(it) },
                onDeleteAccount = {
                    authViewModel.deleteAccount(context)
                    navController.navigate(Routes.LOGIN) { popUpTo(0) }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- SECURITY DETAILS ROUTE ---
        composable(Routes.SECURITY_DETAILS) {
            SecurityDetailsScreen(
                userName = uiState.name.ifEmpty { "User" },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}