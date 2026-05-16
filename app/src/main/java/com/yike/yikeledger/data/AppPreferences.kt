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

    var autoBackup: Boolean
        get() = prefs.getBoolean(KEY_AUTO_BACKUP, false)
        set(value) = prefs.edit { putBoolean(KEY_AUTO_BACKUP, value) }

    var expenseNotify: Boolean
        get() = prefs.getBoolean(KEY_EXPENSE_NOTIFY, false)
        set(value) = prefs.edit { putBoolean(KEY_EXPENSE_NOTIFY, value) }

    var notifyTime: String
        get() = prefs.getString(KEY_NOTIFY_TIME, DEFAULT_NOTIFY) ?: DEFAULT_NOTIFY
        set(value) = prefs.edit { putString(KEY_NOTIFY_TIME, value) }

    companion object {
        private const val PREFS_NAME = "app_settings"
        private const val KEY_FONT_SCALE = "font_scale"
        private const val KEY_CURRENCY_FORMAT = "currency_format"
        private const val KEY_AUTO_BACKUP = "auto_backup"
        private const val KEY_EXPENSE_NOTIFY = "expense_notify"
        private const val KEY_NOTIFY_TIME = "notify_time"
        const val DEFAULT_CURRENCY = "prefix"
        const val DEFAULT_NOTIFY = "每天 20:00"
    }
}
