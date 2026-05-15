package com.yike.yikeledger.data

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class AccountRepository(private val context: Context) {
    private val fileName = "accounts.json"
    private val json = Json { prettyPrint = true }

    private fun getDataFile(): File = File(context.filesDir, fileName)

    fun loadAccounts(): List<Account> {
        return try {
            val file = getDataFile()
            if (!file.exists()) {
                // 首次启动，创建默认现金账户
                val defaultAccount = Account(
                    id = 1,
                    name = "现金",
                    type = AccountType.CASH,
                    initialBalance = 0.0,
                    currentBalance = 0.0
                )
                saveAccounts(listOf(defaultAccount))
                return listOf(defaultAccount)
            }
            val jsonString = file.readText()
            json.decodeFromString<List<Account>>(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun saveAccounts(accounts: List<Account>) {
        try {
            val file = getDataFile()
            val jsonString = json.encodeToString(accounts)
            file.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addAccount(account: Account) {
        val accounts = loadAccounts().toMutableList()
        accounts.add(account)
        saveAccounts(accounts)
    }

    fun updateAccount(updatedAccount: Account) {
        val accounts = loadAccounts().toMutableList()
        val index = accounts.indexOfFirst { it.id == updatedAccount.id }
        if (index != -1) {
            accounts[index] = updatedAccount
            saveAccounts(accounts)
        }
    }

    fun deleteAccount(id: Long) {
        val accounts = loadAccounts().toMutableList()
        accounts.removeIf { it.id == id }
        saveAccounts(accounts)
    }

    fun getAccount(id: Long): Account? {
        return loadAccounts().find { it.id == id }
    }

    fun getAccountByName(name: String): Account? {
        return loadAccounts().find { it.name == name }
    }

    fun updateAccountBalance(accountId: Long, transaction: Transaction, isDelete: Boolean = false) {
        val account = getAccount(accountId) ?: return
        val updatedAccount = if (isDelete) {
            account.updateBalanceAfterDelete(transaction)
        } else {
            account.updateBalance(transaction)
        }
        updateAccount(updatedAccount)
    }
}