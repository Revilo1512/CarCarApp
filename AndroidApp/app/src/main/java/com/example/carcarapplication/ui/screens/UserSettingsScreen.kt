package com.example.carcarapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carcarapplication.api_helpers.RetrofitClient

@Composable
fun UserSettingsScreen() {
    val user = RetrofitClient.getUser()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Aligns items horizontally at the center
        verticalArrangement = Arrangement.Top // Aligns items from the top
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(Color(0xFFDFCEA1))
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Your Profile",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(bottom = 20.dp),
                )
            }

            HorizontalDivider(thickness = 2.dp, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Username:",
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
                Text(
                    text = user.name,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontSize = 20.sp,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "UserID:",
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
                Text(
                    text = "${user.userID}",
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontSize = 20.sp,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Email:",
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
                Text(
                    text = user.email,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp,
                )
            }
        }

        Text(
            text = "Coming soon...",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .padding(top = 40.dp)
        )
    }
}