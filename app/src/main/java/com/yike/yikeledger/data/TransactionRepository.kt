package com.yike.yikeledger.data

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TransactionRepository(
    private val context: Context,
    private val accountRepository: AccountRepository
) {
    private val fileName = "transactions.json"
    private val json = Json { prettyPrint = true }

    private fun getDataFile(): File = File(context.filesDir, fileName)

    fun loadTransactions(): List<Transaction> {
        return try {
            val file = getDataFile()
            if (!file.exists()) {
                return emptyList()
            }
            val jsonString = file.readText()
            json.decodeFromString<List<Transaction>>(jsonString)
                .sortedByDescending { it.dateTime }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun saveTransactions(transactions: List<Transaction>) {
        try {
            val file = getDataFile()
            val jsonString = json.encodeToString(transactions)
            file.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addTransaction(transaction: Transaction) {
        val transactions = loadTransactions().toMutableList()
        transactions.add(transaction)
        saveTransactions(transactions)
        // 更新账户余额
        accountRepository.updateAccountBalance(transaction.accountId, transaction)
    }

    fun deleteTransaction(id: Long) {
        val transactions = loadTransactions().toMutableList()
        val transactionToDelete = transactions.find { it.id == id }
        transactions.removeIf { it.id == id }
        saveTransactions(transactions)
        // 更新账户余额（减去该交易的影响）
        transactionToDelete?.let {
            accountRepository.updateAccountBalance(it.accountId, it, isDelete = true)
        }
    }

    fun updateTransaction(updatedTransaction: Transaction) {
        val transactions = loadTransactions().toMutableList()
        val index = transactions.indexOfFirst { it.id == updatedTransaction.id }
        if (index >= 0) {
            val oldTransaction = transactions[index]
            // 先移除旧交易对账户余额的影响
            accountRepository.updateAccountBalance(oldTransaction.accountId, oldTransaction, isDelete = true)
            // 更新交易
            transactions[index] = updatedTransaction
            saveTransactions(transactions)
            // 添加新交易对账户余额的影响
            accountRepository.updateAccountBalance(updatedTransaction.accountId, updatedTransaction)
        }
    }

    fun getBalance(): Double {
        return loadTransactions().sumOf { it.signedAmount() }
    }

    fun getStatsByPeriod(period: StatPeriod): List<PeriodStat> {
        val transactions = loadTransactions()
        val statsMap = mutableMapOf<String, PeriodStat>()

        transactions.forEach { transaction ->
            try {
                val periodKey = getPeriodKey(transaction.dateTime, period)
                val current = statsMap.getOrPut(periodKey) { PeriodStat(periodKey) }
                val updated = when (transaction.type) {
                    TransactionType.INCOME -> current.copy(income = current.income + transaction.amount)
                    TransactionType.EXPENSE -> current.copy(expense = current.expense + transaction.amount)
                }
                statsMap[periodKey] = updated
            } catch (e: Exception) {
                e.printStackTrace()
                // 跳过有问题的交易，继续处理其他交易
            }
        }

        return statsMap.values.sortedBy { it.period }
    }

    fun getStatsByDateRange(startDate: String, endDate: String, period: StatPeriod): List<PeriodStat> {
        val transactions = loadTransactions()

        // 解析日期范围
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val start = try {
            LocalDate.parse(startDate, dateFormatter)
        } catch (e: Exception) {
            // 如果解析失败，返回空列表
            return emptyList()
        }

        val end = try {
            LocalDate.parse(endDate, dateFormatter)
        } catch (e: Exception) {
            return emptyList()
        }

        // 确保结束日期不小于开始日期
        val actualEnd = if (end.isBefore(start)) start else end

        // 生成日期范围内的所有周期键
        val allPeriodKeys = generateAllPeriodKeys(start, actualEnd, period)
        val statsMap = mutableMapOf<String, PeriodStat>()

        // 用0值初始化所有周期
        allPeriodKeys.forEach { key ->
            statsMap[key] = PeriodStat(key, 0.0, 0.0)
        }

        // 处理交易，更新统计
        transactions.forEach { transaction ->
            try {
                // 解析交易日期
                val transactionDateTime = LocalDateTime.parse(transaction.dateTime, dateTimeFormatter)
                val transactionDate = transactionDateTime.toLocalDate()

                // 检查是否在日期范围内
                if (transactionDate.isBefore(start) || transactionDate.isAfter(actualEnd)) {
                    return@forEach // 跳过不在范围内的交易
                }

                val periodKey = getPeriodKey(transaction.dateTime, period)
                val current = statsMap[periodKey] ?: PeriodStat(periodKey, 0.0, 0.0)
                val updated = when (transaction.type) {
                    TransactionType.INCOME -> current.copy(income = current.income + transaction.amount)
                    TransactionType.EXPENSE -> current.copy(expense = current.expense + transaction.amount)
                }
                statsMap[periodKey] = updated
            } catch (e: Exception) {
                e.printStackTrace()
                // 跳过有问题的交易，继续处理其他交易
            }
        }

        return statsMap.values.sortedBy { it.period }
    }

    private fun generateAllPeriodKeys(start: LocalDate, end: LocalDate, period: StatPeriod): List<String> {
        val keys = mutableSetOf<String>()
        var current = start

        // 遍历日期范围内的每一天
        while (!current.isAfter(end)) {
            // 创建带默认时间的日期字符串以使用getPeriodKey
            val dateTimeStr = current.format(DateTimeFormatter.ISO_DATE) + " 00:00:00"
            val periodKey = getPeriodKey(dateTimeStr, period)
            keys.add(periodKey)
            current = current.plusDays(1)
        }

        return keys.sorted()
    }
}