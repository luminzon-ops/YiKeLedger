package com.yike.yikeledger.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yike.yikeledger.ui.viewmodel.SettingsViewModel
import com.yike.yikeledger.data.ThemeSetting
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf as mutableStateOfAlias
import androidx.compose.ui.platform.LocalContext
import androidx.activity.ComponentActivity

// 设置分组标题
@Composable
fun SettingsGroupTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp),
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun SettingsItemCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } // 闭合内层Row
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "进入",
                tint = MaterialTheme.colorScheme.outline
            )
        } // 闭合外层Row
    } // 闭合Card
} // 闭合函数
// 设置项卡片 - 带开关（用于主题切换等）
@Composable
fun SettingsSwitchCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.size(24.dp)
            )
        } // 闭合外层Row
    } // 闭合Card
} // 闭合函数

@Composable
fun SettingsInfoCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                value?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "进入",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController
) {
    // 获取SettingsViewModel
    val settingsViewModel: SettingsViewModel = viewModel()
    val context = LocalContext.current
    
    // 监听主题变化，确保UI在主题更改时重组
    val currentTheme by settingsViewModel.themeSetting.collectAsState()
    
    // 状态变量
    val notificationsEnabled = remember { mutableStateOf(true) }
    val backupEnabled = remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOfAlias(false) }

    Box {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("设置", style = MaterialTheme.typography.titleLarge) }
                )
            },
            content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 20.dp)
            ) {
                // 1. 数据设置分组
                SettingsGroupTitle(title = "数据设置")
                SettingsItemCard(
                    title = "分类管理",
                    description = "管理收入和支出分类",
                    icon = Icons.Default.Category,
                    onClick = {
                        navController.navigate("category_tags")
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsItemCard(
                    title = "项目管理",
                    description = "管理项目分类",
                    icon = Icons.Default.Folder,
                    onClick = {
                        navController.navigate("project_management")
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 2. 显示设置分组
                SettingsGroupTitle(title = "显示设置")
                SettingsInfoCard(
                    title = "主题模式",
                    description = settingsViewModel.getThemeDescription(),
                    icon = Icons.Default.Palette,
                    onClick = { showThemeDialog = true }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsInfoCard(
                    title = "字体大小",
                    description = "调整应用字体大小",
                    icon = Icons.Default.FormatSize,
                    onClick = { /* 打开字体设置 */ }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsInfoCard(
                    title = "货币格式",
                    description = "设置货币显示格式",
                    icon = Icons.Default.AttachMoney,
                    onClick = { /* 打开货币格式设置 */ }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 3. 数据管理分组
                SettingsGroupTitle(title = "数据管理")
                SettingsSwitchCard(
                    title = "自动备份",
                    description = "开启自动数据备份",
                    icon = Icons.Default.Backup,
                    checked = backupEnabled.value,
                    onCheckedChange = { newValue: Boolean -> backupEnabled.value = newValue }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsItemCard(
                    title = "手动备份",
                    description = "立即备份数据到云端",
                    icon = Icons.Default.CloudUpload,
                    onClick = { /* 执行备份 */ }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsItemCard(
                    title = "恢复数据",
                    description = "从备份恢复数据",
                    icon = Icons.Default.CloudDownload,
                    onClick = { /* 恢复数据 */ }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsItemCard(
                    title = "导出数据",
                    description = "导出数据为CSV/Excel",
                    icon = Icons.Default.FileOpen,
                    onClick = { /* 导出数据 */ }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 4. 通知设置分组
                SettingsGroupTitle(title = "通知与提醒")
                SettingsSwitchCard(
                    title = "消费提醒",
                    description = "开启消费通知提醒",
                    icon = Icons.Default.Notifications,
                    checked = notificationsEnabled.value,
                    onCheckedChange = { newValue: Boolean -> notificationsEnabled.value = newValue }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsInfoCard(
                    title = "提醒设置",
                    description = "自定义提醒时间和频率",
                    icon = Icons.Default.Settings,
                    onClick = { /* 打开提醒设置 */ }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 5. 关于应用分组（列表式）
                SettingsGroupTitle(title = "关于应用")
                SettingsInfoCard(
                    title = "用户手册",
                    description = "查看使用指南和教程",
                    icon = Icons.Default.Book,
                    onClick = { /* 打开用户手册 */ }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsInfoCard(
                    title = "版本信息",
                    description = "查看应用版本和更新",
                    icon = Icons.Default.Info,
                    value = "v1.0.1Test",
                    onClick = { /* 显示版本信息 */ }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsInfoCard(
                    title = "意见反馈",
                    description = "提交问题或建议",
                    icon = Icons.Default.Feedback,
                    onClick = { /* 打开反馈页面 */ }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsInfoCard(
                    title = "隐私政策",
                    description = "查看隐私政策和使用条款",
                    icon = Icons.Default.PrivacyTip,
                    onClick = { /* 打开隐私政策 */ }
                )
            }
        }
    )
        // 主题选择对话框
        if (showThemeDialog) {
            AlertDialog(
                onDismissRequest = { showThemeDialog = false },
                title = { Text("选择主题模式") },
                text = {
                    Column {
                        settingsViewModel.getThemeOptions().forEach { option ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        settingsViewModel.saveThemeSetting(option.setting)
                                        showThemeDialog = false
                                        // 主题将通过 Compose 重组实时更新，无需重建 Activity
                                        // 使用平滑过渡动画改善用户体验
                                    }
                                    .padding(vertical = 12.dp, horizontal = 4.dp),
                                color = if (settingsViewModel.themeSetting.value == option.setting) {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                } else {
                                    MaterialTheme.colorScheme.surface
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = option.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = option.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Divider(modifier = Modifier.padding(horizontal = 4.dp))
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { showThemeDialog = false }
                    ) {
                        Text("取消")
                    }
                }
            )
    }
}
}
