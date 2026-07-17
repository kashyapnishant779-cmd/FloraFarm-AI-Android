package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.PlantViewModel
import com.example.ui.components.AdvisorTab
import com.example.ui.components.CalculatorsTab
import com.example.ui.components.DoctorTab
import com.example.ui.components.EncyclopediaTab
import com.example.ui.components.GardenTab
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: PlantViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                var currentTab by remember { mutableStateOf("ENCYCLOPEDIA") } // ENCYCLOPEDIA, DOCTOR, GARDEN, CALCULATOR, ADVISOR

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Spa,
                                        contentDescription = "App Logo",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "FloraFarm AI",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            },
                            actions = {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primaryContainer,
                                            shape = MaterialTheme.shapes.extraSmall
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "AI SYNCED",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontSize = 9.sp
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                titleContentColor = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier
                                .testTag("bottom_nav_bar")
                                .windowInsetsPadding(WindowInsets.navigationBars),
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 8.dp
                        ) {
                            NavigationBarItem(
                                selected = currentTab == "ENCYCLOPEDIA",
                                onClick = { currentTab = "ENCYCLOPEDIA" },
                                icon = {
                                    Icon(
                                        imageVector = if (currentTab == "ENCYCLOPEDIA") Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                                        contentDescription = "Encyclopedia"
                                    )
                                },
                                label = { Text("Database", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.testTag("nav_item_encyclopedia")
                            )

                            NavigationBarItem(
                                selected = currentTab == "DOCTOR",
                                onClick = { currentTab = "DOCTOR" },
                                icon = {
                                    Icon(
                                        imageVector = if (currentTab == "DOCTOR") Icons.Filled.Healing else Icons.Outlined.Healing,
                                        contentDescription = "AI Doctor"
                                    )
                                },
                                label = { Text("AI Doctor", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.testTag("nav_item_doctor")
                            )

                            NavigationBarItem(
                                selected = currentTab == "GARDEN",
                                onClick = { currentTab = "GARDEN" },
                                icon = {
                                    Icon(
                                        imageVector = if (currentTab == "GARDEN") Icons.Filled.Spa else Icons.Outlined.Spa,
                                        contentDescription = "My Garden"
                                    )
                                },
                                label = { Text("My Garden", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.testTag("nav_item_garden")
                            )

                            NavigationBarItem(
                                selected = currentTab == "CALCULATOR",
                                onClick = { currentTab = "CALCULATOR" },
                                icon = {
                                    Icon(
                                        imageVector = if (currentTab == "CALCULATOR") Icons.Filled.Calculate else Icons.Outlined.Calculate,
                                        contentDescription = "Calculators"
                                    )
                                },
                                label = { Text("Calculators", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.testTag("nav_item_calculator")
                            )

                            NavigationBarItem(
                                selected = currentTab == "ADVISOR",
                                onClick = { currentTab = "ADVISOR" },
                                icon = {
                                    Icon(
                                        imageVector = if (currentTab == "ADVISOR") Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                                        contentDescription = "AI Suite"
                                    )
                                },
                                label = { Text("AI Suite", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.testTag("nav_item_advisor")
                            )
                        }
                    },
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        AnimatedContent(
                            targetState = currentTab,
                            transitionSpec = {
                                (fadeIn() + scaleIn(initialScale = 0.98f))
                                    .togetherWith(fadeOut())
                            },
                            label = "TabTransition"
                        ) { targetTab ->
                            when (targetTab) {
                                "ENCYCLOPEDIA" -> EncyclopediaTab(viewModel = viewModel)
                                "DOCTOR" -> DoctorTab(viewModel = viewModel)
                                "GARDEN" -> GardenTab(viewModel = viewModel)
                                "CALCULATOR" -> CalculatorsTab(viewModel = viewModel)
                                "ADVISOR" -> AdvisorTab(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
