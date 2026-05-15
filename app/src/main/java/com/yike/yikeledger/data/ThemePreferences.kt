package com.yike.yikeledger.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * 主题设置偏好存储
 * 使用SharedPreferences保存和读取主题设置
 */
class ThemePreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "theme_preferences"
        private const val KEY_THEME_SETTING = "theme_setting"
        private const val DEFAULT_THEME = "SYSTEM" // 默认跟随系统
    }
    
    /**
     * 保存主题设置
     */
    fun saveThemeSetting(themeSetting: ThemeSetting) {
        sharedPreferences.edit {
            putString(KEY_THEME_SETTING, themeSetting.name)
        }
    }
    
    /**
     * 读取主题设置
     */
    fun getThemeSetting(): ThemeSetting {
        val themeName = sharedPreferences.getString(KEY_THEME_SETTING, DEFAULT_THEME)
        return try {
            ThemeSetting.valueOf(themeName ?: DEFAULT_THEME)
        } catch (e: IllegalArgumentException) {
            // 如果存储的值无效，返回默认值
            ThemeSetting.SYSTEM
        }
    }
    
    /**
     * 清除主题设置（恢复默认）
     */
    fun clearThemeSetting() {
        sharedPreferences.edit {
            remove(KEY_THEME_SETTING)
        }
    }
}