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

    fun init(context: Context) {
        prefs = AppPreferences(context.applicationContext)
        _fontScale.value = prefs.fontScale
        _currencyFormat.value = prefs.currencyFormat
    }

    fun setFontScale(scale: Float) {
        _fontScale.value = scale
        prefs.fontScale = scale
    }

    fun setCurrencyFormat(format: String) {
        _currencyFormat.value = format
        prefs.currencyFormat = format
    }

    fun formatAmount(amount: Double): String {
        val formatted = String.format("%.2f", amount)
        return when (currencyFormat.value) {
            "suffix" -> "$formatted ¥"
            "prefix_space" -> "¥ $formatted"
            else -> "¥$formatted"
        }
    }
}
