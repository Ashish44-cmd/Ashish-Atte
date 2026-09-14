package com.example.ashishatte

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ashishatte.auth.AuthService
import com.example.ashishatte.ui.AdminScreen
import com.example.ashishatte.ui.EmployeeScreen
import kotlinx.coroutines.launch

@Composable
fun AttendanceApp() {
    val auth = remember { AuthService() }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var signedIn by remember { mutableStateOf(auth.isSignedIn()) }
    var isAdmin by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var screen by remember { mutableStateOf("home") }

    fun signOut() {
        auth.signOut()
        signedIn = false
        isAdmin = false
        screen = "home"
        password = ""
    }

    if (!signedIn) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Ashish Atte", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(4.dp))
            Text("Secure GPS attendance")
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            Button(
                enabled = !loading && email.isNotBlank() && password.isNotBlank(),
                onClick = {
                    scope.launch {
                        loading = true
                        error = ""
                        try {
                            auth.signIn(email, password)
                            isAdmin = auth.isAdmin()
                            signedIn = true
                        } catch (e: Exception) {
                            error = e.message ?: "Login failed. Check email/password."
                        } finally {
                            loading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (loading) "Signing in..." else "Sign In")
            }

            if (error.isNotBlank()) {
                Spacer(Modifier.height(10.dp))
                Text(error, color = MaterialTheme.colorScheme.error)
            }
        }
        return
    }

    when (screen) {
        "employee" -> EmployeeScreen(
            onBack = { screen = "home" },
            onSignOut = { signOut() }
        )

        "admin" -> AdminScreen(
            onBack = { screen = "home" },
            onSignOut = { signOut() }
        )

        else -> Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Welcome", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(18.dp))

            Button(
                onClick = { screen = "employee" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Employee Attendance")
            }

            if (isAdmin) {
                Spacer(Modifier.height(10.dp))
                OutlinedButton(
                    onClick = { screen = "admin" },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Admin Dashboard")
                }
            }

            Spacer(Modifier.height(10.dp))
            TextButton(onClick = { signOut() }) {
                Text("Sign Out")
            }
        }
    }
}
