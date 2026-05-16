package com.yike.yikeledger.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.yike.yikeledger.data.AppSettingsManager
import com.yike.yikeledger.data.ThemeSetting

private val DarkColorScheme = darkColorScheme(
    primary = OceanBlue80,
    secondary = TealGreen80,
    tertiary = Coral80,
    background = Neutral95,
    surface = SurfaceDark,
    surfaceVariant = SurfaceDimDark,
    onPrimary = Neutral10,
    onSecondary = Neutral10,
    onTertiary = Neutral10,
    onBackground = Neutral20,
    onSurface = Neutral20,
    onSurfaceVariant = Neutral40,
    outline = BorderDark,
    error = ErrorRed,
    primaryContainer = OceanBlue80.copy(alpha = 0.22f),
    secondaryContainer = TealGreen80.copy(alpha = 0.2f),
    tertiaryContainer = Coral80.copy(alpha = 0.2f)
)

private val LightColorScheme = lightColorScheme(
    primary = OceanBlue40,
    secondary = TealGreen40,
    tertiary = Coral40,
    background = Neutral10,
    surface = SurfaceLight,
    surfaceVariant = SurfaceDimLight,
    onPrimary = Neutral10,
    onSecondary = Neutral10,
    onTertiary = Neutral10,
    onBackground = Neutral90,
    onSurface = Neutral80,
    onSurfaceVariant = Color(0xFF495057),   // 原 Neutral60 太浅，改为更深的灰确保可读性
    outline = Color(0xFFADB5BD),            // 原 BorderLight 太浅，改为可见的边框色
    error = ErrorRed,
    primaryContainer = OceanBlue40.copy(alpha = 0.12f),
    secondaryContainer = TealGreen40.copy(alpha = 0.12f),
    tertiaryContainer = Coral40.copy(alpha = 0.12f)
)

@Composable
fun YiKeLedgerTheme(
    themeSetting: ThemeSetting = ThemeSetting.SYSTEM,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // 根据主题设置决定是否使用深色主题
    val useDarkTheme = when (themeSetting) {
        ThemeSetting.LIGHT -> false
        ThemeSetting.DARK -> true
        ThemeSetting.SYSTEM -> isSystemInDarkTheme()
    }
    
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val fontScale by AppSettingsManager.fontScale.collectAsState()

    MaterialTheme(
        colorScheme = colorScheme,
        typography = scaledTypography(fontScale),
        content = content
    )
}