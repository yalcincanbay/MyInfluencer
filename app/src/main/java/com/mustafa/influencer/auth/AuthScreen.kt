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
    onAuthSuccess: (String) -> Unit // userType'ı döndürür
) {
    var currentStep by remember { mutableStateOf(1) } // 1: Tip seçimi, 2: E-posta/şifre
    var selectedUserType by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLogin by remember { mutableStateOf(true) } // true: Giriş, false: Kayıt
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (currentStep == 1) {
            // Adım 1: Kullanıcı tipi seçimi
            Text(
                text = "Hoş Geldiniz",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Hesap tipinizi seçin",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            Button(
                onClick = {
                    selectedUserType = "influencer"
                    currentStep = 2
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Influencer",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    selectedUserType = "advertiser"
                    currentStep = 2
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = "Advertiser (Reklam Veren)",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        } else {
            // Adım 2: E-posta ve şifre girişi
            Text(
                text = if (isLogin) "Giriş Yap" else "Kayıt Ol",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = if (selectedUserType == "influencer") "Influencer" else "Advertiser",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // E-posta alanı
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-posta") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Şifre alanı
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
                            contentDescription = if (passwordVisible) "Şifreyi gizle" else "Şifreyi göster"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Hata mesajı
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ana buton (Giriş/Kayıt)
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
                            FirebaseManager.signUpWithEmail(email, password, selectedUserType)
                        }

                        isLoading = false

                        result.onSuccess {
                            onAuthSuccess(selectedUserType)
                        }.onFailure { exception ->
                            errorMessage = when {
                                exception.message?.contains("password") == true -> "Şifre en az 6 karakter olmalıdır"
                                exception.message?.contains("email") == true -> "Geçersiz e-posta adresi"
                                exception.message?.contains("user-not-found") == true -> "Kullanıcı bulunamadı"
                                exception.message?.contains("wrong-password") == true -> "Yanlış şifre"
                                exception.message?.contains("email-already-in-use") == true -> "Bu e-posta zaten kullanımda"
                                else -> exception.message ?: "Bir hata oluştu"
                            }
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = if (isLogin) "Giriş Yap" else "Kayıt Ol",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Giriş/Kayıt geçiş butonu
            TextButton(
                onClick = {
                    isLogin = !isLogin
                    errorMessage = ""
                }
            ) {
                Text(
                    text = if (isLogin) "Hesabınız yok mu? Kayıt olun" else "Zaten hesabınız var mı? Giriş yapın"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Geri butonu
            TextButton(
                onClick = {
                    currentStep = 1
                    email = ""
                    password = ""
                    errorMessage = ""
                }
            ) {
                Text(text = "← Geri")
            }
        }
    }
}
