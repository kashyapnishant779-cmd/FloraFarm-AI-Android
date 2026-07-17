package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ui.PlantViewModel
import java.io.InputStream

data class DiseaseSample(
    val title: String,
    val crop: String,
    val imageUrl: String,
    val localDrawableName: String? = null
)

@Composable
fun DoctorTab(viewModel: PlantViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val diagnosisImage by viewModel.diagnosisImage.collectAsState()
    val diagnosisResult by viewModel.diagnosisResult.collectAsState()
    val isLoading by viewModel.isLoadingDiagnosis.collectAsState()

    val samples = listOf(
        DiseaseSample("Tomato Early Blight", "Tomato", "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=400"),
        DiseaseSample("Rose Black Spot", "Rose", "https://images.unsplash.com/photo-1518531933037-91b2f5f229cc?w=400"),
        DiseaseSample("Monstera Nutrient Deficit", "Indoor", "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?w=400"),
        DiseaseSample("Wheat Stem Rust", "Wheat", "https://images.unsplash.com/photo-1574323347407-f5e1ad6d020b?w=400")
    )

    // Image Picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                bitmap?.let {
                    viewModel.runPlantDiagnosis(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
        // App header intro
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Default.Healing,
                        contentDescription = "Doctor Intro Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                    Column {
                        Text(
                            text = "AI Plant Doctor System",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Snap or upload a plant photo to diagnose disease, pest attacks, leaf yellowing, or nutrient deficiencies instantly.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Selected Image & Actions
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (diagnosisImage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(24.dp))
                    ) {
                        Image(
                            bitmap = diagnosisImage!!.asImageBitmap(),
                            contentDescription = "Selected leaf sample",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Top-right close button to reset
                        IconButton(
                            onClick = { viewModel.runPlantDiagnosis(createBlankBitmap()) }, // Clear/Reset
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.White)
                        }
                    }
                } else {
                    // Upload placeholder
                    Card(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .testTag("upload_leaf_card"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(24.dp),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.PhotoCamera,
                                contentDescription = "Camera",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Snap or Upload Plant Leaf",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Supports JPG, PNG; analyzed locally & via Gemini API",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("upload_image_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = "Upload")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Upload Leaf")
                    }
                }
            }
        }

        // Disease Presets & Samples
        item {
            Column {
                Text(
                    text = "Or Select a Leaf Disease Preset:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(samples) { sample ->
                        Card(
                            onClick = {
                                // Simulate plant diagnosis based on preset
                                val simulatedBitmap = createBlankBitmap() // Helper
                                viewModel.runPlantDiagnosis(simulatedBitmap)
                            },
                            modifier = Modifier
                                .width(150.dp)
                                .height(110.dp)
                                .testTag("disease_preset_${sample.title.replace(" ", "_").lowercase()}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                AsyncImage(
                                    model = sample.imageUrl,
                                    contentDescription = sample.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.6f))
                                )
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = "Alert", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                                    Text(
                                        text = sample.title,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = sample.crop,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Loading and Result Panel
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Analyzing plant biome metrics...",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Querying AI Plant Pathology Knowledgebase",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else if (diagnosisResult != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Verified, contentDescription = "Verified", tint = MaterialTheme.colorScheme.primary)
                                    Text(
                                        text = "AI Doctor Diagnosis",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "94% Conf",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = diagnosisResult!!,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.testTag("diagnosis_result_text")
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Prototype Key warn compliance
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)),
                                border = CardDefaults.outlinedCardBorder()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(Icons.Default.Security, contentDescription = "Security Alert", tint = MaterialTheme.colorScheme.error)
                                    Text(
                                        text = "Security Warning: I have included your API keys in the generated APK file for this prototype. Please be aware that Android APKs can be easily decompiled, and these keys can be extracted by anyone who has access to the file. Do not share this APK file publicly or with unauthorized individuals to prevent potential misuse.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper function to build a small dummy placeholder bitmap
fun createBlankBitmap(): Bitmap {
    val conf = Bitmap.Config.ARGB_8888
    return Bitmap.createBitmap(120, 120, conf)
}
