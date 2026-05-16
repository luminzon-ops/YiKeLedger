package com.yike.yikeledger.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yike.yikeledger.data.Category
import com.yike.yikeledger.data.TransactionType
import com.yike.yikeledger.ui.viewmodel.TransactionViewModel
import com.yike.yikeledger.ui.components.PrimaryButton
import com.yike.yikeledger.ui.components.ModernTextField
import com.yike.yikeledger.ui.components.AmountInputField
import com.yike.yikeledger.ui.components.DatePickerDialog
import com.yike.yikeledger.ui.components.GradientCard
import com.yike.yikeledger.ui.components.GlassCard
import com.yike.yikeledger.ui.components.Badge
import com.yike.yikeledger.ui.components.TransactionTypeBadge
import com.yike.yikeledger.ui.theme.IncomeColor
import com.yike.yikeledger.ui.theme.ExpenseColor
import com.yike.yikeledger.ui.screens.TransactionTypeSelector
import com.yike.yikeledger.ui.components.LottieStateAnimation
import com.yike.yikeledger.R
import java.time.LocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: TransactionViewModel = viewModel(),
    onBack: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var isExpanded by remember { mutableStateOf(false) }

    val accounts = viewModel.accounts.collectAsState().value
    var selectedAccountId by remember { mutableStateOf(accounts.firstOrNull()?.id ?: 1) }
    var isAccountExpanded by remember { mutableStateOf(false) }

    // 加载分类数据
    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    // 分类相关状态
    var selectedCategory by remember { mutableStateOf("") }
    var isCategoryExpanded by remember { mutableStateOf(false) }
    // 加载分类数据
    val allCategories = viewModel.categories.collectAsState().value
    // 根据收入/支出类型筛选分类列表
    val categories = remember(selectedType, allCategories) {
        allCategories.filter { it.type == selectedType }
    }

    // 日期时间相关状态
    var dateTimeText by remember {
        mutableStateOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
    }
    var showDatePickerDialog by remember { mutableStateOf(false) }

    // 表单验证状态
    val isFormValid = description.isNotBlank() && 
                     amountText.isNotBlank() && 
                     amountText.toDoubleOrNull() ?: 0.0 > 0 &&
                     selectedCategory.isNotBlank()
    
    // 保存加载状态
    var isLoading by remember { mutableStateOf(false) }

    // 表单验证错误状态
    var showValidationError by remember { mutableStateOf(false) }
    var showErrorToast by remember { mutableStateOf(false) }

    // 保存成功动画状态
    var showSuccess by remember { mutableStateOf(false) }

    // 协程作用域
    val scope = rememberCoroutineScope()
    
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color(0xFFF0F4FF),
            topBar = {
            MediumTopAppBar(
                title = { 
                    Text("添加交易", style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )) 
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "返回",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 交易类型选择卡片 - 颜色联动版本（收入=红色，支出=绿色）

            // 根据选择类型定义动态颜色
            val cardBackgroundColor = when(selectedType) {
                TransactionType.INCOME -> {
                    // 收入状态：极淡红色背景
                    Brush.verticalGradient(
                        colors = listOf(
                            IncomeColor.copy(alpha = 0.08f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                }
                TransactionType.EXPENSE -> {
                    // 支出状态：极淡绿色背景
                    Brush.verticalGradient(
                        colors = listOf(
                            ExpenseColor.copy(alpha = 0.08f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }

            val cardBorderColor = when(selectedType) {
                TransactionType.INCOME -> IncomeColor.copy(alpha = 0.25f)
                TransactionType.EXPENSE -> ExpenseColor.copy(alpha = 0.25f)
            }

            // 动态阴影颜色
            val shadowColor = when(selectedType) {
                TransactionType.INCOME -> IncomeColor.copy(alpha = 0.1f)
                TransactionType.EXPENSE -> ExpenseColor.copy(alpha = 0.1f)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                        ambientColor = shadowColor,
                        spotColor = when(selectedType) {
                            TransactionType.INCOME -> IncomeColor.copy(alpha = 0.15f)
                            TransactionType.EXPENSE -> ExpenseColor.copy(alpha = 0.15f)
                        }
                    ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = cardBorderColor
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cardBackgroundColor)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "交易类型",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    TransactionTypeSelector(
                        selectedType = selectedType,
                        onTypeSelected = { newType ->
                            selectedType = newType
                        }
                    )
                }
            }

            // 基本信息卡片
            GradientCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                gradientStart = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                gradientEnd = MaterialTheme.colorScheme.surface,
                gradientAngle = 45f,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "交易详情",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    ModernTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = "描述",
                        modifier = Modifier.fillMaxWidth(),
                        isError = description.isBlank() && description.isNotEmpty(),
                        errorMessage = if (description.isBlank() && description.isNotEmpty()) "请输入交易描述" else null
                    )
                    
                    AmountInputField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = "金额",
                        modifier = Modifier.fillMaxWidth(),
                        currencySymbol = "¥",
                        showCurrencySymbol = true,
                        decimalPlaces = 2,
                        minValue = 0.01,
                        isError = amountText.isNotBlank() && (amountText.toDoubleOrNull() ?: 0.0) <= 0,
                        errorMessage = if (amountText.isNotBlank() && (amountText.toDoubleOrNull() ?: 0.0) <= 0) "金额必须大于0" else null,
                        helperText = "输入交易金额",
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    )
                }
            }

            // 分类和账户卡片
            GradientCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                gradientStart = MaterialTheme.colorScheme.primaryContainer,
                gradientEnd = MaterialTheme.colorScheme.surface,
                gradientAngle = 90f,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "分类和账户",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    // 分类选择器 - 增强版
                    ExposedDropdownMenuBox(
                        expanded = isCategoryExpanded,
                        onExpandedChange = { isCategoryExpanded = !isCategoryExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        androidx.compose.material3.OutlinedTextField(
                            value = selectedCategory.ifEmpty { "请选择分类" },
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryExpanded) },
                            leadingIcon = {
                                androidx.compose.material3.Icon(
                                    imageVector = Icons.Filled.Category,
                                    contentDescription = "分类",
                                    tint = if (selectedCategory.isNotEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true),
                            label = { Text("分类") },
                            colors = androidx.compose.material3.TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                            ),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                        )

                        androidx.compose.material3.DropdownMenu(
                            expanded = isCategoryExpanded,
                            onDismissRequest = { isCategoryExpanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (categories.isEmpty()) {
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { 
                                        Text(
                                            "暂无分类", 
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    },
                                    onClick = { },
                                    leadingIcon = {
                                        androidx.compose.material3.Icon(
                                            Icons.Filled.Category,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                )
                            } else {
                                categories.forEach { category ->
                                    val isSelected = selectedCategory == category.name
                                    androidx.compose.material3.DropdownMenuItem(
                                        text = { 
                                            Text(
                                                category.name, 
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = if (isSelected) 
                                                        androidx.compose.ui.text.font.FontWeight.Bold 
                                                    else 
                                                        androidx.compose.ui.text.font.FontWeight.Normal
                                                ),
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                            )
                                        },
                                        onClick = {
                                            selectedCategory = category.name
                                            isCategoryExpanded = false
                                        },
                                        leadingIcon = {
                                            androidx.compose.material3.Icon(
                                                Icons.Filled.Category,
                                                contentDescription = null,
                                                tint = if (isSelected) MaterialTheme.colorScheme.primary 
                                                       else if (category.type == TransactionType.INCOME) IncomeColor 
                                                       else ExpenseColor
                                            )
                                        },
                                        trailingIcon = if (isSelected) {
                                            {
                                                androidx.compose.material3.Icon(
                                                    androidx.compose.material.icons.Icons.Filled.Check,
                                                    contentDescription = "已选择",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        } else null
                                    )
                                }
                            }
                        }
                    }

                    // 账户选择器 - 增强版
                    ExposedDropdownMenuBox(
                        expanded = isAccountExpanded,
                        onExpandedChange = { isAccountExpanded = !isAccountExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        androidx.compose.material3.OutlinedTextField(
                            value = accounts.find { it.id == selectedAccountId }?.name ?: "现金",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAccountExpanded) },
                            leadingIcon = {
                                androidx.compose.material3.Icon(
                                    imageVector = Icons.Filled.AccountBalance,
                                    contentDescription = "账户",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true),
                            label = { Text("账户") },
                            colors = androidx.compose.material3.TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                            ),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                        )

                        androidx.compose.material3.DropdownMenu(
                            expanded = isAccountExpanded,
                            onDismissRequest = { isAccountExpanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            accounts.forEach { account ->
                                val isSelected = selectedAccountId == account.id
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { 
                                        Text(
                                            account.name, 
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) 
                                                    androidx.compose.ui.text.font.FontWeight.Bold 
                                                else 
                                                    androidx.compose.ui.text.font.FontWeight.Normal
                                            ),
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    onClick = {
                                        selectedAccountId = account.id
                                        isAccountExpanded = false
                                    },
                                    leadingIcon = {
                                        androidx.compose.material3.Icon(
                                            Icons.Filled.AccountBalance,
                                            contentDescription = null,
                                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    },
                                    trailingIcon = if (isSelected) {
                                        {
                                            androidx.compose.material3.Icon(
                                                Icons.Filled.Check,
                                                contentDescription = "已选择",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    } else null
                                )
                            }
                        }
                    }
                }
            }

            // 时间信息卡片
            GradientCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                gradientStart = MaterialTheme.colorScheme.tertiaryContainer,
                gradientEnd = MaterialTheme.colorScheme.surface,
                gradientAngle = 270f,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "时间信息",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    // 日期时间选择区域
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 日期输入框
                        androidx.compose.material3.OutlinedTextField(
                            value = dateTimeText,
                            onValueChange = { dateTimeText = it },
                            label = { Text("日期时间") },
                            modifier = Modifier.weight(1f),
                            readOnly = true,
                            trailingIcon = {
                                androidx.compose.material3.IconButton(
                                    onClick = { showDatePickerDialog = true }
                                ) {
                                    androidx.compose.material3.Icon(
                                        imageVector = Icons.Filled.DateRange,
                                        contentDescription = "选择日期",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            leadingIcon = {
                                androidx.compose.material3.Icon(
                                    imageVector = Icons.Filled.Schedule,
                                    contentDescription = "时间",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            colors = androidx.compose.material3.TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                            ),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                        )
                        
                        // 快速设置按钮
                        androidx.compose.material3.AssistChip(
                            onClick = {
                                dateTimeText = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            },
                            label = {
                                Text(
                                    "现在",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                            leadingIcon = {
                                androidx.compose.material3.Icon(
                                    Icons.Filled.Schedule,
                                    contentDescription = "设置当前时间",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                labelColor = MaterialTheme.colorScheme.primary,
                                leadingIconContentColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                        )
                    }
                    
                    Text(
                        text = "提示：默认使用当前时间，可手动修改",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 按钮区域
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 表单验证错误提示
                if (showErrorToast) {
                    val alpha by animateFloatAsState(
                        targetValue = 1f,
                        label = "errorToastAlpha"
                    )
                    GradientCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer { this.alpha = alpha },
                        gradientStart = MaterialTheme.colorScheme.errorContainer,
                        gradientEnd = MaterialTheme.colorScheme.surface,
                        gradientAngle = 90f,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        borderWidth = 1.dp,
                        borderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = "错误提示",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = "请填写所有必填字段并确保金额大于0",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                            )
                        }
                    }
                }

                PrimaryButton(
                    text = "保存交易",
                    onClick = {
                        if (isFormValid && !isLoading) {
                            isLoading = true
                            scope.launch {
                                val amount = amountText.toDoubleOrNull() ?: 0.0
                                viewModel.addTransaction(description, selectedType, selectedCategory, amount, selectedAccountId, dateTimeText)
                                isLoading = false
                                showSuccess = true
                                kotlinx.coroutines.delay(800)
                                onBack()
                            }
                        } else if (!isFormValid && !isLoading) {
                            showValidationError = true
                            showErrorToast = true
                            scope.launch {
                                kotlinx.coroutines.delay(3000) // 3秒后自动隐藏错误提示
                                showErrorToast = false
                                showValidationError = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isFormValid && !isLoading,
                    isLoading = isLoading
                )
                
                // 提示信息卡片
                GradientCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientStart = MaterialTheme.colorScheme.secondaryContainer,
                    gradientEnd = MaterialTheme.colorScheme.surface,
                    gradientAngle = 90f,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = "提示信息",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "提示信息",
                                style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp),
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Column(
                            modifier = Modifier.padding(start = 26.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(MaterialTheme.colorScheme.primary, androidx.compose.foundation.shape.CircleShape)
                                )
                                Text(
                                    text = "支出金额自动记为负数",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(MaterialTheme.colorScheme.primary, androidx.compose.foundation.shape.CircleShape)
                                )
                                Text(
                                    text = "收入金额自动记为正数",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(MaterialTheme.colorScheme.primary, androidx.compose.foundation.shape.CircleShape)
                                )
                                Text(
                                    text = "余额 = 收入 - 支出",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
            
            // 日期选择器对话框
            DatePickerDialog(
                onDateSelected = { selectedDate ->
                    try {
                        // 解析当前时间字符串
                        val currentDateTime = if (dateTimeText.isNotEmpty()) {
                            try {
                                LocalDateTime.parse(dateTimeText, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            } catch (e: Exception) {
                                LocalDateTime.now()
                            }
                        } else {
                            LocalDateTime.now()
                        }
                        
                        // 更新日期部分，保持时间部分不变
                        val updatedDateTime = LocalDateTime.of(
                            selectedDate.year,
                            selectedDate.monthValue,
                            selectedDate.dayOfMonth,
                            currentDateTime.hour,
                            currentDateTime.minute,
                            currentDateTime.second
                        )
                        
                        dateTimeText = updatedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    } catch (e: Exception) {
                        // 如果解析失败，使用默认时间格式
                        dateTimeText = LocalDateTime.of(
                            selectedDate.year,
                            selectedDate.monthValue,
                            selectedDate.dayOfMonth,
                            12, 0, 0
                        ).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    }
                },
                onDismiss = { showDatePickerDialog = false },
                showDialog = showDatePickerDialog,
                initialDate = try {
                    // 尝试从当前日期时间字符串解析日期部分
                    val currentDate = LocalDate.parse(
                        dateTimeText.split(" ").firstOrNull() ?: LocalDate.now().toString(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )
                    currentDate
                } catch (e: Exception) {
                    LocalDate.now()
                },
                title = "选择交易日期",
                confirmText = "确定",
                dismissText = "取消",
                dialogShape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

        if (showSuccess) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LottieStateAnimation(
                    animationRes = R.raw.success_check,
                    iterations = 1,
                    size = 160.dp
                )
            }
        }
    }
}