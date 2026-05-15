package com.yike.yikeledger.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 应用级别主题管理器
 * 单例模式，确保整个应用使用同一份主题状态
 */
object ThemeManager {
    private const val PREFS_NAME = "theme_preferences"
    private const val KEY_THEME_SETTING = "theme_setting"
    private const val DEFAULT_THEME = "SYSTEM"
    
    private lateinit var sharedPreferences: SharedPreferences
    
    private val _themeSetting = MutableStateFlow(ThemeSetting.SYSTEM)
    val themeSetting: StateFlow<ThemeSetting> = _themeSetting.asStateFlow()
    
    /**
     * 初始化 ThemeManager
     * 在 Application.onCreate 中调用
     */
    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedTheme = sharedPreferences.getString(KEY_THEME_SETTING, DEFAULT_THEME)
        _themeSetting.value = try {
            ThemeSetting.valueOf(savedTheme!!)
        } catch (e: IllegalArgumentException) {
            ThemeSetting.SYSTEM
        }
    }
    
    /**
     * 保存并应用主题设置
     */
    fun setThemeSetting(themeSetting: ThemeSetting) {
        _themeSetting.value = themeSetting
        sharedPreferences.edit().putString(KEY_THEME_SETTING, themeSetting.name).apply()
    }
    
    /**
     * 获取当前主题
     */
    fun getThemeSetting(): ThemeSetting = _themeSetting.value
}