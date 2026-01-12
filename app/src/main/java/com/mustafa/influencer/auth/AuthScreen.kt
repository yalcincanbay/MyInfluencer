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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mustafa.influencer.domain.model.UserType

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    vm: AuthViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (state.currentStep == 1) {
            Text(
                text = "Hoş Geldiniz",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Hesap tipinizi seçin",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
            )

            Button(
                onClick = { vm.selectUserType(UserType.INFLUENCER) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) { Text("Influencer") }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { vm.selectUserType(UserType.ADVERTISER) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) { Text("Advertiser (Reklam Veren)") }

        } else {
            Text(
                text = if (state.isLogin) "Giriş Yap" else "Kayıt Ol",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = when (state.selectedUserType) {
                    UserType.INFLUENCER -> "Influencer"
                    UserType.ADVERTISER -> "Advertiser"
                    else -> ""
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = vm::setEmail,
                label = { Text("E-posta") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = vm::setPassword,
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

            if (state.errorMessage.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    // router ile çözüyoruz; AuthSuccess olunca LoadingRouter'a gideceğiz
                    vm.submit {
                        onAuthSuccess()
                    }
                },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(if (state.isLogin) "Giriş Yap" else "Kayıt Ol")
                }
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = vm::toggleLogin) {
                Text(if (state.isLogin) "Hesabınız yok mu? Kayıt olun" else "Zaten hesabınız var mı? Giriş yapın")
            }

            TextButton(onClick = vm::goBack) {
                Text("← Geri")
            }
        }
    }
}
