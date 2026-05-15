package com.yike.yikeledger.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import android.graphics.Paint as AndroidPaint
import android.graphics.Color as AndroidColor
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yike.yikeledger.data.PeriodStat
import com.yike.yikeledger.data.StatPeriod
import com.yike.yikeledger.ui.viewmodel.TransactionViewModel
import com.yike.yikeledger.ui.components.GradientCard
import com.yike.yikeledger.ui.components.StatCard
import com.yike.yikeledger.ui.components.ModernTransactionItem
import com.yike.yikeledger.ui.components.EnhancedEmptyState
import com.yike.yikeledger.ui.components.StaggeredListItem
import com.yike.yikeledger.ui.components.FadeScaleInContent
import com.yike.yikeledger.ui.components.ProgressIndicator
import com.yike.yikeledger.ui.theme.AmountTypography
import com.yike.yikeledger.ui.theme.PrimaryGradientStart
import com.yike.yikeledger.ui.theme.PrimaryGradientEnd
import com.yike.yikeledger.ui.theme.SecondaryGradientStart
import com.yike.yikeledger.ui.theme.SecondaryGradientEnd
import com.yike.yikeledger.ui.theme.IncomeColor
import com.yike.yikeledger.ui.theme.ExpenseColor
import com.yike.yikeledger.ui.theme.InfoBlue

enum class ChartType {
    INCOME,     // 收入
    EXPENSE,    // 支出
    NET         // 净额
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(viewModel: TransactionViewModel = viewModel()) {
    var selectedPeriod by remember { mutableStateOf(StatPeriod.DAILY) }
    var selectedChart by remember { mutableStateOf(ChartType.INCOME) }
    var useCustomDateRange by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    // 根据选择模式获取统计数据
    val stats = if (useCustomDateRange && startDate.isNotBlank() && endDate.isNotBlank()) {
        viewModel.getStatsByDateRange(startDate, endDate, selectedPeriod)
    } else {
        viewModel.getStatsByPeriod(selectedPeriod)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("统计报表", style = MaterialTheme.typography.titleLarge) }
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
            // 统计周期选择器 - 现代化卡片设计
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "统计周期",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatPeriod.entries.forEach { period ->
                                val isSelected = selectedPeriod == period
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedPeriod = period }
                                        .background(
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = getPeriodDisplayName(period),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 日期范围选择器 - 现代化设计
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "自定义日期范围",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "选择特定时间段进行分析",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = useCustomDateRange,
                                onCheckedChange = { useCustomDateRange = it },
                                thumbContent = if (useCustomDateRange) {
                                    {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.primary)
                                        )
                                    }
                                } else {
                                    null
                                }
                            )
                        }

                        if (useCustomDateRange) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.animateContentSize()
                            ) {
                                OutlinedTextField(
                                    value = startDate,
                                    onValueChange = { startDate = it },
                                    label = {
                                        Text("开始日期", style = MaterialTheme.typography.labelMedium)
                                    },
                                    placeholder = { Text("yyyy-MM-dd", color = MaterialTheme.colorScheme.outline) },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                                    )
                                )
                                OutlinedTextField(
                                    value = endDate,
                                    onValueChange = { endDate = it },
                                    label = {
                                        Text("结束日期", style = MaterialTheme.typography.labelMedium)
                                    },
                                    placeholder = { Text("yyyy-MM-dd", color = MaterialTheme.colorScheme.outline) },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 图表类型选择器 - 现代化设计
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "图表类型",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ChartType.entries.forEach { chartType ->
                                val isSelected = selectedChart == chartType
                                val chartColor = when (chartType) {
                                    ChartType.INCOME -> IncomeColor
                                    ChartType.EXPENSE -> ExpenseColor
                                    ChartType.NET -> InfoBlue
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedChart = chartType }
                                        .background(
                                            color = if (isSelected) chartColor else MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when (chartType) {
                                            ChartType.INCOME -> "收入"
                                            ChartType.EXPENSE -> "支出"
                                            ChartType.NET -> "净额"
                                        },
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 汇总卡片 - 使用渐变卡片
            item {
                val totalIncome = stats.sumOf { it.income }
                val totalExpense = stats.sumOf { it.expense }
                val net = totalIncome - totalExpense

                GradientCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientStart = PrimaryGradientStart,
                    gradientEnd = PrimaryGradientEnd
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = "汇总统计",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // 收入
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "收入",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = String.format("%.2f", totalIncome),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            // 分隔线
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(40.dp)
                                    .background(Color.White.copy(alpha = 0.2f))
                            )

                            // 支出
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "支出",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = String.format("%.2f", totalExpense),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            // 分隔线
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(40.dp)
                                    .background(Color.White.copy(alpha = 0.2f))
                            )

                            // 净额
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "净额",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = String.format("%.2f", net),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (net >= 0) Color.White else Color(0xFFFF6B6B)
                                )
                            }
                        }
                    }
                }
            }

            // 趋势图
            if (stats.isNotEmpty()) {
                item {
                    FadeScaleInContent {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    when (selectedChart) {
                                        ChartType.INCOME -> "收入趋势图"
                                        ChartType.EXPENSE -> "支出趋势图"
                                        ChartType.NET -> "净额趋势图"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                LineChart(
                                    stats = stats.takeLast(10),
                                    period = selectedPeriod,
                                    chartType = selectedChart
                                )
                            }
                        }
                    }
                }
            }

            // 详细统计列表标题
            item {
                Text("详细记录", style = MaterialTheme.typography.titleLarge)
            }

            if (stats.isEmpty()) {
                item {
                    EnhancedEmptyState(
                        title = "暂无统计数据",
                        description = "添加一些交易记录后，这里将展示详细的统计图表",
                        icon = Icons.Default.BarChart,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                items(stats.reversed().size) { index ->
                    val stat = stats.reversed()[index]
                    StaggeredListItem(
                        index = index,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StatItem(stat = stat, period = selectedPeriod)
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(stat: PeriodStat, period: StatPeriod) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                Text(
                    text = formatPeriodDisplay(stat.period, period),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formatCurrency(stat.net),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (stat.net >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("收入", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        formatCurrency(stat.income),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column {
                    Text("支出", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        formatCurrency(stat.expense),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

fun getPeriodDisplayName(period: StatPeriod): String {
    return when (period) {
        StatPeriod.DAILY -> "日"
        StatPeriod.WEEKLY -> "周"
        StatPeriod.MONTHLY -> "月"
        StatPeriod.YEARLY -> "年"
    }
}

fun formatPeriodDisplay(periodKey: String, period: StatPeriod): String {
    return when (period) {
        StatPeriod.DAILY -> periodKey
        StatPeriod.WEEKLY -> periodKey.replace("_week", "") + " 周"
        StatPeriod.MONTHLY -> periodKey + "月"
        StatPeriod.YEARLY -> periodKey + "年"
    }
}

@Composable
fun LineChart(stats: List<PeriodStat>, period: StatPeriod, chartType: ChartType) {
    // 如果数据不足，创建虚拟数据点以确保图表可以绘制
    val displayStats = if (stats.isEmpty()) {
        // 创建两个默认数据点
        listOf(
            PeriodStat("无数据1"),
            PeriodStat("无数据2")
        )
    } else if (stats.size < 2) {
        // 如果只有一个数据点，复制它来创建第二个数据点
        val singleStat = stats.first()
        listOf(
            singleStat,
            PeriodStat("${singleStat.period}_2", 0.0, 0.0)
        )
    } else {
        stats
    }
    
    // 根据图表类型选择数据和颜色
    val data = when (chartType) {
        ChartType.INCOME -> displayStats.map { it.income }
        ChartType.EXPENSE -> displayStats.map { it.expense }
        ChartType.NET -> displayStats.map { it.net }
    }
    
    val lineColor = when (chartType) {
        ChartType.INCOME -> IncomeColor
        ChartType.EXPENSE -> ExpenseColor
        ChartType.NET -> InfoBlue
    }
    
    val maxValue = data.maxOrNull() ?: 1.0
    val chartHeight = 200.dp
    val chartPadding = 30.dp // 为坐标轴标签留出空间
    val leftPadding = 40.dp // 左侧边距（包含纵坐标标签）

    // 选中的数据点索引
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight)
                .padding(vertical = 4.dp)
        ) {
            // Y轴标签（左侧）
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxHeight()
                    .padding(vertical = 15.dp)
                    .width(36.dp) // 纵坐标标签宽度
                    .padding(start = 2.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 显示最大值、中间值和0
                val maxDisplay = maxValue.coerceAtLeast(1.0)
                Text(
                    formatCurrencyNoDecimal(maxDisplay),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    formatCurrencyNoDecimal(maxDisplay / 2),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "0",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            val axisColor = MaterialTheme.colorScheme.onSurfaceVariant
            val gridColor = MaterialTheme.colorScheme.outline

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { tapOffset ->
                            // 计算点击位置对应的数据点索引
                            val canvasWidth = size.width - leftPadding.toPx() - chartPadding.toPx()
                            val startX = leftPadding.toPx()
                            val pointWidth = canvasWidth / (displayStats.size - 1).coerceAtLeast(1)

                            val relativeX = tapOffset.x - startX
                            val clickedIndex = (relativeX / pointWidth).toInt().coerceIn(0, displayStats.size - 1)
                            selectedIndex = clickedIndex
                        }
                    }
            ) {
                val canvasWidth = size.width - leftPadding.toPx() - chartPadding.toPx()
                val canvasHeight = size.height - chartPadding.toPx() * 2
                val startX = leftPadding.toPx()
                val startY = chartPadding.toPx()

                // 绘制坐标轴
                drawLine(
                    color = axisColor,
                    start = Offset(startX, startY + canvasHeight),
                    end = Offset(startX + canvasWidth, startY + canvasHeight),
                    strokeWidth = 2f
                )

                drawLine(
                    color = axisColor,
                    start = Offset(startX, startY),
                    end = Offset(startX, startY + canvasHeight),
                    strokeWidth = 2f
                )

                // 绘制水平虚线（Y轴刻度线）
                val dashPattern = floatArrayOf(10f, 5f)
                // y = maxValue (顶部)
                drawLine(
                    color = gridColor,
                    start = Offset(startX, startY),
                    end = Offset(startX + canvasWidth, startY),
                    strokeWidth = 1f,
                    pathEffect = PathEffect.dashPathEffect(dashPattern, 0f)
                )
                // y = maxValue / 2 (中间)
                drawLine(
                    color = gridColor,
                    start = Offset(startX, startY + canvasHeight / 2),
                    end = Offset(startX + canvasWidth, startY + canvasHeight / 2),
                    strokeWidth = 1f,
                    pathEffect = PathEffect.dashPathEffect(dashPattern, 0f)
                )
                // y = 0 (底部，与X轴重合，可以不单独绘制)
                // drawLine(
                //     color = MaterialTheme.colorScheme.outline,
                //     start = Offset(startX, startY + canvasHeight),
                //     end = Offset(startX + canvasWidth, startY + canvasHeight),
                //     strokeWidth = 1f,
                //     pathEffect = PathEffect.dashPathEffect(dashPattern, 0f)
                // )

                // 计算数据点位置
                val points = data.mapIndexed { index, value ->
                    val x = startX + (canvasWidth / (displayStats.size - 1).coerceAtLeast(1)) * index
                    val y = startY + canvasHeight - (value / maxValue).toFloat() * canvasHeight
                    Offset(x, y)
                }
                
                // 绘制折线
                if (points.size > 1) {
                    for (i in 0 until points.size - 1) {
                        drawLine(
                            color = lineColor,
                            start = points[i],
                            end = points[i + 1],
                            strokeWidth = 6f
                        )
                    }
                }
                
                // 绘制数据点
                points.forEach { point ->
                    drawCircle(
                        color = lineColor,
                        radius = 4f,
                        center = point
                    )
                }
                
                // 绘制选中点的指示线
                selectedIndex?.let { index ->
                    if (index >= 0 && index < points.size) {
                        val selectedPoint = points[index]
                        
                        // 垂直指示线
                        drawLine(
                            color = Color(0xFFFF9800),
                            start = Offset(selectedPoint.x, startY),
                            end = Offset(selectedPoint.x, startY + canvasHeight),
                            strokeWidth = 2f
                        )
                        
                        // 指示框背景
                        val boxWidth = 160f
                        val boxHeight = 72f
                        val boxTop = startY - boxHeight - 10f
                        val boxLeft = selectedPoint.x - boxWidth / 2

                        drawRect(
                            color = Color(0xFF333333),
                            topLeft = Offset(boxLeft, boxTop),
                            size = Size(boxWidth, boxHeight)
                        )

                        // 指示框文字
                        val value = data[index]
                        val displayText = formatCurrency(value)

                        drawContext.canvas.nativeCanvas.drawText(
                            displayText,
                            selectedPoint.x,
                            boxTop + boxHeight / 2 + 10f,
                            AndroidPaint().apply {
                                color = AndroidColor.WHITE
                                textSize = 36f
                                textAlign = AndroidPaint.Align.CENTER
                                isAntiAlias = true
                            }
                        )
                    }
                }
            }
            
            // X轴标签（底部）
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = leftPadding, end = chartPadding, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 显示首尾和中间的时间标签
                if (displayStats.size > 0) {
                    Text(
                        formatPeriodDisplay(displayStats.first().period, period),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
                if (displayStats.size > 1) {
                    Text(
                        formatPeriodDisplay(displayStats[displayStats.size / 2].period, period),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
                if (displayStats.size > 2) {
                    Text(
                        formatPeriodDisplay(displayStats.last().period, period),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 图例
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(lineColor)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                when (chartType) {
                    ChartType.INCOME -> "收入"
                    ChartType.EXPENSE -> "支出"
                    ChartType.NET -> "净额"
                },
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatCurrency(value: Double): String {
    return try {
        if (value.isNaN() || value.isInfinite()) {
            "¥0.00"
        } else {
            "¥${String.format("%.2f", value)}"
        }
    } catch (e: Exception) {
        "¥0.00"
    }
}

private fun formatCurrencyNoDecimal(value: Double): String {
    return try {
        if (value.isNaN() || value.isInfinite()) {
            "0"
        } else {
            "${String.format("%.0f", value)}"
        }
    } catch (e: Exception) {
        "0"
    }
}