package id.antasari.idantasarip7_modern_ui_230104040210_

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
// IMPORT INI YANG HILANG:
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.antasari.idantasarip7_modern_ui_230104040210_.ui.auth.AuthUiState
import id.antasari.idantasarip7_modern_ui_230104040210_.ui.theme.P7ModernUiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: AuthUiState,
    onToggleBiometric: (Boolean) -> Unit = {},
    onToggleAppLock: (Boolean) -> Unit = {},
    onToggleTheme: (Boolean) -> Unit = {},
    onToggleLoginAlerts: (Boolean) -> Unit = {},
    onToggleNewDeviceAlerts: (Boolean) -> Unit = {},
    onToggleWifiWarning: (Boolean) -> Unit = {},
    onDeleteAccount: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(24.dp)
        ) {
            // Section: Security
            SettingsHeader(title = "Security")

            ModernSettingsItem(
                icon = Icons.Filled.Fingerprint,
                title = "Biometric Login",
                subtitle = "Use fingerprint or face to log in",
                isChecked = state.isBiometricEnabled,
                onCheckedChange = onToggleBiometric,
                iconColor = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            ModernSettingsItem(
                icon = Icons.Filled.Lock,
                title = "App Lock",
                subtitle = "Lock app immediately when closed",
                isChecked = state.isAppLockEnabled,
                onCheckedChange = onToggleAppLock,
                iconColor = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Section: Appearance
            SettingsHeader(title = "Appearance")
            ModernSettingsItem(
                icon = Icons.Filled.DarkMode,
                title = "Dark Mode",
                subtitle = "Switch between light and dark theme",
                isChecked = state.isDarkTheme,
                onCheckedChange = onToggleTheme,
                iconColor = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Section: Alerts & Notifications
            SettingsHeader(title = "Alerts")
            ModernSettingsItem(
                icon = Icons.Filled.Notifications,
                title = "Login alerts",
                subtitle = "Get notified when someone signs in",
                isChecked = state.loginAlertsEnabled,
                onCheckedChange = onToggleLoginAlerts,
                iconColor = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.height(12.dp))

            ModernSettingsItem(
                icon = Icons.Filled.Devices,
                title = "New device alerts",
                subtitle = "Warn me when used on a new device",
                isChecked = state.newDeviceAlertsEnabled,
                onCheckedChange = onToggleNewDeviceAlerts,
                iconColor = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Danger Zone
            Surface(
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Danger Zone",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Deleting your account is irreversible. All your data will be removed.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onDeleteAccount,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(Icons.Filled.DeleteForever, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Delete Account")
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun ModernSettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    iconColor: Color
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = iconColor)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                    Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.surface,
                    checkedTrackColor = iconColor,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
    P7ModernUiTheme {
        SettingsScreen(state = AuthUiState(name = "User Preview"))
    }
}