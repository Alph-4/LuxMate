package org.julienjnnqin.luxmateapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewHeaderBar() {
    MaterialTheme {
        HeaderBar(
        )
    }
}


@Composable
fun HeaderBar(
    modifier: Modifier = Modifier,
    trailingBtn: Boolean = true,
    trailingBtnAction: () -> Unit = {},
    title: String? = null,
    leadingBtn: Boolean = false,
    leadingBtnAction: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                shape = RoundedCornerShape(24.dp)
            )
            .clip(RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingBtn) BtnNavItem(onTap = trailingBtnAction) else Spacer(modifier = Modifier.size(40.dp))
        if (title != null) TextNavItem(title) else Spacer(modifier = Modifier.size(40.dp))
        if (trailingBtn) BtnNavItem(leadingBtnAction) else Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
fun TextNavItem(title: String) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(title, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun BtnNavItem(onTap: () -> Unit) {
    Box(
        modifier = Modifier.size(40.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Notifications,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        // Badge
        Box(
            modifier = Modifier.size(10.dp)
                .background(
                    color = Color(0xFF22c55e),
                    shape = CircleShape
                )
                .align(Alignment.TopEnd)
        )
    }
}