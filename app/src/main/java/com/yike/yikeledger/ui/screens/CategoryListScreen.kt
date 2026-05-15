package com.yike.yikeledger.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.yike.yikeledger.data.TransactionType
import com.yike.yikeledger.ui.viewmodel.TransactionViewModel
import com.yike.yikeledger.ui.components.GradientCard
import com.yike.yikeledger.ui.components.ModernFloatingActionButton
import com.yike.yikeledger.ui.components.EmptyState
import com.yike.yikeledger.ui.components.CategoryCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    viewModel: TransactionViewModel = viewModel(),
    onAddClick: () -> Unit,
    onEditCategory: (Long) -> Unit
) {
    val categories = viewModel.categories.collectAsState().value

    // 监听数据变化，实时刷新
    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("分类管理", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(
                        onClick = onAddClick,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "添加分类")
                    }
                }
            )
        },
        floatingActionButton = {
            ModernFloatingActionButton(
                onClick = onAddClick,
                icon = Icons.Default.Add,
                contentDescription = "添加分类"
            )
        }
    ) { innerPadding ->
        if (categories.isEmpty()) {
            EmptyState(
                title = "暂无分类",
                description = "点击右下角按钮添加第一个分类"
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
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // 收入分类部分
                val incomeCategories = categories.filter { it.type == TransactionType.INCOME }
                if (incomeCategories.isNotEmpty()) {
                    item {
                        Text(
                            text = "收入分类",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(incomeCategories) { category ->
                        CategoryCard(
                            name = category.name,
                            type = category.type,
                            onClick = { onEditCategory(category.id) }
                        )
                    }
                }

                // 支出分类部分
                val expenseCategories = categories.filter { it.type == TransactionType.EXPENSE }
                if (expenseCategories.isNotEmpty()) {
                    item {
                        Text(
                            text = "支出分类",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(expenseCategories) { category ->
                        CategoryCard(
                            name = category.name,
                            type = category.type,
                            onClick = { onEditCategory(category.id) }
                        )
                    }
                }
            }
        }
    }
}


