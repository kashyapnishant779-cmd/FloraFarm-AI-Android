package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.PlantDataset
import com.example.data.PlantEncyclopediaItem
import com.example.ui.PlantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncyclopediaTab(viewModel: PlantViewModel, modifier: Modifier = Modifier) {
    val selectedPlant by viewModel.selectedPlant.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val filteredPlants = remember(searchQuery) {
        if (searchQuery.trim().isEmpty()) {
            PlantDataset.plants
        } else {
            PlantDataset.plants.filter {
                it.commonName.contains(searchQuery, ignoreCase = true) ||
                        it.scientificName.contains(searchQuery, ignoreCase = true) ||
                        it.localNames.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Search bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("encyclopedia_search_input")
                    .padding(top = 8.dp),
                placeholder = { Text("Search 10,000+ plants by name, family...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
        }

        // Horizontal Row of Quick Explore
        item {
            Column {
                Text(
                    text = "Global Botanical Database",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredPlants) { plant ->
                        val isSelected = selectedPlant?.scientificName == plant.scientificName
                        Card(
                            onClick = { viewModel.selectPlant(plant) },
                            modifier = Modifier
                                .width(140.dp)
                                .height(180.dp)
                                .testTag("plant_card_${plant.commonName.lowercase()}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                AsyncImage(
                                    model = plant.imageUrl,
                                    contentDescription = plant.commonName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(16.dp))
                                )

                                // Overlay gradient for title legibility
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                                                startY = 100f
                                            )
                                        )
                                )

                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = plant.commonName,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = plant.scientificName,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.White.copy(alpha = 0.8f),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Detail Panel of Selected Plant
        selectedPlant?.let { plant ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Hero section
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            AsyncImage(
                                model = plant.imageUrl,
                                contentDescription = plant.commonName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )
                            Column {
                                Text(
                                    text = plant.commonName,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = plant.scientificName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Family: ${plant.family} | Genus: ${plant.genus}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Taxonomic Details
                        Text(
                            text = "Taxonomic & Regional Identity",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TaxonomyRow("Local Names", plant.localNames)
                        TaxonomyRow("Species", plant.species)
                        TaxonomyRow("Native Country", plant.originCountry)
                        TaxonomyRow("Native Region", plant.nativeRegion)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botanical Profile & Importance
                        Text(
                            text = "Botanical Profile & Uses",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ProfileCard("Botanical Information", plant.botanicalInfo, Icons.Default.Info)
                        ProfileCard("Primary Uses", plant.uses, Icons.Default.LocalFlorist)
                        ProfileCard("Medicinal Uses", plant.medicinalUses, Icons.Default.Healing)
                        ProfileCard("Nutritional Value", plant.nutritionalInfo, Icons.Default.Favorite)
                        ProfileCard("Economic Importance", plant.economicImportance, Icons.Default.MonetizationOn)
                        ProfileCard("Scientific Research", plant.scientificResearch, Icons.Default.Science)

                        Spacer(modifier = Modifier.height(20.dp))

                        // Growing Intelligence System
                        Text(
                            text = "Complete Growing Intelligence",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            GrowingGridCard("Best Season", plant.bestGrowingSeason, Icons.Default.WbSunny, Modifier.weight(1f))
                            GrowingGridCard("Temperature", plant.temperatureRequirement, Icons.Default.DeviceThermostat, Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            GrowingGridCard("Humidity", plant.humidityRequirement, Icons.Default.WaterDrop, Modifier.weight(1f))
                            GrowingGridCard("Sunlight", plant.sunlightRequirement, Icons.Default.Brightness5, Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            GrowingGridCard("Soil Type", plant.soilType, Icons.Default.Grass, Modifier.weight(1f))
                            GrowingGridCard("Soil pH", plant.soilPh, Icons.Default.FilterList, Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        ProfileCard("Soil Preparation", plant.soilPreparation, Icons.Default.Construction)

                        Spacer(modifier = Modifier.height(20.dp))

                        // Exact Quantity Recommendations
                        Text(
                            text = "Exact Quantity Recommendation Engine",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        QuantityCard("Watering Needs", plant.waterQuantity, Icons.Default.Opacity)
                        QuantityCard("Fertilizer Dosage", plant.fertilizerQuantity, Icons.Default.FilterFrames)
                        QuantityCard("Organic Compost", plant.compostQuantity, Icons.Default.Park)
                        QuantityCard("Spacing Distance", plant.spacingDistance, Icons.Default.FormatLineSpacing)
                        QuantityCard("Planting Depth", plant.plantingDepth, Icons.Default.ArrowDownward)
                        QuantityCard("Pot / Container", plant.potOrContainerSize, Icons.Default.Inbox)
                        QuantityCard("Field Spacing", plant.fieldSpacing, Icons.Default.GridOn)

                        Spacer(modifier = Modifier.height(20.dp))

                        // AI Growth Timeline
                        Text(
                            text = "AI Botanical Lifecycle Growth Timeline",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        plant.timeline.forEach { step ->
                            TimelineRow(step.day, step.title, step.description)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaxonomyRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.widthIn(max = 220.dp))
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
}

@Composable
fun ProfileCard(title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(text = description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun GrowingGridCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, maxLines = 1)
        }
    }
}

@Composable
fun QuantityCard(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
                Text(text = label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            }
            Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun TimelineRow(day: Int, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Day\n$day", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 9.sp, lineHeight = 10.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(40.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )
        }

        Column(modifier = Modifier.padding(top = 4.dp)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(text = description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
