package com.example.carcarapplication.ui.screens

import DriveState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DriveInfoScreen(
    onNavigateToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Drive Information", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                DriveState.isDriving.value = false
                onNavigateToHome()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("End Drive")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDriveInfoScreen() {
    DriveInfoScreen(
        onNavigateToHome = {}
    )
}