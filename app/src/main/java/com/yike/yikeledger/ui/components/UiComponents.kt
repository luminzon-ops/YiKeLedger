package com.yike.yikeledger.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalLayoutDirection

import com.yike.yikeledger.ui.theme.AmountTypography
import com.yike.yikeledger.ui.theme.CardTitle
import com.yike.yikeledger.ui.theme.ChartLabel
import com.yike.yikeledger.ui.theme.IncomeColor
import com.yike.yikeledger.ui.theme.ExpenseColor
import com.yike.yikeledger.ui.theme.InfoBlue
import com.yike.yikeledger.ui.theme.Neutral40
import com.yike.yikeledger.ui.theme.Neutral60
import com.yike.yikeledger.ui.theme.PrimaryGradientStart
import com.yike.yikeledger.ui.theme.PrimaryGradientEnd
import com.yike.yikeledger.ui.theme.SecondaryGradientStart
import com.yike.yikeledger.ui.theme.SecondaryGradientEnd
import com.yike.yikeledger.ui.theme.SuccessGreen
import com.yike.yikeledger.ui.theme.ErrorRed
import com.yike.yikeledger.data.TransactionType
import com.yike.yikeledger.R
import androidx.compose.foundation.isSystemInDarkTheme
import kotlin.ranges.ClosedRange

// 渐变背景卡片 - Box 实现，无 elevation 阴影
@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    gradientStart: Color = PrimaryGradientStart,
    gradientEnd: Color = PrimaryGradientEnd,
    gradientAngle: Float = 45f,
    shape: androidx.compose.foundation.shape.RoundedCornerShape = RoundedCornerShape(20.dp),
    @Suppress("UNUSED_PARAMETER")
    elevation: androidx.compose.material3.CardElevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    borderWidth: androidx.compose.ui.unit.Dp = 0.dp,
    borderColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Transparent,
    content: @Composable () -> Unit
) {
    val angleRad = gradientAngle * (Math.PI.toFloat() / 180f)
    val endX = kotlin.math.cos(angleRad) * 1000f
    val endY = kotlin.math.sin(angleRad) * 1000f

    val borderMod = if (borderWidth > 0.dp) {
        Modifier.border(BorderStroke(borderWidth, borderColor), shape)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(borderMod)
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(gradientStart, gradientEnd),
                    start = Offset(0f, 0f),
                    end = Offset(endX, endY)
                )
            )
            .padding(20.dp)
    ) {
        content()
    }
}

// 统计卡片 - 增强版：支持背景颜色、边框、形状等自定义选项
@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    description: String = "",
    valueColor: Color = MaterialTheme.colorScheme.primary,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    shape: androidx.compose.foundation.shape.RoundedCornerShape = RoundedCornerShape(16.dp),
    elevation: androidx.compose.material3.CardElevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    backgroundColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    showBorder: Boolean = false,
    borderColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
    borderWidth: androidx.compose.ui.unit.Dp = 1.dp
) {
    val cardModifier = if (onClick != null) {
        modifier.clickable(
            onClick = onClick,
            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
            indication = null
        )
    } else {
        modifier
    }

    Card(
        modifier = cardModifier,
        shape = shape,
        elevation = elevation,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        border = if (showBorder) {
            BorderStroke(borderWidth, borderColor)
        } else {
            null
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(20.dp),
                        tint = valueColor
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = value,
                style = AmountTypography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )

            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// 金额显示组件
@Composable
fun AmountDisplay(
    amount: Double,
    type: String = "",
    showSymbol: Boolean = true,
    fontSize: Int = 20
) {
    val amountText = if (showSymbol) {
        "¥${String.format("%.2f", amount)}"
    } else {
        String.format("%.2f", amount)
    }

    val textColor = when (type) {
        "income" -> IncomeColor
        "expense" -> ExpenseColor
        "transfer" -> InfoBlue
        else -> if (amount >= 0) IncomeColor else ExpenseColor
    }

    val icon = when (type) {
        "income" -> Icons.AutoMirrored.Filled.TrendingUp
        "expense" -> Icons.AutoMirrored.Filled.TrendingDown
        else -> if (amount >= 0) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = type,
            modifier = Modifier.size(16.dp),
            tint = textColor
        )
        Text(
            text = amountText,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

// 交易项目卡片（现代化版本）
@Composable
fun ModernTransactionItem(
    title: String,
    amount: Double,
    type: String,
    date: String,
    category: String = "",
    description: String = "",
    onClick: () -> Unit = {}
) {
    var isPressed by remember { mutableStateOf(false) }
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 6.dp,
        label = "cardElevation"
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧图标/色块
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = when (type) {
                                "income" -> listOf(SuccessGreen.copy(alpha = 0.8f), SuccessGreen)
                                "expense" -> listOf(ErrorRed.copy(alpha = 0.8f), ErrorRed)
                                else -> listOf(InfoBlue.copy(alpha = 0.8f), InfoBlue)
                            }
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (type) {
                        "income" -> Icons.AutoMirrored.Filled.TrendingUp
                        "expense" -> Icons.AutoMirrored.Filled.TrendingDown
                        else -> Icons.AutoMirrored.Filled.ArrowForward
                    },
                    contentDescription = type,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 主要信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )

                if (category.isNotEmpty()) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1
                    )
                }

                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                Text(
                    text = date,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 金额
            Column(
                horizontalAlignment = Alignment.End
            ) {
                AmountDisplay(amount = amount, type = type, fontSize = 18)

            }

            Spacer(modifier = Modifier.width(8.dp))

            // 右侧箭头
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "查看详情",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

// 账户卡片
@Composable
fun AccountCard(
    name: String,
    balance: Double,
    type: String,
    color: Long,
    onClick: () -> Unit = {}
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 账户图标
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(color)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.take(1),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 账户信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = type,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // 余额
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "¥${String.format("%.2f", balance)}",
                    style = AmountTypography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = if (balance >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )

                Text(
                    text = if (balance >= 0) "正余额" else "负余额",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// 浮动操作按钮（现代化）
@Composable
fun ModernFloatingActionButton(
    onClick: () -> Unit,
    icon: ImageVector = Icons.Default.Add,
    contentDescription: String = "添加"
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = tween(160),
        label = "fabPressScale"
    )

    FloatingActionButton(
        onClick = onClick,
        interactionSource = interactionSource,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = CircleShape,
        modifier = Modifier
            .scale(scale)
            .shadow(10.dp, shape = CircleShape)
            .size(60.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(26.dp)
        )
    }
}

// 空状态组件
@Composable
fun EmptyState(
    title: String,
    description: String,
    icon: ImageVector = Icons.Default.Info,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

// 进度指示器
@Composable
fun ProgressIndicator(
    progress: Float, // 0.0 到 1.0
    height: Int = 8,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(height.dp / 2))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(height.dp)
                .clip(RoundedCornerShape(height.dp / 2))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(color, color.copy(alpha = 0.8f))
                    )
                )
        )
    }
}

// 分类卡片
@Composable
fun CategoryCard(
    name: String,
    type: TransactionType,
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (type == TransactionType.INCOME) "收入" else "支出",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (type == TransactionType.INCOME)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "编辑分类",
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

// ============================================
// UI/UX Pro Max 增强组件
// ============================================

// 主按钮 - 增强版：支持图标、自定义颜色、形状、加载动画等
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconTint: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.White,
    backgroundColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    textColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
    disabledBackgroundColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
    disabledTextColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
    shape: androidx.compose.foundation.shape.RoundedCornerShape = RoundedCornerShape(12.dp),
    minHeight: androidx.compose.ui.unit.Dp = 56.dp, // 符合44x44dp触摸目标要求
    paddingValues: androidx.compose.foundation.layout.PaddingValues = androidx.compose.foundation.layout.PaddingValues(horizontal = 24.dp, vertical = 16.dp),
    showRipple: Boolean = true,
    rippleColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.2f),
    loadingIndicatorSize: androidx.compose.ui.unit.Dp = 20.dp,
    loadingIndicatorColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier
            .height(minHeight)
            .fillMaxWidth(),
        enabled = enabled && !isLoading,
        shape = shape,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = disabledBackgroundColor,
            disabledContentColor = disabledTextColor
        ),
        elevation = androidx.compose.material3.ButtonDefaults.buttonElevation(
            defaultElevation = if (enabled && !isLoading) 2.dp else 0.dp,
            pressedElevation = if (enabled && !isLoading) 4.dp else 0.dp,
            disabledElevation = 0.dp
        ),
        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
        contentPadding = paddingValues
    ) {
        if (isLoading) {
            androidx.compose.material3.CircularProgressIndicator(
                modifier = androidx.compose.ui.Modifier.size(loadingIndicatorSize),
                strokeWidth = 2.dp,
                color = loadingIndicatorColor,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        } else {
            Row(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = androidx.compose.ui.Modifier.fillMaxWidth()
            ) {
                if (icon != null) {
                    androidx.compose.material3.Icon(
                        imageVector = icon,
                        contentDescription = text,
                        tint = iconTint,
                        modifier = androidx.compose.ui.Modifier.size(20.dp)
                    )
                    androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
                }
                androidx.compose.material3.Text(
                    text = text,
                    style = com.yike.yikeledger.ui.theme.ButtonLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

// 次要按钮 - 增强版：轮廓样式，支持图标、自定义颜色、边框等
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconTint: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    textColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    disabledTextColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
    borderColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    disabledBorderColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.outline,
    backgroundColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Transparent,
    disabledBackgroundColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Transparent,
    shape: androidx.compose.foundation.shape.RoundedCornerShape = RoundedCornerShape(12.dp),
    borderWidth: androidx.compose.ui.unit.Dp = 1.dp,
    minHeight: androidx.compose.ui.unit.Dp = 56.dp, // 符合44x44dp触摸目标要求
    paddingValues: androidx.compose.foundation.layout.PaddingValues = androidx.compose.foundation.layout.PaddingValues(horizontal = 24.dp, vertical = 16.dp),
    showRipple: Boolean = true,
    rippleColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
) {
    androidx.compose.material3.OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(minHeight)
            .fillMaxWidth(),
        enabled = enabled,
        shape = shape,
        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = disabledBackgroundColor,
            disabledContentColor = disabledTextColor
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = borderWidth,
            color = if (enabled) borderColor else disabledBorderColor
        ),
        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
        contentPadding = paddingValues
    ) {
        Row(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            modifier = androidx.compose.ui.Modifier.fillMaxWidth()
        ) {
            if (icon != null) {
                androidx.compose.material3.Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = iconTint,
                    modifier = androidx.compose.ui.Modifier.size(20.dp)
                )
                androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
            }
            androidx.compose.material3.Text(
                text = text,
                style = com.yike.yikeledger.ui.theme.ButtonMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

// 文字按钮 - 增强版：最小化样式，支持图标、自定义颜色、形状等
@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconTint: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    textColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    disabledTextColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
    backgroundColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Transparent,
    disabledBackgroundColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Transparent,
    shape: androidx.compose.foundation.shape.RoundedCornerShape = RoundedCornerShape(8.dp),
    minHeight: androidx.compose.ui.unit.Dp = 40.dp, // 文字按钮可以稍小一些，但也要满足触摸目标
    paddingValues: androidx.compose.foundation.layout.PaddingValues = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    showRipple: Boolean = true,
    rippleColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
    underlineText: Boolean = false,
    underlineColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary
) {
    androidx.compose.material3.TextButton(
        onClick = onClick,
        modifier = modifier
            .height(minHeight),
        enabled = enabled,
        shape = shape,
        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = disabledBackgroundColor,
            disabledContentColor = disabledTextColor
        ),
        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
        contentPadding = paddingValues
    ) {
        Row(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            modifier = androidx.compose.ui.Modifier
        ) {
            if (icon != null) {
                androidx.compose.material3.Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = iconTint,
                    modifier = androidx.compose.ui.Modifier.size(16.dp)
                )
                androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.width(6.dp))
            }
            androidx.compose.material3.Text(
                text = text,
                style = com.yike.yikeledger.ui.theme.ButtonMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = if (underlineText) {
                    androidx.compose.ui.Modifier
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(underlineColor, underlineColor),
                                start = androidx.compose.ui.geometry.Offset(0f, 100f),
                                end = androidx.compose.ui.geometry.Offset(100f, 100f)
                            )
                        )
                        .padding(bottom = 1.dp)
                } else {
                    androidx.compose.ui.Modifier
                }
            )
        }
    }
}

// 玻璃效果卡片 - 根据UI/UX Pro Max文档，玻璃效果需要足够不透明度
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!isSystemInDarkTheme()) {
                com.yike.yikeledger.ui.theme.GlassLight
            } else {
                com.yike.yikeledger.ui.theme.GlassDark
            }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            androidx.compose.ui.graphics.Color.Transparent,
                            androidx.compose.ui.graphics.Color.Transparent
                        )
                    )
                )
                .padding(20.dp)
        ) {
            content()
        }
    }
}

// 现代化输入框 - 增强版：支持占位符、帮助文本、字符计数、视觉转换等
@Composable
fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    helperText: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    maxLength: Int? = null,
    showCounter: Boolean = false,
    keyboardOptions: androidx.compose.foundation.text.KeyboardOptions = androidx.compose.foundation.text.KeyboardOptions.Default,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: androidx.compose.foundation.shape.RoundedCornerShape = RoundedCornerShape(12.dp),
    containerColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
    focusedContainerColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
    unfocusedContainerColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
    focusedLabelColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    unfocusedLabelColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
    focusedIndicatorColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    unfocusedIndicatorColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.outline,
    errorIndicatorColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.error,
    cursorColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    textColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
    placeholderColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
    helperTextColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
    counterTextColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    val charCount = value.length
    val maxLengthReached = maxLength?.let { charCount >= it } ?: false

    Column(modifier = modifier.fillMaxWidth()) {
        androidx.compose.material3.OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (maxLength == null || newValue.length <= maxLength) {
                    onValueChange(newValue)
                } else {
                    onValueChange(newValue.take(maxLength))
                }
            },
            label = {
                androidx.compose.material3.Text(
                    text = label,
                    style = com.yike.yikeledger.ui.theme.InputLabel
                )
            },
            placeholder = placeholder?.let {
                {
                    androidx.compose.material3.Text(
                        text = it,
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        color = placeholderColor
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            isError = isError || maxLengthReached,
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            shape = shape,
            colors = androidx.compose.material3.TextFieldDefaults.colors(),
            enabled = enabled,
            readOnly = readOnly,
            supportingText = {
                Column {
                    if (helperText != null) {
                        androidx.compose.material3.Text(
                            text = helperText,
                            style = com.yike.yikeledger.ui.theme.InputHelper,
                            color = helperTextColor
                        )
                    }
                    if (showCounter && maxLength != null) {
                        androidx.compose.material3.Text(
                            text = "$charCount/$maxLength",
                            style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                            color = if (maxLengthReached) errorIndicatorColor else counterTextColor,
                            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.End
                        )
                    }
                }
            }
        )

        if (isError && errorMessage != null) {
            androidx.compose.material3.Text(
                text = errorMessage,
                style = com.yike.yikeledger.ui.theme.InputHelper,
                color = errorIndicatorColor,
                modifier = androidx.compose.ui.Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

// 徽章组件 - 用于标签和状态指示
@Composable
fun Badge(
    text: String,
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    textColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
) {
    Box(
        modifier = modifier
            .background(color, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        androidx.compose.material3.Text(
            text = text,
            style = com.yike.yikeledger.ui.theme.BadgeText,
            color = textColor,
            maxLines = 1
        )
    }
}

// 收入/支出徽章
@Composable
fun TransactionTypeBadge(
    type: com.yike.yikeledger.data.TransactionType,
    modifier: Modifier = Modifier
) {
    val (text, color) = when (type) {
        com.yike.yikeledger.data.TransactionType.INCOME -> Pair("收入", com.yike.yikeledger.ui.theme.IncomeColor)
        com.yike.yikeledger.data.TransactionType.EXPENSE -> Pair("支出", com.yike.yikeledger.ui.theme.ExpenseColor)
    }
    
    Badge(
        text = text,
        modifier = modifier,
        color = color,
        textColor = androidx.compose.ui.graphics.Color.White
    )
}

// 加载状态骨架屏 - 根据UI/UX Pro Max文档，提供平滑的加载体验
@Composable
fun LoadingSkeleton(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "skeletonPulse")
    val shimmerAlpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.72f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeletonAlpha"
    )
    val skeletonColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = shimmerAlpha)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(skeletonColor)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(skeletonColor)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(skeletonColor)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(skeletonColor)
                )
            }
        }
    }
}

// 空状态组件增强版
@Composable
fun EnhancedEmptyState(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    lottieRes: Int? = R.raw.empty_ledger
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (lottieRes != null) {
            LottieStateAnimation(
                animationRes = lottieRes,
                size = 132.dp
            )
        } else {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        if (actionText != null && onAction != null) {
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                text = actionText,
                onClick = onAction,
                modifier = Modifier.fillMaxWidth(0.72f)
            )
        }
    }
}

// ============================================
// 新基础组件
// ============================================

// 加载状态组件 - 支持不同类型的加载指示器
@Composable
fun LoadingState(
    modifier: Modifier = Modifier,
    loadingType: LoadingType = LoadingType.CircularProgress,
    size: androidx.compose.ui.unit.Dp = 48.dp,
    color: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    backgroundColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Transparent,
    message: String? = null,
    messageColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
    showBackground: Boolean = false,
    backgroundShape: androidx.compose.foundation.shape.RoundedCornerShape = RoundedCornerShape(12.dp)
) {
    Box(
        modifier = modifier
            .then(if (showBackground) {
                androidx.compose.ui.Modifier
                    .background(backgroundColor, backgroundShape)
                    .padding(24.dp)
            } else {
                androidx.compose.ui.Modifier
            }),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (loadingType) {
                LoadingType.CircularProgress -> {
                    androidx.compose.material3.CircularProgressIndicator(
                        modifier = androidx.compose.ui.Modifier.size(size),
                        color = color,
                        strokeWidth = 4.dp,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                }
                LoadingType.LinearProgress -> {
                    androidx.compose.material3.LinearProgressIndicator(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxWidth(0.6f)
                            .height(size / 4),
                        color = color,
                        trackColor = color.copy(alpha = 0.2f)
                    )
                }
                LoadingType.Skeleton -> {
                    LoadingSkeleton(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxWidth(0.8f)
                            .height(size * 2)
                    )
                }
                LoadingType.Pulse -> {
                    val pulseScale by rememberInfiniteTransition(label = "pulseLoading").animateFloat(
                        initialValue = 0.82f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 700),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "pulseScale"
                    )
                    Box(
                        modifier = Modifier
                            .size(size)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(color.copy(alpha = 0.22f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(size * 0.58f)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }
                LoadingType.Lottie -> {
                    LottieStateAnimation(
                        animationRes = R.raw.loading_coins,
                        size = size * 1.8f
                    )
                }
            }

            if (message != null) {
                androidx.compose.material3.Text(
                    text = message,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = messageColor,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

// 加载类型枚举
enum class LoadingType {
    CircularProgress,
    LinearProgress,
    Skeleton,
    Pulse,
    Lottie
}

// 确认对话框组件 - 用于确认操作，支持自定义标题、内容、按钮等
@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = "确认",
    dismissText: String = "取消",
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconTint: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    isDestructive: Boolean = false,
    showDialog: Boolean = true,
    confirmButtonColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    dismissButtonColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.outline,
    titleColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
    messageColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
    dialogShape: androidx.compose.foundation.shape.RoundedCornerShape = RoundedCornerShape(20.dp),
    dialogElevation: androidx.compose.ui.unit.Dp = 24.dp
    ) {
        if (showDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = onDismiss,
                modifier = modifier,
                shape = dialogShape,
                tonalElevation = dialogElevation,
                icon = icon?.let {
                    {
                        androidx.compose.material3.Icon(
                            imageVector = it,
                            contentDescription = title,
                            tint = iconTint,
                            modifier = androidx.compose.ui.Modifier.size(32.dp)
                        )
                    }
                },
                title = {
                    androidx.compose.material3.Text(
                        text = title,
                        style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                        color = titleColor
                    )
                },
                text = {
                    androidx.compose.material3.Text(
                        text = message,
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        color = messageColor,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Start
                    )
                },
                confirmButton = {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            onConfirm()
                            onDismiss()
                        },
                        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                            contentColor = if (isDestructive) {
                                androidx.compose.material3.MaterialTheme.colorScheme.error
                            } else {
                                confirmButtonColor
                            }
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        androidx.compose.material3.Text(
                            text = confirmText,
                            style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                        )
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(
                        onClick = onDismiss,
                        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                            contentColor = dismissButtonColor
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        androidx.compose.material3.Text(
                            text = dismissText,
                            style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                        )
                    }
                }
            )
        }
    }

// 金额输入字段组件 - 专门用于输入金额，支持货币符号、格式化、验证等
@Composable
fun AmountInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "金额",
    currencySymbol: String = "¥",
    showCurrencySymbol: Boolean = true,
    decimalPlaces: Int = 2,
    maxValue: Double? = null,
    minValue: Double = 0.0,
    isError: Boolean = false,
    errorMessage: String? = null,
    helperText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    shape: androidx.compose.foundation.shape.RoundedCornerShape = RoundedCornerShape(12.dp),
    containerColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
    focusedContainerColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
    unfocusedContainerColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
    focusedLabelColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    unfocusedLabelColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
    focusedIndicatorColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    unfocusedIndicatorColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.outline,
    errorIndicatorColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.error,
    cursorColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    textColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
) {
    val numericValue = value.toDoubleOrNull() ?: 0.0
    val formattedValue = if (value.isNotEmpty()) {
        try {
            // 移除非数字字符，除了小数点
            val cleanValue = value.replace(Regex("[^\\d.]"), "")
            // 限制小数位数
            if (cleanValue.contains('.')) {
                val parts = cleanValue.split('.')
                if (parts.size == 2) {
                    val integerPart = parts[0]
                    val decimalPart = parts[1].take(decimalPlaces)
                    "$integerPart.$decimalPart"
                } else {
                    cleanValue
                }
            } else {
                cleanValue
            }
        } catch (e: Exception) {
            value
        }
    } else {
        value
    }

    val internalIsError = isError || (value.isNotEmpty() && numericValue < minValue) ||
            (maxValue != null && value.isNotEmpty() && numericValue > maxValue)

    val internalErrorMessage = errorMessage ?: when {
        value.isNotEmpty() && numericValue < minValue -> "金额不能小于$currencySymbol${String.format("%.${decimalPlaces}f", minValue)}"
        maxValue != null && value.isNotEmpty() && numericValue > maxValue -> "金额不能超过$currencySymbol${String.format("%.${decimalPlaces}f", maxValue)}"
        else -> null
    }

    ModernTextField(
        value = formattedValue,
        onValueChange = { newValue ->
            // 只允许数字和小数点
            if (newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                // 限制小数点后位数
                if (newValue.contains('.')) {
                    val parts = newValue.split('.')
                    if (parts.size == 2 && parts[1].length <= decimalPlaces) {
                        onValueChange(newValue)
                    } else if (parts.size == 2 && parts[1].length > decimalPlaces) {
                        // 截断多余的小数位
                        val truncated = "${parts[0]}.${parts[1].take(decimalPlaces)}"
                        onValueChange(truncated)
                    } else {
                        onValueChange(newValue)
                    }
                } else {
                    onValueChange(newValue)
                }
            } else if (newValue.isEmpty()) {
                onValueChange(newValue)
            }
        },
        label = label,
        modifier = modifier,
        placeholder = if (showCurrencySymbol) "${currencySymbol}0.00" else "0.00",
        helperText = helperText,
        isError = internalIsError,
        errorMessage = internalErrorMessage,
        singleLine = true,
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions.Default.copy(
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
        ),
        leadingIcon = if (showCurrencySymbol) {
            {
                androidx.compose.material3.Text(
                    text = currencySymbol,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                    color = if (internalIsError) errorIndicatorColor else focusedLabelColor,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
            }
        } else null,
        shape = shape,
        containerColor = containerColor,
        focusedContainerColor = focusedContainerColor,
        unfocusedContainerColor = unfocusedContainerColor,
        focusedLabelColor = focusedLabelColor,
        unfocusedLabelColor = unfocusedLabelColor,
        focusedIndicatorColor = focusedIndicatorColor,
        unfocusedIndicatorColor = unfocusedIndicatorColor,
        errorIndicatorColor = errorIndicatorColor,
        cursorColor = cursorColor,
        textColor = textColor,
        enabled = enabled,
        readOnly = readOnly,
        showCounter = false
    )
}

// 日期选择器对话框组件 - 用于选择日期，支持初始日期、最小日期、最大日期等
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (java.time.LocalDate) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialDate: java.time.LocalDate = java.time.LocalDate.now(),
    minDate: java.time.LocalDate? = null,
    maxDate: java.time.LocalDate? = null,
    showDialog: Boolean = true,
    title: String = "选择日期",
    confirmText: String = "确定",
    dismissText: String = "取消",
    yearRange: IntRange = 1900..2100,
    dateFormat: java.time.format.DateTimeFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"),
    titleColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
    confirmButtonColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    dismissButtonColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.outline,
    dialogShape: androidx.compose.foundation.shape.RoundedCornerShape = RoundedCornerShape(20.dp),
    dialogElevation: androidx.compose.ui.unit.Dp = 24.dp
) {
    if (showDialog) {
        val datePickerState = androidx.compose.material3.rememberDatePickerState(
            initialSelectedDateMillis = initialDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli(),
            yearRange = yearRange,
            initialDisplayMode = androidx.compose.material3.DisplayMode.Picker
        )

        androidx.compose.material3.DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { epochMillis ->
                            val instant = java.time.Instant.ofEpochMilli(epochMillis)
                            val zoneId = java.time.ZoneId.systemDefault()
                            val selectedDate = java.time.LocalDate.ofInstant(instant, zoneId)
                            onDateSelected(selectedDate)
                        }
                        onDismiss()
                    },
                    colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                        contentColor = confirmButtonColor
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    androidx.compose.material3.Text(
                        text = confirmText,
                        style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                    )
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(
                    onClick = onDismiss,
                    colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                        contentColor = dismissButtonColor
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    androidx.compose.material3.Text(
                        text = dismissText,
                        style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                    )
                }
            },
            modifier = modifier,
            shape = dialogShape,
            tonalElevation = dialogElevation
        ) {
            androidx.compose.material3.DatePicker(
                state = datePickerState,
                title = {
                    androidx.compose.material3.Text(
                        text = title,
                        style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                        color = titleColor
                    )
                },
                showModeToggle = true
            )
        }
    }
}