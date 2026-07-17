package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.sp
import com.example.data.CommunityPost
import com.example.ui.ChatMessage
import com.example.ui.PlantViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvisorTab(viewModel: PlantViewModel, modifier: Modifier = Modifier) {
    val chatHistory by viewModel.advisorChatHistory.collectAsState()
    val isChatLoading by viewModel.isLoadingChat.collectAsState()

    val climateLocation by viewModel.climateLocation.collectAsState()
    val climateAdvice by viewModel.climateAdvice.collectAsState()
    val isClimateLoading by viewModel.isLoadingClimate.collectAsState()

    val soilReport by viewModel.soilReport.collectAsState()
    val isSoilLoading by viewModel.isLoadingSoil.collectAsState()

    val selectedDesignStyle by viewModel.selectedDesignStyle.collectAsState()
    val gardenDesign by viewModel.gardenDesign.collectAsState()
    val isDesignLoading by viewModel.isLoadingDesign.collectAsState()

    val researchSynthesis by viewModel.researchSynthesis.collectAsState()
    val isResearchLoading by viewModel.isLoadingResearch.collectAsState()

    var activeSubSection by remember { mutableStateOf("CHAT") } // "CHAT", "CLIMATE", "SOIL", "DESIGN", "RESEARCH", "VARIETY", "COMMUNITY"
    var chatInput by remember { mutableStateOf("") }
    var locationInput by remember { mutableStateOf(climateLocation) }

    // Soil Inputs
    var phVal by remember { mutableStateOf(6.5) }
    var nVal by remember { mutableStateOf("Medium") }
    var pVal by remember { mutableStateOf("High") }
    var kVal by remember { mutableStateOf("Low") }

    // Variety Comparison Selection
    var selectedVarietyTab by remember { mutableStateOf("Tomato") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Advanced AI Features Row Navigation
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "FloraFarm AI Intelligence Suite",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Subsections tabs scrollable
                ScrollableTabRow(
                    selectedTabIndex = when (activeSubSection) {
                        "CHAT" -> 0
                        "CLIMATE" -> 1
                        "SOIL" -> 2
                        "DESIGN" -> 3
                        "RESEARCH" -> 4
                        "VARIETY" -> 5
                        else -> 6
                    },
                    edgePadding = 0.dp,
                    divider = {},
                    containerColor = Color.Transparent
                ) {
                    Tab(selected = activeSubSection == "CHAT", onClick = { activeSubSection = "CHAT" }, text = { Text("AI Expert Chat", fontWeight = FontWeight.Bold) })
                    Tab(selected = activeSubSection == "CLIMATE", onClick = { activeSubSection = "CLIMATE" }, text = { Text("Climate System", fontWeight = FontWeight.Bold) })
                    Tab(selected = activeSubSection == "SOIL", onClick = { activeSubSection = "SOIL" }, text = { Text("Soil Intelligence", fontWeight = FontWeight.Bold) })
                    Tab(selected = activeSubSection == "DESIGN", onClick = { activeSubSection = "DESIGN" }, text = { Text("Garden Designer", fontWeight = FontWeight.Bold) })
                    Tab(selected = activeSubSection == "RESEARCH", onClick = { activeSubSection = "RESEARCH" }, text = { Text("Research Synthesis", fontWeight = FontWeight.Bold) })
                    Tab(selected = activeSubSection == "VARIETY", onClick = { activeSubSection = "VARIETY" }, text = { Text("Variety Intel", fontWeight = FontWeight.Bold) })
                    Tab(selected = activeSubSection == "COMMUNITY", onClick = { activeSubSection = "COMMUNITY" }, text = { Text("Community Forum", fontWeight = FontWeight.Bold) })
                }
            }
        }

        // Display Active SubSection
        when (activeSubSection) {
            "CHAT" -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Expert Personalities Indicator
                            Text(
                                text = "Choose AI Consultant Expert Profile:",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                                    Text("AI Botanist", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                }
                                Box(modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(12.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                                    Text("AI Agricultural Scientist", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                }
                                Box(modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(12.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                                    Text("AI Climate Expert", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Chat Transcript (Last 4 messages)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 300.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                chatHistory.takeLast(6).forEach { msg ->
                                    val isAI = msg.sender == "AI"
                                    val bubbleBg = if (isAI) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                                   else MaterialTheme.colorScheme.primaryContainer
                                    val align = if (isAI) Alignment.Start else Alignment.End

                                    Column(modifier = Modifier.align(align)) {
                                        Box(
                                            modifier = Modifier
                                                .widthIn(max = 260.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(bubbleBg)
                                                .padding(12.dp)
                                        ) {
                                            Text(text = msg.text, style = MaterialTheme.typography.bodyMedium)
                                        }
                                        Text(
                                            text = if (isAI) "FloraFarm Botanical Expert" else "You",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(top = 2.dp, start = 4.dp)
                                        )
                                    }
                                }

                                if (isChatLoading) {
                                    Text("FloraFarm expert is typing...", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 4.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Custom Input field
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = chatInput,
                                    onValueChange = { chatInput = it },
                                    modifier = Modifier.weight(1f).testTag("advisor_chat_input"),
                                    placeholder = { Text("Ask about seeds, hydroponics, business yields...") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                IconButton(
                                    onClick = {
                                        viewModel.sendAdvisorMessage(chatInput)
                                        chatInput = ""
                                    },
                                    modifier = Modifier.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)).testTag("send_chat_button")
                                ) {
                                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            "CLIMATE" -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Climate Intelligence and Alert System",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            // Location input
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = locationInput,
                                    onValueChange = { locationInput = it },
                                    label = { Text("Enter GPS City/Country Location") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f).testTag("climate_location_input")
                                )
                                Button(
                                    onClick = {
                                        viewModel.updateClimateLocation(locationInput)
                                        viewModel.fetchClimateAdvice()
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.testTag("climate_sync_button")
                                ) {
                                    Text("Sync")
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            if (isClimateLoading) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary, modifier = Modifier.align(Alignment.CenterHorizontally))
                            } else if (climateAdvice != null) {
                                val advice = climateAdvice!!

                                // Live metrics
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    ClimateBox("Temp", advice.temperature, Icons.Default.DeviceThermostat, Modifier.weight(1f))
                                    ClimateBox("Humidity", advice.humidity, Icons.Default.WaterDrop, Modifier.weight(1f))
                                    ClimateBox("Rainfall", advice.rainfall, Icons.Default.CloudQueue, Modifier.weight(1f))
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Active Alert display if exists
                                advice.alertType?.let { alert ->
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)),
                                        border = CardDefaults.outlinedCardBorder()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Icon(Icons.Default.Warning, contentDescription = "Weather Alert", tint = MaterialTheme.colorScheme.error)
                                            Column {
                                                Text(text = "Active $alert Alert", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                                                Text(text = advice.alertMessage ?: "Risk detected", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                }

                                // Watering schedule advice
                                Text(text = "Today's Irrigation Advice", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                Text(text = advice.wateringRecommendation, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 4.dp))

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(text = "Optimal Fertilizer Timing", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                Text(text = advice.fertilizerTiming, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 4.dp))

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(text = "Seasonal Recommendations", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                Text(text = advice.seasonalTips, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                }
            }

            "SOIL" -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Soil Mineral & Health Intelligence",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            // Inputs
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Soil pH Level: $phVal", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                Slider(
                                    value = phVal.toFloat(),
                                    onValueChange = { phVal = (it * 10).roundToInt() / 10.0 },
                                    valueRange = 4.0f..9.0f,
                                    modifier = Modifier.width(180.dp).testTag("soil_ph_slider")
                                )
                            }

                            SoilDropdownSelector("Nitrogen (N)", nVal, listOf("Low", "Medium", "High")) { nVal = it }
                            SoilDropdownSelector("Phosphorus (P)", pVal, listOf("Low", "Medium", "High")) { pVal = it }
                            SoilDropdownSelector("Potassium (K)", kVal, listOf("Low", "Medium", "High")) { kVal = it }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    viewModel.updateSoilParams(phVal, nVal, pVal, kVal)
                                    viewModel.analyzeSoil()
                                },
                                modifier = Modifier.fillMaxWidth().testTag("analyze_soil_button"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Science, contentDescription = "Science")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Analyze Soil Chemistry")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            if (isSoilLoading) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary, modifier = Modifier.align(Alignment.CenterHorizontally))
                            } else if (soilReport != null) {
                                val rep = soilReport!!
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Organic Matter: ${rep.organicMatter}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                    Box(
                                        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)).padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("Health Score: ${rep.healthScore}/100", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Organic Amendments Recommendations:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                Text(text = rep.recommendations, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }

            "DESIGN" -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "AI Landscape & Garden Designer",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Describe or select your garden zone style to generate structural blueprints, pot arrangements, light patterns, and custom irrigation.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            Spacer(modifier = Modifier.height(12.dp))

                            var showStylesMenu by remember { mutableStateOf(false) }
                            val designStyles = listOf("Balcony Zen Retreat", "Urban Vertical Oasis", "Rooftop Permaculture", "Desert Xeriscaping")

                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = selectedDesignStyle,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier.fillMaxWidth().testTag("designer_style_selector").clickable { showStylesMenu = true },
                                    label = { Text("Landscape Design Vibe") },
                                    trailingIcon = {
                                        IconButton(onClick = { showStylesMenu = true }) {
                                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                                        }
                                    }
                                )
                                DropdownMenu(expanded = showStylesMenu, onDismissRequest = { showStylesMenu = false }) {
                                    designStyles.forEach { st ->
                                        DropdownMenuItem(text = { Text(st) }, onClick = {
                                            viewModel.updateDesignStyle(st)
                                            showStylesMenu = false
                                        })
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            if (isDesignLoading) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary, modifier = Modifier.align(Alignment.CenterHorizontally))
                            } else if (gardenDesign != null) {
                                val d = gardenDesign!!
                                DesignDetailRow("Structural Blueprint", d.layoutBlueprint)
                                DesignDetailRow("Proposed Botanical Lineup", d.proposedPlants.joinToString(", "))
                                DesignDetailRow("Pot Selection & Depth", d.potSelections)
                                DesignDetailRow("Artificial & Solar Lighting", d.lightingSetup)
                                DesignDetailRow("Watering Setup", d.wateringMethod)
                            }
                        }
                    }
                }
            }

            "RESEARCH" -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.School, contentDescription = "Academic", tint = MaterialTheme.colorScheme.primary)
                                Text(
                                    text = "AI Academic Research Mode",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Synthesizes high-level agronomy papers, clinical botanical studies, and scientific datasets into practical, action-based garden guidelines.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { viewModel.synthesizeResearch() },
                                modifier = Modifier.fillMaxWidth().testTag("synthesize_research_button"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Synthesize Latest Agronomy Paper")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            if (isResearchLoading) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary, modifier = Modifier.align(Alignment.CenterHorizontally))
                            } else if (researchSynthesis != null) {
                                val rs = researchSynthesis!!
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(text = "Paper: ${rs.paperTitle}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        Text(text = rs.authorsAndJournal, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(text = "Key Botanical Discovery:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                        Text(text = rs.keyDiscovery, style = MaterialTheme.typography.bodyMedium)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(text = "Actionable Farm Application:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                        Text(text = rs.practicalApplication, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "VARIETY" -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "AI Variety Intelligence",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            // Crop Variety selector tabs
                            TabRow(
                                selectedTabIndex = if (selectedVarietyTab == "Tomato") 0 else 1,
                                divider = {}
                            ) {
                                Tab(selected = selectedVarietyTab == "Tomato", onClick = { selectedVarietyTab = "Tomato" }, text = { Text("Tomato Varieties") })
                                Tab(selected = selectedVarietyTab == "Wheat", onClick = { selectedVarietyTab = "Wheat" }, text = { Text("Wheat Varieties") })
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            if (selectedVarietyTab == "Tomato") {
                                VarietyCompareItem("Roma Heirloom", "9,500 kg/acre", "Acidic, fleshy", "High (drought-tolerating)", "Moderate (rust resistance)", "High ($0.45/kg)")
                                VarietyCompareItem("Beefsteak", "14,000 kg/acre", "Deep, juicy, slicing", "Moderate (requires high shade)", "Low (susceptible to wilt)", "Premium ($0.75/kg)")
                                VarietyCompareItem("Cherry Tomato", "6,000 kg/acre", "Intensely sweet", "High (very resilient)", "High (blight resistant)", "Very Premium ($1.20/kg)")
                            } else {
                                VarietyCompareItem("Durum Wheat", "3,200 kg/acre", "Nutty, high-gluten protein", "Moderate (prefers winter rain)", "Moderate (rust vulnerable)", "High ($350/ton)")
                                VarietyCompareItem("Hard Red Winter", "4,100 kg/acre", "Classic earthy, baking bread", "High (frost resilient)", "High", "Steady ($280/ton)")
                                VarietyCompareItem("Soft White Wheat", "5,500 kg/acre", "Mild, pastry flour baking", "Low (needs rich silt moisture)", "Low", "Premium ($320/ton)")
                            }
                        }
                    }
                }
            }

            else -> { // COMMUNITY
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Global Growers Community",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            CommunityPostCard("Dr. Botanica", "Botanist", "2h ago", "Mycorrhizal fungi inoculants have boosted my balcony roma tomatoes roots by nearly 200%. Highly recommended for small containers!", 42, "Indoor Groups")
                            CommunityPostCard("Farmer John", "Commercial Farmer", "5h ago", "Wheat jointing stage is in full effect across Punjab. Adding secondary light urea tilling before the morning dew. Share your water schedules!", 128, "Cereal Crops")
                            CommunityPostCard("Zoe Leaf", "Home Gardener", "1d ago", "Can I grow tropical Monstera on an east-facing balcony in Paris during winter? Should I move it indoors near artificial LEDs?", 19, "Monstera Lovers")
                        }
                    }
                }
            }
        }

        // Planting Map Intelligence (Always visible in bottom of Advisor tab!)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Map, contentDescription = "Map", tint = MaterialTheme.colorScheme.primary)
                        Text(
                            text = "AI Plant Distribution & Suitability Map",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Global satellite mapping overlay. Coordinates show Punjab region suitable for high-yield Wheat, and Southern European zones ideal for Lavender.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun ClimateBox(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun SoilDropdownSelector(label: String, selected: String, options: List<String>, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth().clickable { expanded = true },
            label = { Text(label) },
            trailingIcon = { IconButton(onClick = { expanded = true }) { Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown") } }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { opt ->
                DropdownMenuItem(text = { Text(opt) }, onClick = {
                    onSelect(opt)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun DesignDetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 2.dp))
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    }
}

@Composable
fun VarietyCompareItem(
    name: String,
    yield: String,
    taste: String,
    climate: String,
    disease: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(6.dp))
            VarietyStat("Expected Yield", yield)
            VarietyStat("Taste/Flavor Vibe", taste)
            VarietyStat("Climate Tolerance", climate)
            VarietyStat("Disease Immunity", disease)
            VarietyStat("Avg Market Value", value)
        }
    }
}

@Composable
fun VarietyStat(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun CommunityPostCard(
    author: String,
    role: String,
    time: String,
    content: String,
    likes: Int,
    group: String
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
                        Text(text = author.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text(text = author, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        Text(text = "$role • $time", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Box(modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                    Text(text = group, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = content, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.ThumbUp, contentDescription = "Like", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Text(text = "$likes likes", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Comment, contentDescription = "Reply", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                    Text(text = "Reply", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
