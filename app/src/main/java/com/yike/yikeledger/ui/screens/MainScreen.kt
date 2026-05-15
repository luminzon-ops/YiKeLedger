package com.yike.yikeledger.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf(Tab.TRANSACTIONS) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                tonalElevation = 0.dp
            ) {
                Tab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    val iconColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        animationSpec = tween(250),
                        label = "navIconColor"
                    )
                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        animationSpec = tween(250),
                        label = "navTextColor"
                    )
                    val iconScale by animateFloatAsState(
                        targetValue = if (isSelected) 1.1f else 1f,
                        animationSpec = tween(200),
                        label = "navIconScale"
                    )

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            selectedTab = tab
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                tab.icon,
                                contentDescription = tab.title,
                                modifier = Modifier.scale(iconScale),
                                tint = iconColor
                            )
                        },
                        label = {
                            Text(
                                tab.title,
                                color = textColor,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Tab.TRANSACTIONS.route,
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                start = innerPadding.calculateLeftPadding(LocalLayoutDirection.current),
                end = innerPadding.calculateRightPadding(LocalLayoutDirection.current),
                bottom = innerPadding.calculateBottomPadding()
            )
        ) {
            composable(Tab.TRANSACTIONS.route) {
                TransactionListScreen(
                    onAddClick = {
                        navController.navigate("add_transaction")
                    },
                    onEditTransaction = { transactionId ->
                        navController.navigate("edit_transaction/$transactionId")
                    }
                )
            }
            composable(Tab.REPORTS.route) {
                ReportScreen()
            }
            composable(Tab.SETTINGS.route) {
                SettingsScreen(
                    navController = navController
                )
            }
            composable("add_transaction") {
                AddTransactionScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable("edit_transaction/{transactionId}") { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getString("transactionId")?.toLongOrNull()
                EditTransactionScreen(
                    transactionId = transactionId,
                    onBack = { navController.popBackStack() }
                )
            }
            composable("add_account") {
                AddAccountScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable("edit_account/{accountId}") { backStackEntry ->
                val accountId = backStackEntry.arguments?.getString("accountId")?.toLongOrNull()
                EditAccountScreen(
                    accountId = accountId,
                    onBack = { navController.popBackStack() }
                )
            }
            composable("category_tags") {
                CategoryTagsScreen(
                    navController = navController
                )
            }
            composable("project_management") {
                ProjectManagementScreen(
                    navController = navController
                )
            }
            composable("accounts") {
                AccountListScreen(
                    navController = navController,
                    onAddClick = {
                        navController.navigate("add_account")
                    },
                    onEditAccount = { accountId ->
                        navController.navigate("edit_account/$accountId")
                    }
                )
            }
            composable("expense_categories") {
                ExpenseCategoriesScreen(
                    navController = navController
                )
            }
            composable("income_categories") {
                IncomeCategoriesScreen(
                    navController = navController
                )
            }
            composable("add_category") {
                AddEditCategoryScreen(
                    categoryId = null,
                    onSave = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() }
                )
            }
            composable("edit_category/{categoryId}") { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId")?.toLongOrNull()
                AddEditCategoryScreen(
                    categoryId = categoryId,
                    onSave = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}

enum class Tab(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    TRANSACTIONS("transactions", "流水", Icons.Default.List),
    REPORTS("reports", "报表", Icons.Default.BarChart),
    SETTINGS("settings", "设置", Icons.Default.Settings)
}