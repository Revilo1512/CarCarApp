package com.example.carcarapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrawerContent(
    onNavigateToHome: () -> Unit,
    onNavigateToGroup: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "App Navigation",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        HorizontalDivider()
        TextButton(onClick = onNavigateToHome) {
            Text(
                text = "Home",
                fontSize = 16.sp
            )
        }
        TextButton(onClick = onNavigateToGroup) {
            Text(
                text = "Group",
                fontSize = 16.sp
            )
        }
    }
}
