package id.antasari.idantasarip7_modern_ui_230104040210_.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Skema warna Modern
private val ModernLightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlue.copy(alpha = 0.1f),
    onPrimaryContainer = PrimaryBlue,
    secondary = SecondaryTeal,
    tertiary = TertiaryIndigo,
    background = Neutral50,
    onBackground = Neutral800,
    surface = Color.White,
    onSurface = Neutral800,
    surfaceVariant = Neutral100,
    onSurfaceVariant = Neutral500,
    outline = Neutral200,
    error = ErrorRed
)

private val ModernDarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkBackground,
    primaryContainer = DarkPrimary.copy(alpha = 0.2f),
    background = DarkBackground,
    onBackground = Neutral200,
    surface = DarkSurface,
    onSurface = Neutral200,
    surfaceVariant = DarkSurface,
    onSurfaceVariant = Neutral500,
    outline = Neutral800,
    error = ErrorRed
)

@Composable
fun P7ModernUiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color kita matikan agar warna modern kita yang tampil
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> ModernDarkColorScheme
        else -> ModernLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        // PERBAIKAN DI SINI: Menggunakan 'AppTypography' (bukan Typography)
        typography = AppTypography,
        shapes = Shapes,
        content = content
    )
}