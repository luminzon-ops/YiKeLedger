package com.yike.yikeledger.data

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class Account(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val type: AccountType,
    val initialBalance: Double = 0.0,
    val currentBalance: Double = initialBalance,
    val createdAt: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    val color: Int = 0xFF2196F3.toInt() // 默认蓝色
) {
    fun updateBalance(transaction: Transaction): Account {
        val signedAmount = transaction.signedAmount()
        return this.copy(currentBalance = currentBalance + signedAmount)
    }

    fun updateBalanceAfterDelete(transaction: Transaction): Account {
        val signedAmount = transaction.signedAmount()
        return this.copy(currentBalance = currentBalance - signedAmount)
    }
}

@Serializable
enum class AccountType {
    CASH,          // 现金
    BANK_CARD,     // 银行卡
    ALIPAY,        // 支付宝
    WECHAT,        // 微信
    CREDIT_CARD,   // 信用卡
    OTHER          // 其他
}