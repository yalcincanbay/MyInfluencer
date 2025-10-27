package com.mustafa.influencer.advertiser

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mustafa.influencer.shared.FirebaseManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvertiserProfileSetupScreen(
    onSetupComplete: () -> Unit
) {
    var companyName by remember { mutableStateOf("") }
    var selectedPlatforms by remember { mutableStateOf(setOf<String>()) }
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val availablePlatforms = listOf("YouTube", "TikTok", "Instagram", "Twitter", "Facebook")
    val availableCategories = listOf(
        "Spor", "Makyaj", "Elektronik", "Oyun", "Yemek",
        "Moda", "Seyahat", "Eğitim", "Müzik", "Sanat"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Şirket Profili Kurulumu") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Hoş Geldiniz!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Şirket profilinizi oluşturmak için birkaç bilgiye ihtiyacımız var",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Şirket İsmi
            Text(
                text = "Şirketinizin İsmi *",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = companyName,
                onValueChange = { companyName = it },
                label = { Text("Şirket İsmi") },
                placeholder = { Text("Örn: ABC Teknoloji A.Ş.") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Platformlar Seçimi
            Text(
                text = "Hangi platformlarda reklam vermek istiyorsunuz? *",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            availablePlatforms.forEach { platform ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedPlatforms.contains(platform.lowercase()),
                        onCheckedChange = { checked ->
                            selectedPlatforms = if (checked) {
                                selectedPlatforms + platform.lowercase()
                            } else {
                                selectedPlatforms - platform.lowercase()
                            }
                        }
                    )
                    Text(platform)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // İçerik Kategorileri
            Text(
                text = "Hangi alanlarda reklam vermek istiyorsunuz? *",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Kategoriler için grid yapı
            availableCategories.chunked(2).forEach { rowCategories ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowCategories.forEach { category ->
                        FilterChip(
                            selected = selectedCategories.contains(category.lowercase()),
                            onClick = {
                                selectedCategories = if (selectedCategories.contains(category.lowercase())) {
                                    selectedCategories - category.lowercase()
                                } else {
                                    selectedCategories + category.lowercase()
                                }
                            },
                            label = { Text(category) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Eğer tek eleman varsa boş alan ekle
                    if (rowCategories.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // Hata mesajı
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Kaydet Butonu
            Button(
                onClick = {
                    when {
                        companyName.isBlank() -> {
                            errorMessage = "Lütfen şirket ismini girin"
                            return@Button
                        }
                        selectedPlatforms.isEmpty() -> {
                            errorMessage = "Lütfen en az bir platform seçin"
                            return@Button
                        }
                        selectedCategories.isEmpty() -> {
                            errorMessage = "Lütfen en az bir alan seçin"
                            return@Button
                        }
                    }

                    isLoading = true
                    errorMessage = ""

                    scope.launch {
                        val result = FirebaseManager.saveAdvertiserProfile(
                            companyName = companyName,
                            platforms = selectedPlatforms.toList(),
                            categories = selectedCategories.toList()
                        )

                        isLoading = false

                        result.onSuccess {
                            onSetupComplete()
                        }.onFailure { exception ->
                            errorMessage = exception.message ?: "Profil kaydedilemedi"
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Kaydet ve Devam Et")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
