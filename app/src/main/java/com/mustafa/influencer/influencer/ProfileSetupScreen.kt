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
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    onSetupComplete: () -> Unit,
    vm: ProfileSetupViewModel = viewModel()
) {
    val uiState by vm.state.collectAsState()

    val availablePlatforms = listOf("YouTube", "TikTok", "Instagram", "Twitter", "Facebook")
    val availableCategories = listOf(
        "Spor", "Makyaj", "Elektronik", "Oyun", "Yemek",
        "Moda", "Seyahat", "Eğitim", "Müzik", "Sanat"
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profil Kurulumu") }) }
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

            Text(
                text = "Hangi platformlarda aktifsiniz? *",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            availablePlatforms.forEach { platform ->
                val key = platform.lowercase()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.selectedPlatforms.contains(key),
                        onCheckedChange = { vm.togglePlatform(key) }
                    )
                    Text(platform)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.selectedPlatforms.isNotEmpty()) {
                Text(
                    text = "Platform Linkleri (İsteğe bağlı)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (uiState.selectedPlatforms.contains("youtube")) {
                    OutlinedTextField(
                        value = uiState.youtubeLink,
                        onValueChange = vm::setYoutube,
                        label = { Text("YouTube Kanal Linki") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        singleLine = true
                    )
                }

                if (uiState.selectedPlatforms.contains("tiktok")) {
                    OutlinedTextField(
                        value = uiState.tiktokLink,
                        onValueChange = vm::setTiktok,
                        label = { Text("TikTok Profil Linki") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        singleLine = true
                    )
                }

                if (uiState.selectedPlatforms.contains("instagram")) {
                    OutlinedTextField(
                        value = uiState.instagramLink,
                        onValueChange = vm::setInstagram,
                        label = { Text("Instagram Profil Linki") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = "İçerik Kategorileriniz *",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            availableCategories.chunked(2).forEach { rowCategories ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowCategories.forEach { category ->
                        val key = category.lowercase()
                        FilterChip(
                            selected = uiState.selectedCategories.contains(key),
                            onClick = { vm.toggleCategory(key) },
                            label = { Text(category) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowCategories.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Hakkınızda (İsteğe bağlı)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = uiState.bio,
                onValueChange = vm::setBio,
                label = { Text("Kendinizi tanıtın") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5,
                placeholder = { Text("Örn: Spor ve sağlıklı yaşam üzerine içerikler üretiyorum...") }
            )

            if (uiState.errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { vm.submit(withLinks = true, onSuccess = onSetupComplete) },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Kaydet ve Devam Et")
                }
            }

            TextButton(
                onClick = { vm.submit(withLinks = false, onSuccess = onSetupComplete) },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Link ve Açıklama Olmadan Devam Et")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
