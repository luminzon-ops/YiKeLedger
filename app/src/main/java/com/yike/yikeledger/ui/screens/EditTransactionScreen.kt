package com.yike.yikeledger.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yike.yikeledger.data.Category
import com.yike.yikeledger.data.TransactionType
import com.yike.yikeledger.ui.viewmodel.TransactionViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    transactionId: Long?,
    viewModel: TransactionViewModel = viewModel(),
    onBack: () -> Unit
) {
    val transactions = viewModel.transactions.collectAsState().value
    val accounts = viewModel.accounts.collectAsState().value
    
    // 查找要编辑的交易
    val transactionToEdit = transactions.find { it.id == transactionId }
    
    var description by remember { mutableStateOf(transactionToEdit?.description ?: "") }
    var amountText by remember { mutableStateOf(transactionToEdit?.amount?.toString() ?: "") }
    var selectedType by remember { mutableStateOf(transactionToEdit?.type ?: TransactionType.EXPENSE) }
    var selectedAccountId by remember { mutableStateOf(transactionToEdit?.accountId ?: accounts.firstOrNull()?.id ?: 1) }
    var originalDateTime by remember { mutableStateOf(transactionToEdit?.dateTime ?: "") }
    var isExpanded by remember { mutableStateOf(false) }
    var isAccountExpanded by remember { mutableStateOf(false) }

    // 加载分类数据
    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    // 分类相关状态
    var selectedCategory by remember { mutableStateOf(transactionToEdit?.category ?: "") }
    var isCategoryExpanded by remember { mutableStateOf(false) }
    // 加载分类数据
    val allCategories = viewModel.categories.collectAsState().value
    // 根据收入/支出类型筛选分类列表
    val categories = remember(selectedType, allCategories) {
        allCategories.filter { it.type == selectedType }
    }

    // 日期时间编辑状态
    var dateTimeText by remember { mutableStateOf(originalDateTime) }

    // 当交易数据加载时更新表单
    LaunchedEffect(transactionToEdit) {
        transactionToEdit?.let {
            description = it.description
            amountText = it.amount.toString()
            selectedType = it.type
            selectedAccountId = it.accountId
            originalDateTime = it.dateTime
            selectedCategory = it.category
            dateTimeText = it.dateTime
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("编辑交易") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    // 删除按钮
                    transactionId?.let { id ->
                        IconButton(
                            onClick = {
                                viewModel.deleteTransaction(id)
                                onBack()
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "删除", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("描述") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = !isExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = if (selectedType == TransactionType.INCOME) "收入" else "支出",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = { Text("类型") }
                )

                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("收入") },
                        onClick = {
                            selectedType = TransactionType.INCOME
                            isExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("支出") },
                        onClick = {
                            selectedType = TransactionType.EXPENSE
                            isExpanded = false
                        }
                    )
                }
            }

            // 分类选择器
            ExposedDropdownMenuBox(
                expanded = isCategoryExpanded,
                onExpandedChange = { isCategoryExpanded = !isCategoryExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = selectedCategory.ifEmpty { "请选择分类" },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = { Text("分类") }
                )

                DropdownMenu(
                    expanded = isCategoryExpanded,
                    onDismissRequest = { isCategoryExpanded = false }
                ) {
                    if (categories.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("暂无分类") },
                            onClick = { }
                        )
                    } else {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategory = category.name
                                    isCategoryExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // 日期时间输入
            OutlinedTextField(
                value = dateTimeText,
                onValueChange = { dateTimeText = it },
                label = { Text("时间 (yyyy-MM-dd HH:mm:ss)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 账户选择器
            ExposedDropdownMenuBox(
                expanded = isAccountExpanded,
                onExpandedChange = { isAccountExpanded = !isAccountExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = accounts.find { it.id == selectedAccountId }?.name ?: "现金",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAccountExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = { Text("账户") }
                )

                DropdownMenu(
                    expanded = isAccountExpanded,
                    onDismissRequest = { isAccountExpanded = false }
                ) {
                    accounts.forEach { account ->
                        DropdownMenuItem(
                            text = { Text(account.name) },
                            onClick = {
                                selectedAccountId = account.id
                                isAccountExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = amountText,
                onValueChange = { newValue ->
                    // 只允许数字和小数点
                    if (newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                        amountText = newValue
                    }
                },
                label = { Text("金额") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                singleLine = true,
                prefix = { Text("¥") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (description.isNotBlank() && amountText.isNotBlank()) {
                        val amount = amountText.toDoubleOrNull() ?: 0.0
                        if (amount > 0 && transactionId != null) {
                            // 使用更新交易方法，保留原时间
                            viewModel.updateTransaction(transactionId, description, selectedType, selectedCategory, amount, selectedAccountId, dateTimeText)
                            onBack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = description.isNotBlank() && amountText.isNotBlank() && amountText.toDoubleOrNull() ?: 0.0 > 0 && transactionId != null
            ) {
                Text("保存", style = MaterialTheme.typography.titleMedium)
            }

            // 提示信息
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("提示：", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text("• 支出金额自动记为负数", style = MaterialTheme.typography.bodySmall)
                Text("• 收入金额自动记为正数", style = MaterialTheme.typography.bodySmall)
                Text("• 余额 = 收入 - 支出", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}