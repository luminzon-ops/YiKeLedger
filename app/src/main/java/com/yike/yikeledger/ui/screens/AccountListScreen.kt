package com.yike.yikeledger.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yike.yikeledger.data.AccountType
import com.yike.yikeledger.ui.viewmodel.TransactionViewModel
import com.yike.yikeledger.ui.components.GradientCard
import com.yike.yikeledger.ui.components.AccountCard
import com.yike.yikeledger.ui.components.ModernFloatingActionButton
import com.yike.yikeledger.ui.components.EmptyState
import com.yike.yikeledger.ui.theme.AmountTypography
import com.yike.yikeledger.ui.theme.PrimaryGradientStart
import com.yike.yikeledger.ui.theme.PrimaryGradientEnd
import com.yike.yikeledger.ui.theme.SecondaryGradientStart
import com.yike.yikeledger.ui.theme.SecondaryGradientEnd

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountListScreen(
    navController: NavController,
    viewModel: TransactionViewModel = viewModel(),
    onAddClick: () -> Unit,
    onEditAccount: (Long) -> Unit
) {
    val accounts = viewModel.accounts.collectAsState().value
    
    // 监听数据变化，实时刷新
    LaunchedEffect(Unit) {
        viewModel.loadAccounts()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("账户管理", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(
                        onClick = onAddClick,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "添加账户")
                    }
                }
            )
        },
        floatingActionButton = {
            ModernFloatingActionButton(
                onClick = onAddClick,
                icon = Icons.Default.Add,
                contentDescription = "添加账户"
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateLeftPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateRightPadding(LocalLayoutDirection.current),
                    bottom = innerPadding.calculateBottomPadding()
                ),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 总余额卡片 - 使用渐变卡片
            item {
                val totalBalance = accounts.sumOf { it.currentBalance }
                GradientCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientStart = PrimaryGradientStart,
                    gradientEnd = PrimaryGradientEnd
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "当前总余额",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Text(
                            text = "¥${String.format("%.2f", totalBalance)}",
                            style = AmountTypography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${accounts.size}个账户",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "账户列表",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            if (accounts.isEmpty()) {
                item {
                    EmptyState(
                        title = "暂无账户",
                        description = "点击右下角按钮添加你的第一个账户",
                        icon = Icons.Default.Add
                    )
                }
            } else {
                items(accounts) { account ->
                    AccountCard(
                        name = account.name,
                        balance = account.currentBalance.toDouble(),
                        type = getAccountTypeName(account.type),
                        color = account.color.toLong(),
                        onClick = { onEditAccount(account.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun AccountItem(
    account: com.yike.yikeledger.data.Account,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 账户颜色标识
            Spacer(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .padding(end = 8.dp)
                    .background(Color(account.color))
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row {
                    Text(
                        text = account.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = getAccountTypeName(account.type),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "余额: ¥${String.format("%.2f", account.currentBalance)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (account.currentBalance >= 0)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "编辑", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

fun getAccountTypeName(type: AccountType): String {
    return when (type) {
        AccountType.CASH -> "现金"
        AccountType.BANK_CARD -> "银行卡"
        AccountType.ALIPAY -> "支付宝"
        AccountType.WECHAT -> "微信"
        AccountType.CREDIT_CARD -> "信用卡"
        AccountType.OTHER -> "其他"
    }
}