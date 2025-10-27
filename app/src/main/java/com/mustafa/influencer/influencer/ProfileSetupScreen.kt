package com.mustafa.influencer.influencer

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
fun ProfileSetupScreen(
    onSetupComplete: () -> Unit
) {
    var selectedPlatforms by remember { mutableStateOf(setOf<String>()) }
    var youtubeLink by remember { mutableStateOf("") }
    var tiktokLink by remember { mutableStateOf("") }
    var instagramLink by remember { mutableStateOf("") }
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }
    var bio by remember { mutableStateOf("") }
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
                title = { Text("Profil Kurulumu") }
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
                text = "Profilinizi oluşturmak için birkaç bilgiye ihtiyacımız var",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Platformlar Seçimi
            Text(
                text = "Hangi platformlarda aktifsiniz? *",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Platform Linkleri (Opsiyonel)
            if (selectedPlatforms.isNotEmpty()) {
                Text(
                    text = "Platform Linkleri (İsteğe bağlı)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (selectedPlatforms.contains("youtube")) {
                    OutlinedTextField(
                        value = youtubeLink,
                        onValueChange = { youtubeLink = it },
                        label = { Text("YouTube Kanal Linki") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        singleLine = true
                    )
                }

                if (selectedPlatforms.contains("tiktok")) {
                    OutlinedTextField(
                        value = tiktokLink,
                        onValueChange = { tiktokLink = it },
                        label = { Text("TikTok Profil Linki") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        singleLine = true
                    )
                }

                if (selectedPlatforms.contains("instagram")) {
                    OutlinedTextField(
                        value = instagramLink,
                        onValueChange = { instagramLink = it },
                        label = { Text("Instagram Profil Linki") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // İçerik Kategorileri
            Text(
                text = "İçerik Kategorileriniz *",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Kategoriler için FlowRow benzeri yapı (basit grid)
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

            Spacer(modifier = Modifier.height(16.dp))

            // Bio (Opsiyonel)
            Text(
                text = "Hakkınızda (İsteğe bağlı)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Kendinizi tanıtın") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5,
                placeholder = { Text("Örn: Spor ve sağlıklı yaşam üzerine içerikler üretiyorum...") }
            )

            // Hata mesajı
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Kaydet Butonu
            Button(
                onClick = {
                    if (selectedPlatforms.isEmpty()) {
                        errorMessage = "Lütfen en az bir platform seçin"
                        return@Button
                    }
                    if (selectedCategories.isEmpty()) {
                        errorMessage = "Lütfen en az bir kategori seçin"
                        return@Button
                    }

                    isLoading = true
                    errorMessage = ""

                    val platformLinks = mutableMapOf<String, String>()
                    if (youtubeLink.isNotBlank()) platformLinks["youtube"] = youtubeLink
                    if (tiktokLink.isNotBlank()) platformLinks["tiktok"] = tiktokLink
                    if (instagramLink.isNotBlank()) platformLinks["instagram"] = instagramLink

                    scope.launch {
                        val result = FirebaseManager.saveInfluencerProfile(
                            platforms = selectedPlatforms.toList(),
                            platformLinks = platformLinks,
                            categories = selectedCategories.toList(),
                            bio = bio
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

            // Şimdilik Atla Butonu
            TextButton(
                onClick = {
                    if (selectedPlatforms.isEmpty() || selectedCategories.isEmpty()) {
                        errorMessage = "Platform ve kategori seçimi zorunludur"
                        return@TextButton
                    }

                    isLoading = true
                    errorMessage = ""

                    scope.launch {
                        val result = FirebaseManager.saveInfluencerProfile(
                            platforms = selectedPlatforms.toList(),
                            platformLinks = emptyMap(),
                            categories = selectedCategories.toList(),
                            bio = ""
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Link ve Açıklama Olmadan Devam Et")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
