package com.yike.yikeledger.data

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
data class PeriodStat(
    val period: String, // 时间段标识，如 "2025-02-26"
    val income: Double = 0.0,
    val expense: Double = 0.0
) {
    val net: Double get() = income - expense
}

enum class StatPeriod {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}

fun getPeriodKey(dateTime: String, period: StatPeriod): String {
    return try {
        // 检查dateTime长度是否足够
        if (dateTime.length < 10) {
            return "未知日期"
        }
        
        val date = LocalDate.parse(dateTime.substring(0, 10), DateTimeFormatter.ISO_DATE)

        when (period) {
            StatPeriod.DAILY -> date.format(DateTimeFormatter.ISO_DATE)
            StatPeriod.WEEKLY -> {
                val weekStart = date.minusDays(date.dayOfWeek.value.toLong() - 1)
                weekStart.format(DateTimeFormatter.ISO_DATE) + "_week"
            }
            StatPeriod.MONTHLY -> date.format(DateTimeFormatter.ofPattern("yyyy-MM"))
            StatPeriod.YEARLY -> date.year.toString()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        "未知日期"
    }
}