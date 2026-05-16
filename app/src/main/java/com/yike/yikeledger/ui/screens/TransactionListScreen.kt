package com.yike.yikeledger.ui.screens

import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yike.yikeledger.data.AppSettingsManager
import com.yike.yikeledger.data.TransactionType
import com.yike.yikeledger.ui.viewmodel.TransactionViewModel
import com.yike.yikeledger.ui.components.LoadingState
import com.yike.yikeledger.ui.components.LoadingType
import com.yike.yikeledger.ui.components.Badge
import com.yike.yikeledger.ui.components.GradientCard
import com.yike.yikeledger.ui.components.StatCard
import com.yike.yikeledger.ui.components.ModernTransactionItem
import com.yike.yikeledger.ui.components.ModernFloatingActionButton
import com.yike.yikeledger.ui.components.EmptyState
import com.yike.yikeledger.ui.components.EnhancedEmptyState
import com.yike.yikeledger.ui.components.LoadingSkeleton
import com.yike.yikeledger.ui.components.GlassCard
import com.yike.yikeledger.ui.components.TransactionTypeBadge
import com.yike.yikeledger.ui.theme.AmountTypography
import com.yike.yikeledger.ui.theme.PrimaryGradientStart
import com.yike.yikeledger.ui.theme.PrimaryGradientEnd
import com.yike.yikeledger.ui.theme.IncomeColor
import com.yike.yikeledger.ui.theme.ExpenseColor
import com.yike.yikeledger.ui.theme.ChartColor1
import com.yike.yikeledger.ui.theme.ChartColor2
import com.yike.yikeledger.ui.theme.ChartColor3
import com.yike.yikeledger.ui.theme.ChartColor4
import com.yike.yikeledger.ui.theme.ChartColor5
import com.yike.yikeledger.ui.components.StaggeredListItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TransactionListScreen(
    viewModel: TransactionViewModel = viewModel(),
    onAddClick: (Offset) -> Unit = {},
    onEditTransaction: (Long) -> Unit
) {
    val transactions = viewModel.transactions.collectAsState().value
    val balance = viewModel.balance.collectAsState().value
    val fabPosition = remember { mutableStateOf(Offset.Zero) }
    val topAddPosition = remember { mutableStateOf(Offset.Zero) }
    val emptyAddPosition = remember { mutableStateOf(Offset.Zero) }

    // 监听数据变化，实时刷新
    LaunchedEffect(Unit) {
        viewModel.loadTransactions()
    }

    val isLoading = false // 这里可以根据实际需要添加加载状态
    
    // 计算统计数据
    val summary = remember(transactions) {
        val income = transactions.filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
        val expense = transactions.filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
        val count = transactions.size
        val average = if (count > 0) (income + expense) / count else 0.0
        TransactionSummary(income, expense, count, average)
    }
    val totalIncome = summary.totalIncome
    val totalExpense = summary.totalExpense
    val totalCount = summary.totalCount
    val averageAmount = summary.averageAmount
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "一刻记账",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    ) 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                actions = {
                    IconButton(
                        onClick = { onAddClick(topAddPosition.value) },
                        modifier = Modifier
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .onGloballyPositioned { coord ->
                                val rootPos = coord.localToRoot(Offset.Zero)
                                val size = coord.size
                                topAddPosition.value = Offset(rootPos.x + size.width / 2f, rootPos.y + size.height / 2f)
                            },
                        enabled = !isLoading
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "添加交易",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
        )},
        floatingActionButton = {
            if (!isLoading) {
                ModernFloatingActionButton(
                    onClick = { onAddClick(fabPosition.value) },
                    icon = Icons.Default.Add,
                    modifier = Modifier.onGloballyPositioned { coord ->
                        val rootPos = coord.localToRoot(Offset.Zero)
                        val size = coord.size
                        fabPosition.value = Offset(rootPos.x + size.width / 2f, rootPos.y + size.height / 2f)
                    },
                    contentDescription = "添加交易"
                )
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            LoadingState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        start = innerPadding.calculateLeftPadding(LocalLayoutDirection.current),
                        end = innerPadding.calculateRightPadding(LocalLayoutDirection.current),
                        bottom = innerPadding.calculateBottomPadding()
                    ),
                loadingType = LoadingType.Lottie,
                size = 64.dp,
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                message = "加载交易记录...",
                messageColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                showBackground = true,
                backgroundShape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
            )
        } else {
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
            // 总结余卡片 - 使用增强版渐变卡片
            item {
                GradientCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientStart = ChartColor1,
                    gradientEnd = ChartColor2,
                    gradientAngle = 135f, // 斜角渐变
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                    elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 12.dp),
                    borderWidth = 2.dp,
                    borderColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.2f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        androidx.compose.material3.Text(
                            text = "总结余",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.95f)
                        )
                        androidx.compose.material3.Text(
                            text = AppSettingsManager.formatAmount(balance),
                            style = AmountTypography.displayLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = androidx.compose.ui.graphics.Color.White,
                            letterSpacing = androidx.compose.ui.unit.TextUnit(0.5f, androidx.compose.ui.unit.TextUnitType.Sp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            androidx.compose.material3.Text(
                                text = "更新于今日",
                                style = MaterialTheme.typography.labelSmall,
                                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                            )
                            androidx.compose.material3.Text(
                                text = if (balance >= 0) "财务状况良好" else "注意支出",
                                style = MaterialTheme.typography.labelSmall,
                                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // 统计卡片行 - 使用增强版统计卡片
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "总收入",
                        value = AppSettingsManager.formatAmount(totalIncome),
                        description = "累计收入",
                        valueColor = IncomeColor,
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 6.dp),
                        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                        showBorder = true,
                        borderColor = IncomeColor.copy(alpha = 0.2f),
                        borderWidth = 1.5.dp
                    )

                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "总支出",
                        value = AppSettingsManager.formatAmount(totalExpense),
                        description = "累计支出",
                        valueColor = ExpenseColor,
                        icon = Icons.AutoMirrored.Filled.TrendingDown,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 6.dp),
                        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                        showBorder = true,
                        borderColor = ExpenseColor.copy(alpha = 0.2f),
                        borderWidth = 1.5.dp
                    )
                }
            }

            // 交易数量统计 - 使用增强版统计卡片
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "交易总数",
                        value = "$totalCount",
                        description = "笔",
                        valueColor = ChartColor3,
                        icon = null,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp),
                        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                        showBorder = true,
                        borderColor = ChartColor3.copy(alpha = 0.1f),
                        borderWidth = 1.dp
                    )

                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "平均金额",
                        value = AppSettingsManager.formatAmount(averageAmount),
                        description = "每笔交易",
                        valueColor = ChartColor4,
                        icon = null,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp),
                        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                        showBorder = true,
                        borderColor = ChartColor4.copy(alpha = 0.1f),
                        borderWidth = 1.dp
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        androidx.compose.material3.Text(
                            text = "最近交易",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                        )

                        if (transactions.isNotEmpty()) {
                            Badge(
                                text = "共${transactions.size}笔",
                                modifier = Modifier,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                textColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    // 分割线
                    androidx.compose.material3.HorizontalDivider(
                        color = androidx.compose.material3.MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (transactions.isEmpty()) {
                item {
                    EnhancedEmptyState(
                        title = "暂无交易记录",
                        description = "开始记录你的第一笔交易，跟踪个人财务状况",
                        icon = Icons.AutoMirrored.Filled.ArrowForward,
                        actionText = "添加交易",
                        onAction = { onAddClick(emptyAddPosition.value) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coord ->
                                val rootPos = coord.localToRoot(Offset.Zero)
                                val size = coord.size
                                emptyAddPosition.value = Offset(rootPos.x + size.width / 2f, rootPos.y + size.height / 2f)
                            }
                    )
                }
            } else {
                itemsIndexed(
                    items = transactions,
                    key = { _, transaction -> transaction.id }
                ) { index, transaction ->
                    StaggeredListItem(
                        index = index,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        ModernTransactionItem(
                            title = transaction.description,
                            amount = transaction.amount,
                            type = if (transaction.type == TransactionType.INCOME) "income" else "expense",
                            date = transaction.dateTime,
                            category = transaction.category,
                            description = "",
                            onClick = { onEditTransaction(transaction.id) }
                        )
                    }
                }
            }
        }
    }
}

}

private data class TransactionSummary(
    val totalIncome: Double,
    val totalExpense: Double,
    val totalCount: Int,
    val averageAmount: Double
)
