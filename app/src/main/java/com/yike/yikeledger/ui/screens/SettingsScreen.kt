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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
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
import android.content.Intent
import android.widget.Toast
import com.yike.yikeledger.ui.viewmodel.SettingsViewModel
import com.yike.yikeledger.ui.viewmodel.TransactionViewModel
import com.yike.yikeledger.data.ThemeSetting
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.yike.yikeledger.ui.components.PressAnimation
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
    PressAnimation(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "进入",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
// 设置项卡片 - 带开关（用于主题切换等）
@Composable
fun SettingsSwitchCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    PressAnimation(
        onClick = { onCheckedChange(!checked) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
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
    } // 闭合PressAnimation
} // 闭合函数

@Composable
fun SettingsInfoCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String? = null,
    onClick: () -> Unit
) {
    PressAnimation(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController
) {
    // 获取ViewModels
    val settingsViewModel: SettingsViewModel = viewModel()
    val transactionViewModel: TransactionViewModel = viewModel()
    val context = LocalContext.current

    // 监听持久化状态
    val currentTheme by settingsViewModel.themeSetting.collectAsState()
    val currentFontScale by settingsViewModel.fontScale.collectAsState()
    val currentCurrencyFormat by settingsViewModel.currencyFormat.collectAsState()
    val autoBackup by settingsViewModel.autoBackup.collectAsState()
    val expenseNotify by settingsViewModel.expenseNotify.collectAsState()
    val notifyTime by settingsViewModel.notifyTime.collectAsState()

    var showThemeDialog by remember { mutableStateOf(false) }
    var showFontDialog by remember { mutableStateOf(false) }
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showNotifyDialog by remember { mutableStateOf(false) }

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
                    description = settingsViewModel.getFontScaleDescription(),
                    icon = Icons.Default.FormatSize,
                    onClick = { showFontDialog = true }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsInfoCard(
                    title = "货币格式",
                    description = settingsViewModel.getCurrencyFormatDescription(),
                    icon = Icons.Default.AttachMoney,
                    onClick = { showCurrencyDialog = true }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 3. 数据管理分组
                SettingsGroupTitle(title = "数据管理")
                SettingsSwitchCard(
                    title = "自动备份",
                    description = if (autoBackup) "已开启" else "已关闭",
                    icon = Icons.Default.Backup,
                    checked = autoBackup,
                    onCheckedChange = { settingsViewModel.setAutoBackup(it) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsItemCard(
                    title = "手动备份",
                    description = "立即备份数据到云端",
                    icon = Icons.Default.CloudUpload,
                    onClick = { Toast.makeText(context, "此功能将在后续版本中实现", Toast.LENGTH_SHORT).show() }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsItemCard(
                    title = "恢复数据",
                    description = "从备份恢复数据",
                    icon = Icons.Default.CloudDownload,
                    onClick = { Toast.makeText(context, "此功能将在后续版本中实现", Toast.LENGTH_SHORT).show() }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsItemCard(
                    title = "导出数据",
                    description = "导出交易记录为CSV",
                    icon = Icons.Default.FileOpen,
                    onClick = {
                        val csv = transactionViewModel.exportTransactionsAsCsv()
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/csv"
                            putExtra(Intent.EXTRA_TEXT, csv)
                            putExtra(Intent.EXTRA_SUBJECT, "一刻记账 - 交易记录导出")
                        }
                        context.startActivity(Intent.createChooser(intent, "分享CSV"))
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 4. 通知设置分组
                SettingsGroupTitle(title = "通知与提醒")
                SettingsSwitchCard(
                    title = "消费提醒",
                    description = if (expenseNotify) "已开启" else "已关闭",
                    icon = Icons.Default.Notifications,
                    checked = expenseNotify,
                    onCheckedChange = { settingsViewModel.setExpenseNotify(it) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsInfoCard(
                    title = "提醒设置",
                    description = settingsViewModel.getNotifyDescription(),
                    icon = Icons.Default.Settings,
                    onClick = { showNotifyDialog = true }
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
                    value = "v0.1.0",
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
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 4.dp))
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

        // 字体大小对话框
        if (showFontDialog) {
            AlertDialog(
                onDismissRequest = { showFontDialog = false },
                title = { Text("选择字体大小") },
                text = {
                    Column {
                        SettingsViewModel.FONT_SCALE_OPTIONS.forEach { option ->
                            val selected = currentFontScale == option.scale
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        settingsViewModel.setFontScale(option.scale)
                                        showFontDialog = false
                                    }
                                    .padding(vertical = 12.dp, horizontal = 4.dp),
                                color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    else MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selected,
                                        onClick = null
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(option.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                        Text(option.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 4.dp))
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showFontDialog = false }) { Text("取消") }
                }
            )
        }

        // 货币格式对话框
        if (showCurrencyDialog) {
            AlertDialog(
                onDismissRequest = { showCurrencyDialog = false },
                title = { Text("选择货币格式") },
                text = {
                    Column {
                        SettingsViewModel.CURRENCY_OPTIONS.forEach { option ->
                            val selected = currentCurrencyFormat == option.format
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        settingsViewModel.setCurrencyFormat(option.format)
                                        showCurrencyDialog = false
                                    }
                                    .padding(vertical = 12.dp, horizontal = 4.dp),
                                color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    else MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selected,
                                        onClick = null
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(option.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                        Text(option.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 4.dp))
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showCurrencyDialog = false }) { Text("取消") }
                }
            )
        }

        // 提醒时间对话框
        if (showNotifyDialog) {
            AlertDialog(
                onDismissRequest = { showNotifyDialog = false },
                title = { Text("选择提醒时间") },
                text = {
                    Column {
                        SettingsViewModel.NOTIFY_TIME_OPTIONS.forEach { opt ->
                            val sel = notifyTime == opt.time
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { settingsViewModel.setNotifyTime(opt.time); showNotifyDialog = false }
                                    .padding(vertical = 12.dp, horizontal = 4.dp),
                                color = if (sel) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    else MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(selected = sel, onClick = null)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(opt.title, style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 4.dp))
                        }
                    }
                },
                confirmButton = { TextButton(onClick = { showNotifyDialog = false }) { Text("取消") } }
            )
        }
    }
}
