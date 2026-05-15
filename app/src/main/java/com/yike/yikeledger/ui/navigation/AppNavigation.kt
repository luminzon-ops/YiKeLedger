package com.yike.yikeledger.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yike.yikeledger.ui.screens.AddTransactionScreen
import com.yike.yikeledger.ui.screens.TransactionListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.TransactionList.route
    ) {
        composable(Screen.TransactionList.route) {
            TransactionListScreen(
                onAddClick = { navController.navigate(Screen.AddTransaction.route) },
                onEditTransaction = { /* TODO: 实现编辑交易导航 */ }
            )
        }
        composable(Screen.AddTransaction.route) {
            AddTransactionScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object TransactionList : Screen("transaction_list")
    object AddTransaction : Screen("add_transaction")
}