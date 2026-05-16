package com.yike.yikeledger.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yike.yikeledger.ui.viewmodel.TransactionViewModel
import com.yike.yikeledger.data.AppSettingsManager
import com.yike.yikeledger.data.AccountType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(
    accountId: Long?,
    viewModel: TransactionViewModel = viewModel(),
    onBack: () -> Unit
) {
    val accounts = viewModel.accounts.collectAsState().value
    
    // 查找要编辑的账户
    val accountToEdit = accounts.find { it.id == accountId }
    
    var name by remember { mutableStateOf(accountToEdit?.name ?: "") }
    var isSaving by remember { mutableStateOf(false) }

    // 当账户数据加载时更新表单
    LaunchedEffect(accountToEdit) {
        accountToEdit?.let {
            name = it.name
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("编辑账户") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    // 删除按钮
                    accountId?.let { id ->
                        IconButton(
                            onClick = {
                                viewModel.deleteAccount(id)
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
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 账户信息卡片（只读信息）
            accountToEdit?.let { account ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("账户信息", fontSize = 16.sp, fontWeight = MaterialTheme.typography.titleMedium.fontWeight)
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // 类型信息
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("账户类型", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                getAccountTypeName(account.type),
                                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // 当前余额
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("当前余额", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                AppSettingsManager.formatAmount(account.currentBalance),
                                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                                color = if (account.currentBalance >= 0) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.error
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // 创建时间
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("创建时间", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(account.createdAt)
                        }
                    }
                }
            }

            // 编辑表单
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("账户名称") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && accountToEdit != null) {
                        isSaving = true
                        // 更新账户信息
                        val updatedAccount = accountToEdit.copy(name = name)
                        viewModel.updateAccount(updatedAccount)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && accountToEdit != null && name != accountToEdit.name
            ) {
                Text("保存", style = MaterialTheme.typography.titleMedium)
            }

            // 提示信息
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("提示：", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text("• 账户创建后无法修改类型", style = MaterialTheme.typography.bodySmall)
                Text("• 当前余额根据交易自动计算", style = MaterialTheme.typography.bodySmall)
                Text("• 删除账户将同时删除相关交易记录", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

