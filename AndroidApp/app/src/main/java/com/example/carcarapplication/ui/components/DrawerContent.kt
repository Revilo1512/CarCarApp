package com.example.carcarapplication.ui.components

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carcarapplication.TestValues.getGroups
import com.example.carcarapplication.TestValues.getUser
import com.example.carcarapplication.api_helpers.RetrofitClient
import com.example.carcarapplication.data_classes.Group
import com.example.carcarapplication.data_classes.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DrawerContent(
    user: User,
    groups: List<Group>,
    onNavigateToHome: () -> Unit,
    onNavigateToGroup: (String) -> Unit,
    onNavigateToGroupCreation: () -> Unit,
    onNavigateToUserSettings: () -> Unit,
    onNavigateLogOut: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val currentGroups = remember { mutableStateOf<List<Group>>(emptyList()) }

    // Initial fetch for groups
    remember {
        scope.launch {
            val updatedGroups = updateGroups(user.userID, context)
            if (updatedGroups != null) {
                currentGroups.value = updatedGroups
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Section

        Column {
            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            HorizontalDivider()

            TextButton(onClick = onNavigateToHome) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Home",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            // Dropdown Menu for Groups
            var expanded by remember { mutableStateOf(false) }

            Box {
                TextButton(
                    onClick = {
                        if (currentGroups.value.isNotEmpty()) {
                            expanded = !expanded
                        }
                        scope.launch {
                            val updatedGroups = updateGroups(user.userID, context)
                            currentGroups.value = updatedGroups
                        }
                    },
                    enabled = currentGroups.value.isNotEmpty() // Disable button if no groups
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Group",
                            modifier = Modifier.size(24.dp),
                            tint = if (currentGroups.value.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Your Groups",
                            style = MaterialTheme.typography.titleLarge,
                            color = if (currentGroups.value.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (currentGroups.value.isNotEmpty()) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        currentGroups.value.forEach { group ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = group.name,
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    onNavigateToGroup(group.name)
                                }
                            )
                        }
                    }
                }
            }

            TextButton(onClick = onNavigateToGroupCreation) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Group",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Create Group",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            TextButton(onClick = onNavigateToUserSettings) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }

        // Bottom Section
        TextButton(onClick = onNavigateLogOut) {
            Text(
                text = "Log Out",
                style = MaterialTheme.typography.titleMedium,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 320) // Width for typical drawer layout
@Composable
fun PreviewDrawerContent() {
    MaterialTheme {
        DrawerContent(
            onNavigateToHome = { /* Do nothing */ },
            onNavigateToGroup = { /* Do nothing */ },
            onNavigateToGroupCreation = {},
            onNavigateToUserSettings = { /* Do nothing */ },
            onNavigateLogOut = { /* Do nothing */ },
            groups = getGroups(),
            user = getUser()
        )
    }
}

suspend fun updateGroups(
    userID: Long,
    context: Context
): List<Group> {
    return try {
        val feedback = RetrofitClient.apiService.getGroupsOfUser(userID)
        if (feedback.isEmpty()) {
            Log.d("Update Drawer Groups", "No groups found for user: ID=$userID")
        } else {
            Log.d("Update Drawer Groups", "Fetched ${feedback.size} groups for user: ID=$userID")
        }
        feedback
    } catch (e: Exception) {
        e.printStackTrace()
        Log.d("updateDrawerGroup", "Error: ${e.message}", e)
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Failed to fetch groups", Toast.LENGTH_SHORT).show()
        }
        emptyList() // Return an empty list on error
    }
}
