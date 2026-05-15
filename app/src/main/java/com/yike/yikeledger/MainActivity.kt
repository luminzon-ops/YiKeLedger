package com.yike.yikeledger

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import com.yike.yikeledger.data.ThemeManager
import com.yike.yikeledger.data.ThemeSetting
import com.yike.yikeledger.ui.screens.MainScreen
import com.yike.yikeledger.ui.theme.YiKeLedgerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // 在 super.onCreate 之前应用主题
        applyThemeToAppCompat(ThemeManager.getThemeSetting())
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            // 直接收集 ThemeManager 的状态流
            val currentTheme by ThemeManager.themeSetting.collectAsState()
            
            // 当主题变化时，更新系统级主题
            LaunchedEffect(currentTheme) {
                applyThemeToAppCompat(currentTheme)
            }
            
            YiKeLedgerTheme(themeSetting = currentTheme) {
                MainScreen()
            }
        }
    }
    
    private fun applyThemeToAppCompat(themeSetting: ThemeSetting) {
        val nightMode = when (themeSetting) {
            ThemeSetting.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeSetting.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            ThemeSetting.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        
        // 只在主题真正改变时更新，避免不必要的重建
        if (AppCompatDelegate.getDefaultNightMode() != nightMode) {
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
        
        updateSystemBarAppearance(themeSetting)
    }
    
    private fun updateSystemBarAppearance(themeSetting: ThemeSetting) {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        
        val isDarkTheme = when (themeSetting) {
            ThemeSetting.LIGHT -> false
            ThemeSetting.DARK -> true
            ThemeSetting.SYSTEM -> resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }
        
        windowInsetsController.isAppearanceLightStatusBars = !isDarkTheme
        windowInsetsController.isAppearanceLightNavigationBars = !isDarkTheme
    }
}