package com.example.carcarapplication.ui.screens

import android.content.Intent
import android.util.Log
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
                        val request = User(0, username, email, password)
                        try {
                            // Register the user
                            val user = RetrofitClient.apiService.postUser(request)
                            Toast.makeText(context, "Register successful!", Toast.LENGTH_SHORT).show()

                            // Now login the user by calling getUser API
                            val loggedInUser = RetrofitClient.apiService.getUser(email, password)
                            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()

                            // Save user session
                            RetrofitClient.setUser(loggedInUser)

                            // Navigate to the MainActivity
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        } catch (e: HttpException) {
                            Log.e("RegisterScreen", "Registration failed: ${e.message()}")
                            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Log.e("RegisterScreen", "An error occurred: ${e.message}")
                            Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
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
    }
}