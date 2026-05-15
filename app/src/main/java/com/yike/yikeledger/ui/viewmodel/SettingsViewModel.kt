package com.yike.yikeledger.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.yike.yikeledger.data.ThemeManager
import com.yike.yikeledger.data.ThemeSetting
import kotlinx.coroutines.flow.StateFlow

/**
 * 设置页面的ViewModel
 * 现在使用 ThemeManager 作为单一数据源
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    
    // 直接暴露 ThemeManager 的 StateFlow，确保所有地方看到的是同一份数据
    val themeSetting: StateFlow<ThemeSetting> = ThemeManager.themeSetting
    
    /**
     * 保存主题设置
     */
    fun saveThemeSetting(themeSetting: ThemeSetting) {
        ThemeManager.setThemeSetting(themeSetting)
    }
    
    /**
     * 获取当前主题描述文本
     */
    fun getThemeDescription(): String {
        return when (themeSetting.value) {
            ThemeSetting.LIGHT -> "浅色模式"
            ThemeSetting.DARK -> "深色模式"
            ThemeSetting.SYSTEM -> "跟随系统"
        }
    }
    
    /**
     * 获取主题选项列表
     */
    fun getThemeOptions(): List<ThemeOption> {
        return listOf(
            ThemeOption(ThemeSetting.LIGHT, "浅色模式", "使用浅色主题"),
            ThemeOption(ThemeSetting.DARK, "深色模式", "使用深色主题"),
            ThemeOption(ThemeSetting.SYSTEM, "跟随系统", "跟随系统主题设置")
        )
    }
    
    data class ThemeOption(
        val setting: ThemeSetting,
        val title: String,
        val description: String
    )
}