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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvertiserProfileSetupScreen(
    onSetupComplete: () -> Unit
) {
    val viewModel = remember { AdvertiserProfileSetupViewModel() }
    val uiState by viewModel.state.collectAsState()

    val availablePlatforms = listOf("YouTube", "TikTok", "Instagram", "Twitter", "Facebook")
    val availableCategories = listOf(
        "Spor", "Makyaj", "Elektronik", "Oyun", "Yemek",
        "Moda", "Seyahat", "Eğitim", "Müzik", "Sanat"
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Şirket Profili Kurulumu") }) }
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

            Text(
                text = "Şirketinizin İsmi *",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = uiState.companyName,
                onValueChange = viewModel::setCompanyName,
                label = { Text("Şirket İsmi") },
                placeholder = { Text("Örn: ABC Teknoloji A.Ş.") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Hangi platformlarda reklam vermek istiyorsunuz? *",
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
                        onCheckedChange = { viewModel.togglePlatform(key) }
                    )
                    Text(platform)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Hangi alanlarda reklam vermek istiyorsunuz? *",
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
                            onClick = { viewModel.toggleCategory(key) },
                            label = { Text(category) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowCategories.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }

            if (uiState.errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.submit(onSuccess = onSetupComplete) },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(56.dp)
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

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
