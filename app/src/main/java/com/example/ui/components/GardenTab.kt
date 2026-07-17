package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.database.GardenPlant
import com.example.ui.PlantViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GardenTab(viewModel: PlantViewModel, modifier: Modifier = Modifier) {
    val plants by viewModel.allPlants.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Tracker Summary header
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "My Personal AI Garden",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Track daily growth, manage customized schedules, log soil actions, and schedule offline water reminders.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = { showAddDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("add_to_garden_fab")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add")
                    }
                }
            }
        }

        if (plants.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Spa, contentDescription = "Empty", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Your garden is empty", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Click 'Add' above to start tracking your local crops!", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // List of items in Garden
        items(plants) { plant ->
            GardenPlantRow(
                plant = plant,
                onWater = { viewModel.waterPlant(plant) },
                onDelete = { viewModel.deletePlantFromGarden(plant) }
            )
        }
    }

    // Add Crop Dialog Form
    if (showAddDialog) {
        AddPlantDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, scientific, variety, interval, notes ->
                viewModel.addPlantToGarden(name, scientific, variety, interval, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun GardenPlantRow(
    plant: GardenPlant,
    onWater: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val addedStr = dateFormat.format(Date(plant.addedDate))
    val lastWateredStr = dateFormat.format(Date(plant.lastWateredDate))

    // Calculate days since watered to warn if it needs hydration
    val diffMs = System.currentTimeMillis() - plant.lastWateredDate
    val diffDays = (diffMs / (1000 * 60 * 60 * 24)).toInt()
    val needsWater = diffDays >= plant.wateringIntervalDays
    val statusText = if (needsWater) "Needs Water" else "Healthy"
    val statusColor = if (needsWater) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .testTag("garden_plant_${plant.name.lowercase().replace(" ", "_")}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(statusColor.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocalFlorist, contentDescription = "Plant", tint = statusColor)
                    }
                    Column {
                        Text(text = plant.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(text = plant.scientificName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Box(
                    modifier = Modifier
                        .background(statusColor.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            // Quick stats visible always
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Watering Loop: Every ${plant.wateringIntervalDays} days", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = "Last Watered: $lastWateredStr", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            // Expanded details
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(12.dp))

                if (plant.notes.isNotEmpty()) {
                    Text(text = "Grower Log / Notes", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                    Text(text = plant.notes, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 4.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(text = "Date Introduced: $addedStr", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onWater() },
                        modifier = Modifier.weight(1f).testTag("water_plant_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.WaterDrop, contentDescription = "Water")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Water Crop")
                    }
                    OutlinedButton(
                        onClick = { onDelete() },
                        modifier = Modifier.weight(1f).testTag("delete_plant_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Remove")
                    }
                }
            }
        }
    }
}

@Composable
fun AddPlantDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, scientificName: String, variety: String, interval: Int, notes: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var scientific by remember { mutableStateOf("") }
    var variety by remember { mutableStateOf("") }
    var intervalStr by remember { mutableStateOf("3") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Introduce Crop to Garden") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Crop Custom Name") },
                    placeholder = { Text("e.g. My Balcony Tomato") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_plant_name_input")
                )
                OutlinedTextField(
                    value = scientific,
                    onValueChange = { scientific = it },
                    label = { Text("Botanical Scientific Name") },
                    placeholder = { Text("e.g. Solanum lycopersicum") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_plant_scientific_input")
                )
                OutlinedTextField(
                    value = variety,
                    onValueChange = { variety = it },
                    label = { Text("Variety / Breed") },
                    placeholder = { Text("e.g. Roma Red Heirloom") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_plant_variety_input")
                )
                OutlinedTextField(
                    value = intervalStr,
                    onValueChange = { intervalStr = it },
                    label = { Text("Watering Interval (Days)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_plant_interval_input")
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Grower Notes") },
                    placeholder = { Text("Add soil status, solar direction, pot dimensions...") },
                    modifier = Modifier.fillMaxWidth().testTag("add_plant_notes_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotEmpty()) {
                        val interval = intervalStr.toIntOrNull() ?: 3
                        onConfirm(name, scientific, variety, interval, notes)
                    }
                },
                modifier = Modifier.testTag("dialog_confirm_add")
            ) {
                Text("Introduce")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}
