package org.julienjnnqin.luxmateapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
    leadingBtn: Boolean = true,
    leadingBtnAction: () -> Unit = {},
    title: String? = null,
    trailingBtn: Boolean = false,
    trailingBtnAction: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth(0.94f)
            .height(64.dp)
            .shadow(20.dp, RoundedCornerShape(28.dp), ambientColor = Color.LightGray)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(24.dp)
            )
            .clip(RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingBtn) BtnNavItem(onTap = leadingBtnAction) else Spacer(modifier = Modifier.size(40.dp))
        if (title != null) TextNavItem(title) else Spacer(modifier = Modifier.size(40.dp))
        if (trailingBtn) BtnNavItem(trailingBtnAction) else Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
fun TextNavItem(title: String) {
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(title, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun BtnNavItem(onTap: () -> Unit) {
    Icon(
        imageVector = Icons.Filled.ChevronLeft,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .size(24.dp)
            .clickable(onClick = onTap)
    )
}