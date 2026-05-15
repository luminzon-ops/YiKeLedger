package com.yike.yikeledger

import android.app.Application
import com.yike.yikeledger.data.ThemeManager

class YiKeLedgerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化主题管理器
        ThemeManager.initialize(this)
    }
}