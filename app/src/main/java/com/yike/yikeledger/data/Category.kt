package com.yike.yikeledger.data

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val type: TransactionType
) {
    companion object {
        // 默认收入分类
        val defaultIncomeCategories = listOf(
            Category(name = "工资", type = TransactionType.INCOME),
            Category(name = "奖金", type = TransactionType.INCOME),
            Category(name = "投资回报", type = TransactionType.INCOME),
            Category(name = "兼职", type = TransactionType.INCOME),
            Category(name = "其他收入", type = TransactionType.INCOME)
        )

        // 默认支出分类
        val defaultExpenseCategories = listOf(
            Category(name = "餐饮", type = TransactionType.EXPENSE),
            Category(name = "交通", type = TransactionType.EXPENSE),
            Category(name = "购物", type = TransactionType.EXPENSE),
            Category(name = "住房", type = TransactionType.EXPENSE),
            Category(name = "娱乐", type = TransactionType.EXPENSE),
            Category(name = "医疗", type = TransactionType.EXPENSE),
            Category(name = "教育", type = TransactionType.EXPENSE),
            Category(name = "其他支出", type = TransactionType.EXPENSE)
        )

        // 获取所有分类
        fun allCategories(): List<Category> = defaultIncomeCategories + defaultExpenseCategories

        // 根据类型获取分类
        fun getCategoriesByType(type: TransactionType): List<Category> =
            allCategories().filter { it.type == type }
    }
}