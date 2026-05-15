package com.yike.yikeledger.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yike.yikeledger.data.TransactionType
import com.yike.yikeledger.ui.theme.IncomeColor
import com.yike.yikeledger.ui.theme.ExpenseColor

import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.layout.Arrangement

/**
 * 现代金融科技风格交易类型选择器
 *
 * 设计理念：清晰的数据可视化、专业级财务工具美感、精细的微交互反馈
 */
@Composable
fun TransactionTypeSelector(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(24.dp)
    ) {
        // 收入选项
        TransactionTypeCard(
            type = TransactionType.INCOME,
            isSelected = selectedType == TransactionType.INCOME,
            onClick = { onTypeSelected(TransactionType.INCOME) },
            modifier = Modifier.weight(1f)
        )

        // 支出选项
        TransactionTypeCard(
            type = TransactionType.EXPENSE,
            isSelected = selectedType == TransactionType.EXPENSE,
            onClick = { onTypeSelected(TransactionType.EXPENSE) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TransactionTypeCard(
    type: TransactionType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isIncome = type == TransactionType.INCOME
    val primaryColor = if (isIncome) IncomeColor else ExpenseColor
    val title = if (isIncome) "收入" else "支出"
    val subtitle = if (isIncome) "+ 增加余额" else "− 减少余额"
    val icon = if (isIncome) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown

    // 增强动画状态 - 更流畅的动画
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 3.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "borderWidth"
    )

    val elevation by animateDpAsState(
        targetValue = if (isSelected) 20.dp else 6.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "elevation"
    )

    val containerAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.5f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "containerAlpha"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "iconScale"
    )

    val primaryColorAnimated by animateColorAsState(
        targetValue = primaryColor,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "primaryColor"
    )

    val glowIntensity by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "glowIntensity"
    )

    // 卡片背景 - 选中状态使用柔和主题色，未选中使用中性灰色
    val containerColor = if (isSelected) {
        Brush.linearGradient(
            colors = if (isIncome) {
                listOf(
                    IncomeColor.copy(alpha = 0.12f),
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            } else {
                listOf(
                    ExpenseColor.copy(alpha = 0.12f),
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            },
            start = Offset(0f, 0f),
            end = Offset(0f, 1f)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
            ),
            start = Offset(0f, 0f),
            end = Offset(0f, 1f)
        )
    }

    // 图标背景渐变 - 更精致的辐射渐变
    val iconBackgroundBrush = if (isSelected) {
        Brush.radialGradient(
            colors = if (isIncome) {
                listOf(
                    IncomeColor.copy(alpha = 0.3f),  // 中心绿色（收入）
                    Color(0xFF2196F3).copy(alpha = 0.15f),  // 中间蓝色
                    Color(0xFFE3F2FD).copy(alpha = 0.05f)   // 边缘浅色
                )
            } else {
                listOf(
                    ExpenseColor.copy(alpha = 0.3f),  // 中心红色（支出）
                    Color(0xFF9C27B0).copy(alpha = 0.15f),  // 中间紫色
                    Color(0xFFFCE4EC).copy(alpha = 0.05f)   // 边缘浅色
                )
            },
            center = androidx.compose.ui.geometry.Offset(0.5f, 0.5f),
            radius = 0.9f
        )
    } else {
        Brush.radialGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.1f)
            ),
            center = androidx.compose.ui.geometry.Offset(0.5f, 0.5f),
            radius = 0.8f
        )
    }

    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(24.dp),
                clip = false,
                ambientColor = primaryColorAnimated.copy(alpha = if (isSelected) 0.2f * glowIntensity else 0.02f),
                spotColor = primaryColorAnimated.copy(alpha = if (isSelected) 0.25f * glowIntensity else 0.03f)
            )
            .clickable(
                onClick = onClick,
                role = Role.Button
            )
    ) {
        // 外发光层
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(24.dp))
                    .drawBehind {
                        // 外发光效果
                        drawRoundRect(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    primaryColorAnimated.copy(alpha = 0.1f * glowIntensity),
                                    primaryColorAnimated.copy(alpha = 0.03f * glowIntensity),
                                    Color.Transparent
                                ),
                                center = androidx.compose.ui.geometry.Offset(center.x, center.y),
                                radius = size.width * 1.1f
                            ),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx())
                        )
                    }
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            border = BorderStroke(
                width = borderWidth,
                color = if (isSelected) {
                    // 选中状态：使用主色但更柔和的透明度，与背景渐变协调
                    primaryColorAnimated.copy(alpha = 0.6f)
                } else {
                    // 未选中：非常微妙的边框，几乎不可见
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.06f)
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(containerColor)
                    .drawBehind {
                        // 边框内侧发光 - 增强边框与背景的融合
                        if (isSelected) {
                            drawRoundRect(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        primaryColorAnimated.copy(alpha = 0.08f * glowIntensity),
                                        Color.Transparent
                                    ),
                                    center = androidx.compose.ui.geometry.Offset(center.x, center.y),
                                    radius = size.width * 0.5f
                                ),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx())
                            )
                        }
                    }
                    .padding(vertical = 32.dp, horizontal = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
                ) {
                    // 图标容器 - 带精致渐变背景
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(iconBackgroundBrush),
                        contentAlignment = Alignment.Center
                    ) {
                        // 图标发光效果
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .drawBehind {
                                        drawCircle(
                                            brush = Brush.radialGradient(
                                                colors = listOf(
                                                    primaryColorAnimated.copy(alpha = 0.2f),
                                                    Color.Transparent
                                                ),
                                                center = androidx.compose.ui.geometry.Offset(center.x, center.y),
                                                radius = size.width * 0.4f
                                            )
                                        )
                                    }
                            )
                        }

                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            modifier = Modifier
                                .size(40.dp)
                                .graphicsLayer {
                                    scaleX = iconScale
                                    scaleY = iconScale
                                    alpha = if (isSelected) 1f else 0.9f
                                },
                            tint = if (isSelected) primaryColorAnimated
                                   else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }

                    // 标题和副标题 - 增强排版
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 20.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                letterSpacing = (-0.3).sp
                            ),
                            color = if (isSelected) primaryColorAnimated
                                   else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                                letterSpacing = 0.2.sp
                            ),
                            color = if (isSelected) primaryColorAnimated.copy(alpha = 0.9f)
                                   else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }

                    // 动态选中指示器 - 带动画
                    Box(
                        modifier = Modifier
                            .width(if (isSelected) 32.dp else 12.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(2.5.dp))
                            .background(
                                if (isSelected) primaryColorAnimated.copy(alpha = 0.9f)
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            )
                    )
                }
            }
        }

        // 内发光层 - 增强选中效果
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(24.dp))
                    .drawBehind {
                        // 非常柔和的内部发光 - 增强边框与背景的融合
                        drawRoundRect(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    primaryColorAnimated.copy(alpha = 0.06f * glowIntensity),
                                    Color.Transparent
                                ),
                                center = androidx.compose.ui.geometry.Offset(center.x, center.y),
                                radius = size.width * 0.6f
                            ),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx())
                        )
                        
                        // 微妙的边框发光 - 增强边框与背景的融合
                        drawRoundRect(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    primaryColorAnimated.copy(alpha = 0.15f * glowIntensity),
                                    primaryColorAnimated.copy(alpha = 0.05f * glowIntensity),
                                    Color.Transparent
                                ),
                                center = androidx.compose.ui.geometry.Offset(center.x, center.y),
                                radius = size.width * 0.8f
                            ),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx()),
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                    }
            )
        }
    }
}

/**
 * 预览函数（仅用于开发时预览）
 */
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun TransactionTypeSelectorPreview() {
    androidx.compose.material3.Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(24.dp)
        ) {
            TransactionTypeSelector(
                selectedType = TransactionType.INCOME,
                onTypeSelected = {}
            )

            TransactionTypeSelector(
                selectedType = TransactionType.EXPENSE,
                onTypeSelected = {}
            )
        }
    }
}