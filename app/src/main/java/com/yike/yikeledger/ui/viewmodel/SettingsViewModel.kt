package com.yike.yikeledger.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.yike.yikeledger.data.AppSettingsManager
import com.yike.yikeledger.data.ThemeManager
import com.yike.yikeledger.data.ThemeSetting
import kotlinx.coroutines.flow.StateFlow

/**
 * 设置页面的ViewModel
 * 现在使用 ThemeManager 作为单一数据源
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    init {
        AppSettingsManager.init(application)
    }

    // 直接暴露 ThemeManager 的 StateFlow，确保所有地方看到的是同一份数据
    val themeSetting: StateFlow<ThemeSetting> = ThemeManager.themeSetting
    val fontScale: StateFlow<Float> = AppSettingsManager.fontScale
    val currencyFormat: StateFlow<String> = AppSettingsManager.currencyFormat
    val autoBackup: StateFlow<Boolean> = AppSettingsManager.autoBackup
    val expenseNotify: StateFlow<Boolean> = AppSettingsManager.expenseNotify
    val notifyTime: StateFlow<String> = AppSettingsManager.notifyTime

    fun saveThemeSetting(s: ThemeSetting) = ThemeManager.setThemeSetting(s)
    fun setFontScale(s: Float) = AppSettingsManager.setFontScale(s)
    fun setCurrencyFormat(f: String) = AppSettingsManager.setCurrencyFormat(f)
    fun setAutoBackup(e: Boolean) = AppSettingsManager.setAutoBackup(e)
    fun setExpenseNotify(e: Boolean) = AppSettingsManager.setExpenseNotify(e)
    fun setNotifyTime(t: String) = AppSettingsManager.setNotifyTime(t)

    fun getFontScaleDescription(): String {
        return FONT_SCALE_OPTIONS.find { it.scale == fontScale.value }?.title ?: "标准"
    }

    fun getCurrencyFormatDescription(): String {
        return CURRENCY_OPTIONS.find { it.format == currencyFormat.value }?.title ?: "¥ 在前"
    }

    fun getNotifyDescription(): String =
        if (expenseNotify.value) notifyTime.value else "已关闭"

    fun getThemeDescription(): String {
        return when (themeSetting.value) {
            ThemeSetting.LIGHT -> "浅色模式"
            ThemeSetting.DARK -> "深色模式"
            ThemeSetting.SYSTEM -> "跟随系统"
        }
    }

    fun getThemeOptions(): List<ThemeOption> {
        return listOf(
            ThemeOption(ThemeSetting.LIGHT, "浅色模式", "使用浅色主题"),
            ThemeOption(ThemeSetting.DARK, "深色模式", "使用深色主题"),
            ThemeOption(ThemeSetting.SYSTEM, "跟随系统", "跟随系统主题设置")
        )
    }

    data class FontScaleOption(val scale: Float, val title: String, val description: String)

    data class CurrencyOption(val format: String, val title: String, val description: String)

    companion object {
        val FONT_SCALE_OPTIONS = listOf(
            FontScaleOption(0.85f, "小", "紧凑显示更多信息"),
            FontScaleOption(1.00f, "标准", "默认字体大小"),
            FontScaleOption(1.15f, "大", "稍微放大字体"),
            FontScaleOption(1.30f, "超大", "最大字体显示")
        )

        val CURRENCY_OPTIONS = listOf(
            CurrencyOption("prefix", "¥ 在前", "¥100.00"),
            CurrencyOption("suffix", "¥ 在后", "100.00 ¥"),
            CurrencyOption("prefix_space", "¥ 在前（空格）", "¥ 100.00")
        )

        val NOTIFY_TIME_OPTIONS = listOf(
            NotifyTimeOption("每天 09:00", "每天 09:00"),
            NotifyTimeOption("每天 12:00", "每天 12:00"),
            NotifyTimeOption("每天 18:00", "每天 18:00"),
            NotifyTimeOption("每天 20:00", "每天 20:00"),
            NotifyTimeOption("每周一 09:00", "每周一 09:00"),
            NotifyTimeOption("每月1日 09:00", "每月1日 09:00")
        )
    }

    data class NotifyTimeOption(val time: String, val title: String)
    data class ThemeOption(
        val setting: ThemeSetting,
        val title: String,
        val description: String
    )
}