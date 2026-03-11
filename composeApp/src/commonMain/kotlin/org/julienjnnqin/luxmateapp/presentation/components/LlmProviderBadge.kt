package org.julienjnnqin.luxmateapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme

private val providerDeepseekColor = Color(0xFF0EA5E9)
private val providerMistralColor = Color(0xFF6E57E0)
private val providerGeminiColor = Color(0xFF1A73E8)

private fun providerInfo(provider: String): Pair<String, Color> = when {
    provider.equals("mistral", ignoreCase = true) -> "Mistral AI" to providerMistralColor
    provider.equals("gemini", ignoreCase = true) -> "Google Gemini" to providerGeminiColor
    else -> "DeepSeek AI" to providerDeepseekColor
}

@Composable
fun LlmProviderBadge(
    provider: String,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 10.dp,
    verticalPadding: Dp = 5.dp,
    iconSize: Dp = 14.dp
) {
    val (label, badgeColor) = providerInfo(provider)
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = badgeColor.copy(alpha = 0.12f),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = verticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.SmartToy,
                contentDescription = null,
                tint = badgeColor,
                modifier = Modifier.size(iconSize)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = badgeColor
            )
        }
    }
}
