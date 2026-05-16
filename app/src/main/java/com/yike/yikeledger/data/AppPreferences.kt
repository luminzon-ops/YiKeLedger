package com.yike.yikeledger.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class AppPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var fontScale: Float
        get() = prefs.getFloat(KEY_FONT_SCALE, 1.0f)
        set(value) = prefs.edit { putFloat(KEY_FONT_SCALE, value) }

    var currencyFormat: String
        get() = prefs.getString(KEY_CURRENCY_FORMAT, DEFAULT_CURRENCY) ?: DEFAULT_CURRENCY
        set(value) = prefs.edit { putString(KEY_CURRENCY_FORMAT, value) }

    companion object {
        private const val PREFS_NAME = "app_settings"
        private const val KEY_FONT_SCALE = "font_scale"
        private const val KEY_CURRENCY_FORMAT = "currency_format"
        const val DEFAULT_CURRENCY = "prefix"
    }
}
