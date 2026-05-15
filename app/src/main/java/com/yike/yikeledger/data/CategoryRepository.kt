package com.yike.yikeledger.data

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class CategoryRepository(private val context: Context) {
    private val fileName = "categories.json"
    private val json = Json { prettyPrint = true }

    private fun getDataFile(): File = File(context.filesDir, fileName)

    fun loadCategories(): List<Category> {
        return try {
            val file = getDataFile()
            if (!file.exists()) {
                // 首次启动，创建默认分类
                val defaultCategories = Category.defaultIncomeCategories + Category.defaultExpenseCategories
                saveCategories(defaultCategories)
                return defaultCategories
            }
            val jsonString = file.readText()
            json.decodeFromString<List<Category>>(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun saveCategories(categories: List<Category>) {
        try {
            val file = getDataFile()
            val jsonString = json.encodeToString(categories)
            file.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addCategory(category: Category) {
        val categories = loadCategories().toMutableList()
        categories.add(category)
        saveCategories(categories)
    }

    fun updateCategory(updatedCategory: Category) {
        val categories = loadCategories().toMutableList()
        val index = categories.indexOfFirst { it.id == updatedCategory.id }
        if (index != -1) {
            categories[index] = updatedCategory
            saveCategories(categories)
        }
    }

    fun deleteCategory(id: Long) {
        val categories = loadCategories().toMutableList()
        categories.removeIf { it.id == id }
        saveCategories(categories)
    }

    fun getCategory(id: Long): Category? {
        return loadCategories().find { it.id == id }
    }

    fun getCategoriesByType(type: TransactionType): List<Category> {
        return loadCategories().filter { it.type == type }
    }

    fun getCategoryByName(name: String, type: TransactionType? = null): Category? {
        return loadCategories().find {
            it.name == name && (type == null || it.type == type)
        }
    }
}