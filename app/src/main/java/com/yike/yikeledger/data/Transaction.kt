package com.yike.yikeledger.data

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class Transaction(
    val id: Long = System.currentTimeMillis(),
    val dateTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    val description: String,
    val type: TransactionType,
    val category: String = "",
    val amount: Double,
    val accountId: Long = 1
) {
    // 根据类型返回带符号的金额（支出为负数）
    fun signedAmount(): Double = if (type == TransactionType.EXPENSE) -amount else amount
}

@Serializable
enum class TransactionType {
    INCOME, // 收入
    EXPENSE // 支出
}