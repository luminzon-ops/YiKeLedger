package com.yike.yikeledger.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AppSettingsManager {
    private lateinit var prefs: AppPreferences

    private val _fontScale = MutableStateFlow(1.0f)
    val fontScale: StateFlow<Float> = _fontScale.asStateFlow()

    private val _currencyFormat = MutableStateFlow(AppPreferences.DEFAULT_CURRENCY)
    val currencyFormat: StateFlow<String> = _currencyFormat.asStateFlow()

    private val _autoBackup = MutableStateFlow(false)
    val autoBackup: StateFlow<Boolean> = _autoBackup.asStateFlow()

    private val _expenseNotify = MutableStateFlow(false)
    val expenseNotify: StateFlow<Boolean> = _expenseNotify.asStateFlow()

    private val _notifyTime = MutableStateFlow(AppPreferences.DEFAULT_NOTIFY)
    val notifyTime: StateFlow<String> = _notifyTime.asStateFlow()

    fun init(context: Context) {
        prefs = AppPreferences(context.applicationContext)
        _fontScale.value = prefs.fontScale
        _currencyFormat.value = prefs.currencyFormat
        _autoBackup.value = prefs.autoBackup
        _expenseNotify.value = prefs.expenseNotify
        _notifyTime.value = prefs.notifyTime
    }

    fun setFontScale(scale: Float) { _fontScale.value = scale; prefs.fontScale = scale }
    fun setCurrencyFormat(format: String) { _currencyFormat.value = format; prefs.currencyFormat = format }
    fun setAutoBackup(enabled: Boolean) { _autoBackup.value = enabled; prefs.autoBackup = enabled }
    fun setExpenseNotify(enabled: Boolean) { _expenseNotify.value = enabled; prefs.expenseNotify = enabled }
    fun setNotifyTime(time: String) { _notifyTime.value = time; prefs.notifyTime = time }

    fun formatAmount(amount: Double): String {
        val formatted = String.format("%.2f", amount)
        return when (currencyFormat.value) {
            "suffix" -> "$formatted ¥"
            "prefix_space" -> "¥ $formatted"
            else -> "¥$formatted"
        }
    }
}
