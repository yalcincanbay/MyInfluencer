package com.mustafa.influencer.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mustafa.influencer.shared.FirebaseManager
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit
) {
    var currentStep by remember { mutableStateOf(1) } // 1: tip seçimi, 2: email/pass
    var selectedUserType by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLogin by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (currentStep == 1) {
            Text("Hoş Geldiniz", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Text(
                "Hesap tipinizi seçin",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
            )

            Button(
                onClick = { selectedUserType = "influencer"; currentStep = 2 },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) { Text("Influencer") }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { selectedUserType = "advertiser"; currentStep = 2 },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) { Text("Advertiser (Reklam Veren)") }

        } else {
            Text(
                text = if (isLogin) "Giriş Yap" else "Kayıt Ol",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (selectedUserType == "influencer") "Influencer" else "Advertiser",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-posta") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Şifre") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (errorMessage.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(errorMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Lütfen tüm alanları doldurun"
                        return@Button
                    }

                    isLoading = true
                    errorMessage = ""

                    scope.launch {
                        val result = if (isLogin) {
                            FirebaseManager.signInWithEmail(email, password)
                        } else {
                            if (selectedUserType.isBlank()) {
                                isLoading = false
                                errorMessage = "Kullanıcı tipi seçilmedi"
                                return@launch
                            }
                            FirebaseManager.signUpWithEmail(email, password, selectedUserType)
                        }

                        isLoading = false

                        result.onSuccess {
                            onAuthSuccess()
                        }.onFailure { ex ->
                            errorMessage = ex.message ?: "Bir hata oluştu"
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                else Text(if (isLogin) "Giriş Yap" else "Kayıt Ol")
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = { isLogin = !isLogin; errorMessage = "" }) {
                Text(if (isLogin) "Hesabınız yok mu? Kayıt olun" else "Zaten hesabınız var mı? Giriş yapın")
            }

            TextButton(
                onClick = {
                    currentStep = 1
                    email = ""
                    password = ""
                    errorMessage = ""
                }
            ) { Text("← Geri") }
        }
    }
}
