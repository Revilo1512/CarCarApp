package com.example.carcarapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carcarapplication.TestValues.getUser

@Composable
fun DrawerContent(
    onNavigateToHome: () -> Unit,
    onNavigateToGroup: () -> Unit,
    onNavigateToUserSettings: () -> Unit,
    onNavigateLogOut: () -> Unit
) {
    val user = getUser() //This is an ugly solution probable even stupid; Should be removed with state or ViewModel
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        //Top Section
        Column{
            Text(
                text = user.username,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            HorizontalDivider()

            TextButton(onClick = onNavigateToHome) {
                Row (verticalAlignment = Alignment.CenterVertically){
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

            TextButton(onClick = onNavigateToGroup) {
                Row (verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person, //Need a better icon for that
                        contentDescription = "Select Group",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Group",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Dropdownthing",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            TextButton(onClick = onNavigateToUserSettings) {
                Row (verticalAlignment = Alignment.CenterVertically){
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

        //Bottom Section
        TextButton(onClick =  onNavigateLogOut) {
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
            onNavigateToUserSettings = { /* Do nothing */ },
            onNavigateLogOut = { /* Do nothing */ }
        )
    }
}