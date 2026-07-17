package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.PlantDataset
import com.example.ui.PlantViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorsTab(viewModel: PlantViewModel, modifier: Modifier = Modifier) {
    val selectedCrop by viewModel.selectedCalculatorCrop.collectAsState()
    val areaSize by viewModel.farmingAreaSize.collectAsState()
    val farmingReport by viewModel.farmingReport.collectAsState()
    val isLoading by viewModel.isLoadingFarmingReport.collectAsState()

    var showCropMenu by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Welcome Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Default.Calculate,
                        contentDescription = "Calculator Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                    Column {
                        Text(
                            text = "Smart Farming & Cost Calculators",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Accurate planning tool. Dynamically calculates water, seeds, costs, and profit projections based on acreage size and crop varieties.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Input selectors
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Configure Farming Model",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Crop dropdown selector
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = selectedCrop.commonName,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("calculator_crop_selector")
                                .clickable { showCropMenu = true },
                            label = { Text("Selected Crop Variety") },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showCropMenu = true }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                                }
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        DropdownMenu(
                            expanded = showCropMenu,
                            onDismissRequest = { showCropMenu = false }
                        ) {
                            PlantDataset.plants.forEach { plant ->
                                DropdownMenuItem(
                                    text = { Text("${plant.commonName} (${plant.scientificName})") },
                                    onClick = {
                                        viewModel.selectCalculatorCrop(plant)
                                        showCropMenu = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Acreage size slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Land Coverage", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = String.format("%.1f Acre(s)", areaSize),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Slider(
                        value = areaSize.toFloat(),
                        onValueChange = { viewModel.updateFarmingAreaSize(it.toDouble()) },
                        valueRange = 0.1f..10.0f,
                        steps = 99,
                        modifier = Modifier.testTag("calculator_area_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("0.1 Acre", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("10.0 Acres", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Live calculator results (Dynamic based on inputs)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(20.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "2. Quick Quantity Estimates",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Calculate variables based on acreage
                    val calculatedSeeds = (areaSize * 120).roundToInt()
                    val calculatedWater = (areaSize * 450000).roundToInt()
                    val calculatedNPK = (areaSize * 50).roundToInt()
                    val calculatedProductionCost = (areaSize * 950).roundToInt()
                    val calculatedYield = (areaSize * 14500).roundToInt()
                    val calculatedProfit = (areaSize * 3600).roundToInt()

                    CalculatorStatRow("Estimated Seed Quantity", "$calculatedSeeds grams", Icons.Default.FilterVintage)
                    CalculatorStatRow("Water Lifeline Needed", "$calculatedWater Litres", Icons.Default.WaterDrop)
                    CalculatorStatRow("NPK Mineral Nutrients", "$calculatedNPK kg", Icons.Default.Grass)
                    CalculatorStatRow("Target Production Cost", "$$calculatedProductionCost USD", Icons.Default.Paid)
                    CalculatorStatRow("Projected Harvest Yield", "$calculatedYield kg", Icons.Default.Agriculture)

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.TrendingUp, contentDescription = "Profit", tint = MaterialTheme.colorScheme.primary)
                            Text("Estimated Net Profit", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            "$$calculatedProfit USD",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // AI Strategy Generator
        item {
            Button(
                onClick = { viewModel.generateFarmingReport() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("generate_farming_strategy_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "AI")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generate AI Commercial Strategy")
            }
        }

        // AI Business Consultation Results
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Drafting comprehensive agricultural economics forecast...", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                    }
                } else if (farmingReport != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                            ) {
                                Icon(Icons.Default.BusinessCenter, contentDescription = "Strategy", tint = MaterialTheme.colorScheme.primary)
                                Text(
                                    text = "AI Commercial Farming Intelligence",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            FarmingReportDetail("Land Preparation", farmingReport!!.landPrep)
                            FarmingReportDetail("Crop Sowing Scheme", farmingReport!!.cropPlanning)
                            FarmingReportDetail("Drip Machinery", farmingReport!!.machineryRecommendation)
                            FarmingReportDetail("Labor Estimation", farmingReport!!.laborEstimation)
                            FarmingReportDetail("Market Demand Report", farmingReport!!.marketDemand)
                            FarmingReportDetail("Export Opportunities", farmingReport!!.exportOpportunities)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorStatRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
            Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun FarmingReportDetail(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    }
}
