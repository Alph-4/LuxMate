package org.julienjnnqin.luxmateapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewBottomNavigationBar() {
    MaterialTheme {
        BottomNavigationBar(
            selectedIndex = 0,
            onItemSelected = {}
        )
    }
}

@Preview
@Composable
fun PreviewTopAppBarTeachers() {
    MaterialTheme {
        TopAppBarTeachers()
    }
}

@Preview
@Composable
fun PreviewTopAppBarProfile() {
    MaterialTheme {
        TopAppBarProfile()
    }
}

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(103.dp)
            .background(Color.Transparent)
            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            .dropShadow(
                shape = RoundedCornerShape(38.dp),
                shadow = Shadow(
                    radius = 38.dp,
                    spread = 0.dp,
                    color = Color(0x40000000),
                    offset = DpOffset(x = 0.dp, 25.dp)
                )
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(38.dp)
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Filled.Home,
                label = "Home",
                isSelected = selectedIndex == 0,
                onClick = { onItemSelected(0) }
            )
            BottomNavItem(
                icon = Icons.Filled.BarChart,
                label = "Personas",
                isSelected = selectedIndex == 1,
                onClick = { onItemSelected(1) }
            )
            BottomNavItem(
                icon = Icons.Filled.School,
                label = "Chat",
                isSelected = selectedIndex == 2,
                onClick = { onItemSelected(2) }
            )
            BottomNavItem(
                icon = Icons.Filled.Person,
                label = "Profile",
                isSelected = selectedIndex == 3,
                onClick = { onItemSelected(3) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(size = 16.dp))
                .clickable(onClick = onClick)
                .border(
                    shape = RoundedCornerShape(size = 16.dp), color = MaterialTheme.colorScheme.surface,
                    width = 0.dp
                )
                .size(64.dp)
                .background(
                    color = if (isSelected)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    else
                        MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )


                Text(
                    text = label,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 15.sp),
                    color = if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

        }
    }
}

@Composable
fun TopAppBarTeachers(
    title: String = "Choisissez votre professeur",
    onMenuClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                )
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onMenuClick() },
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Box {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onNotificationClick() },
                    tint = MaterialTheme.colorScheme.primary
                )
                // Badge
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            color = Color(0xFF22c55e),
                            shape = CircleShape
                        )
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
fun TopAppBarProfile(
    title: String = "Mon Profil",
    onBackClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onBackClick() },
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Box(modifier = Modifier.size(28.dp))
        }
    }
}





