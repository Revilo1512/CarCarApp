package com.example.carcarapplication.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.carcarapplication.MainActivity
import com.example.carcarapplication.R
import com.example.carcarapplication.api_helpers.ApiService
import com.example.carcarapplication.api_helpers.RetrofitClient
import com.example.carcarapplication.data_classes.User
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun RegisterScreen(onNavigateToLogin: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val apiService = RetrofitClient.instance.create(ApiService::class.java)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.carcaricon),
            contentDescription = "bitmap",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 20.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        if (loading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 20.dp))
        } else {
            Button(
                onClick = {
                    loading = true
                    (context as? ComponentActivity)?.lifecycleScope?.launch {
                        try {
                            val user = apiService.postUser(username, email, password)
                            Toast.makeText(context, "Register successful!", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(context, MainActivity::class.java).apply {
                                putExtra("user", user)
                            }
                            context.startActivity(intent)
                        } catch (e: HttpException) {
                            Toast.makeText(context, "Registering failed", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show()
                        } finally {
                            loading = false
                        }
                    }
                },
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(text = "Register")
            }
        }

        Text(
            text = "or login with existing account",
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(top = 10.dp)
                .clickable {
                    onNavigateToLogin()
                }
        )

        Text(
            text = "use test user",
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(top = 10.dp)
                .clickable {
                    val testUser = User(
                        userID = 9999999,
                        username = "testuser",
                        email = "testuser@email.com",
                    )
                    val intent = Intent(context, MainActivity::class.java).apply {
                        putExtra("user", testUser)
                    }
                    context.startActivity(intent)
                }
        )
    }
}