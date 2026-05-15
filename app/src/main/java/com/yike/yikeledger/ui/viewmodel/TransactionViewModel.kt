package com.yike.yikeledger.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yike.yikeledger.data.Account
import com.yike.yikeledger.data.AccountType
import com.yike.yikeledger.data.AccountRepository
import com.yike.yikeledger.data.Transaction
import com.yike.yikeledger.data.TransactionRepository
import com.yike.yikeledger.data.PeriodStat
import com.yike.yikeledger.data.StatPeriod
import com.yike.yikeledger.data.TransactionType
import com.yike.yikeledger.data.Category
import com.yike.yikeledger.data.CategoryRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val accountRepository = AccountRepository(application.applicationContext)
    private val repository = TransactionRepository(application.applicationContext, accountRepository)
    private val categoryRepository = CategoryRepository(application.applicationContext)


    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance.asStateFlow()

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    init {
        loadTransactions()
        loadAccounts()
        loadCategories()
    }

    fun loadTransactions() {
        viewModelScope.launch {
            val loaded = repository.loadTransactions()
            _transactions.value = loaded
            updateBalance(loaded)
        }
    }

    fun loadAccounts() {
        viewModelScope.launch {
            val loaded = accountRepository.loadAccounts()
            _accounts.value = loaded
        }
    }

    fun addAccount(name: String, type: AccountType, initialBalance: Double = 0.0) {
        viewModelScope.launch {
            val account = Account(name = name, type = type, initialBalance = initialBalance)
            accountRepository.addAccount(account)
            loadAccounts()
        }
    }

    fun updateAccount(account: Account) {
        viewModelScope.launch {
            accountRepository.updateAccount(account)
            loadAccounts()
        }
    }

    fun deleteAccount(id: Long) {
        viewModelScope.launch {
            accountRepository.deleteAccount(id)
            loadAccounts()
        }
    }

    fun addTransaction(description: String, type: TransactionType, category: String, amount: Double, accountId: Long, dateTime: String? = null) {
        viewModelScope.launch {
            val transaction = Transaction(
                description = description,
                type = type,
                category = category,
                amount = amount,
                accountId = accountId,
                dateTime = dateTime ?: LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
            repository.addTransaction(transaction)
            loadTransactions() // 重新加载
        }
    }

    fun updateTransaction(id: Long, description: String, type: TransactionType, category: String, amount: Double, accountId: Long, dateTime: String) {
        viewModelScope.launch {
            val transaction = Transaction(
                id = id,
                dateTime = dateTime,
                description = description,
                type = type,
                category = category,
                amount = amount,
                accountId = accountId
            )
            repository.updateTransaction(transaction)
            loadTransactions()
        }
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            repository.deleteTransaction(id)
            loadTransactions()
        }
    }

    fun getStatsByPeriod(period: StatPeriod): List<PeriodStat> {
        return repository.getStatsByPeriod(period)
    }

    fun getStatsByDateRange(startDate: String, endDate: String, period: StatPeriod): List<PeriodStat> {
        return repository.getStatsByDateRange(startDate, endDate, period)
    }

    // 分类管理方法
    fun loadCategories() {
        viewModelScope.launch {
            val loaded = categoryRepository.loadCategories()
            _categories.value = loaded
        }
    }

    fun addCategory(name: String, type: TransactionType) {
        viewModelScope.launch {
            val category = Category(name = name, type = type)
            categoryRepository.addCategory(category)
            loadCategories()
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.updateCategory(category)
            loadCategories()
        }
    }

    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(id)
            loadCategories()
        }
    }

    fun getCategoriesByType(type: TransactionType): List<Category> {
        return _categories.value.filter { it.type == type }
    }





    private fun updateBalance(transactions: List<Transaction>) {
        _balance.value = transactions.sumOf { it.signedAmount() }
    }
}