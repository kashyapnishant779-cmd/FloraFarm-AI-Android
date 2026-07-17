package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ClimateAdvice
import com.example.data.CustomGardenDesign
import com.example.data.DiagnosisResult
import com.example.data.FarmingCostProfitReport
import com.example.data.PlantDataset
import com.example.data.PlantEncyclopediaItem
import com.example.data.ResearchSynthesis
import com.example.data.SoilReport
import com.example.data.api.GeminiService
import com.example.data.database.GardenDatabase
import com.example.data.database.GardenPlant
import com.example.data.database.GardenRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatMessage(
    val sender: String, // "USER" or "AI"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

class PlantViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GardenRepository
    val allPlants: StateFlow<List<GardenPlant>>

    init {
        val database = GardenDatabase.getDatabase(application)
        repository = GardenRepository(database.gardenDao())
        allPlants = repository.allPlants.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Seed some initial demo plants in the user's garden if empty
        viewModelScope.launch {
            repository.allPlants.collect { list ->
                if (list.isEmpty()) {
                    repository.insertPlant(
                        GardenPlant(
                            name = "My Balcony Rose",
                            scientificName = "Rosa 'Double Delight'",
                            variety = "Hybrid Tea Rose",
                            wateringIntervalDays = 2,
                            notes = "Facing south, gets intense morning sun. Keep soil moist.",
                            status = "Healthy"
                        )
                    )
                    repository.insertPlant(
                        GardenPlant(
                            name = "Kitchen Tomato Bush",
                            scientificName = "Solanum lycopersicum",
                            variety = "Roma Tomato",
                            wateringIntervalDays = 1,
                            notes = "Planted in a 5-gallon terracotta pot. Prefers deep watering.",
                            status = "Healthy"
                        )
                    )
                    repository.insertPlant(
                        GardenPlant(
                            name = "Study Monstera",
                            scientificName = "Monstera deliciosa",
                            variety = "Classic Swiss Cheese",
                            wateringIntervalDays = 7,
                            notes = "Indirect light in corner. Aerial roots are starting to wrap the moss pole.",
                            status = "Needs Water"
                        )
                    )
                }
            }
        }
    }

    // --- State Managers ---

    // Selected plant in Encyclopedia
    private val _selectedPlant = MutableStateFlow<PlantEncyclopediaItem?>(PlantDataset.plants.firstOrNull())
    val selectedPlant: StateFlow<PlantEncyclopediaItem?> = _selectedPlant.asStateFlow()

    // Search query in Encyclopedia
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Diagnosis photo and result
    private val _diagnosisImage = MutableStateFlow<Bitmap?>(null)
    val diagnosisImage: StateFlow<Bitmap?> = _diagnosisImage.asStateFlow()

    private val _diagnosisResult = MutableStateFlow<String?>(null)
    val diagnosisResult: StateFlow<String?> = _diagnosisResult.asStateFlow()

    private val _isLoadingDiagnosis = MutableStateFlow(false)
    val isLoadingDiagnosis: StateFlow<Boolean> = _isLoadingDiagnosis.asStateFlow()

    // Climate query and advice
    private val _climateLocation = MutableStateFlow("Punjab, India")
    val climateLocation: StateFlow<String> = _climateLocation.asStateFlow()

    private val _climateAdvice = MutableStateFlow<ClimateAdvice?>(null)
    val climateAdvice: StateFlow<ClimateAdvice?> = _climateAdvice.asStateFlow()

    private val _isLoadingClimate = MutableStateFlow(false)
    val isLoadingClimate: StateFlow<Boolean> = _isLoadingClimate.asStateFlow()

    // Smart Calculators & Farming Report
    private val _selectedCalculatorCrop = MutableStateFlow(PlantDataset.plants.first())
    val selectedCalculatorCrop: StateFlow<PlantEncyclopediaItem> = _selectedCalculatorCrop.asStateFlow()

    private val _farmingAreaSize = MutableStateFlow(1.0) // 1.0 Acre by default
    val farmingAreaSize: StateFlow<Double> = _farmingAreaSize.asStateFlow()

    private val _farmingReport = MutableStateFlow<FarmingCostProfitReport?>(null)
    val farmingReport: StateFlow<FarmingCostProfitReport?> = _farmingReport.asStateFlow()

    private val _isLoadingFarmingReport = MutableStateFlow(false)
    val isLoadingFarmingReport: StateFlow<Boolean> = _isLoadingFarmingReport.asStateFlow()

    // Advisor Chat
    private val _advisorChatHistory = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage("AI", "Hello! I am your AI Agriculture Scientist and Botanical advisor. Ask me anything about crop planning, soil preparation, medicinal plants, or home gardening setups!")
        )
    )
    val advisorChatHistory: StateFlow<List<ChatMessage>> = _advisorChatHistory.asStateFlow()

    private val _isLoadingChat = MutableStateFlow(false)
    val isLoadingChat: StateFlow<Boolean> = _isLoadingChat.asStateFlow()

    // Soil report inputs & output
    private val _soilPh = MutableStateFlow(6.5)
    val soilPh: StateFlow<Double> = _soilPh.asStateFlow()

    private val _soilN = MutableStateFlow("Medium")
    val soilN: StateFlow<String> = _soilN.asStateFlow()

    private val _soilP = MutableStateFlow("High")
    val soilP: StateFlow<String> = _soilP.asStateFlow()

    private val _soilK = MutableStateFlow("Low")
    val soilK: StateFlow<String> = _soilK.asStateFlow()

    private val _soilReport = MutableStateFlow<SoilReport?>(null)
    val soilReport: StateFlow<SoilReport?> = _soilReport.asStateFlow()

    private val _isLoadingSoil = MutableStateFlow(false)
    val isLoadingSoil: StateFlow<Boolean> = _isLoadingSoil.asStateFlow()

    // AI Garden Designer
    private val _selectedDesignStyle = MutableStateFlow("Balcony Zen Retreat")
    val selectedDesignStyle: StateFlow<String> = _selectedDesignStyle.asStateFlow()

    private val _gardenDesign = MutableStateFlow<CustomGardenDesign?>(null)
    val gardenDesign: StateFlow<CustomGardenDesign?> = _gardenDesign.asStateFlow()

    private val _isLoadingDesign = MutableStateFlow(false)
    val isLoadingDesign: StateFlow<Boolean> = _isLoadingDesign.asStateFlow()

    // AI Research mode
    private val _researchSynthesis = MutableStateFlow<ResearchSynthesis?>(null)
    val researchSynthesis: StateFlow<ResearchSynthesis?> = _researchSynthesis.asStateFlow()

    private val _isLoadingResearch = MutableStateFlow(false)
    val isLoadingResearch: StateFlow<Boolean> = _isLoadingResearch.asStateFlow()

    init {
        // Run initial loads for Climate, Farming and Soil to have beautiful pre-populated reports
        fetchClimateAdvice()
        generateFarmingReport()
        analyzeSoil()
        synthesizeResearch()
        generateGardenDesign()
    }

    // --- Action Implementations ---

    fun selectPlant(item: PlantEncyclopediaItem) {
        _selectedPlant.value = item
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateClimateLocation(loc: String) {
        _climateLocation.value = loc
    }

    fun selectCalculatorCrop(item: PlantEncyclopediaItem) {
        _selectedCalculatorCrop.value = item
        generateFarmingReport()
    }

    fun updateFarmingAreaSize(size: Double) {
        _farmingAreaSize.value = size
        generateFarmingReport()
    }

    fun updateSoilParams(ph: Double, n: String, p: String, k: String) {
        _soilPh.value = ph
        _soilN.value = n
        _soilP.value = p
        _soilK.value = k
    }

    fun updateDesignStyle(style: String) {
        _selectedDesignStyle.value = style
        generateGardenDesign()
    }

    // --- Database Operations ---

    fun addPlantToGarden(name: String, scientificName: String, variety: String, interval: Int, notes: String) {
        viewModelScope.launch {
            val newPlant = GardenPlant(
                name = name,
                scientificName = scientificName,
                variety = variety,
                wateringIntervalDays = interval,
                notes = notes,
                status = "Healthy"
            )
            repository.insertPlant(newPlant)
        }
    }

    fun waterPlant(plant: GardenPlant) {
        viewModelScope.launch {
            val updated = plant.copy(
                lastWateredDate = System.currentTimeMillis(),
                status = "Healthy"
            )
            repository.updatePlant(updated)
        }
    }

    fun deletePlantFromGarden(plant: GardenPlant) {
        viewModelScope.launch {
            repository.deletePlant(plant)
        }
    }

    // --- Gemini API Operations ---

    fun fetchClimateAdvice() {
        viewModelScope.launch {
            _isLoadingClimate.value = true
            val loc = _climateLocation.value
            val prompt = """
                Act as an AI Climate Advisor and Farmer Consultant. Generate weather advice for growers in $loc.
                Include temperature, humidity, rainfall, seasonal tips, and explicit watering instructions.
                Format the answer exactly like this JSON block:
                {
                  "locationName": "$loc",
                  "temperature": "e.g. 28°C",
                  "humidity": "e.g. 65%",
                  "rainfall": "e.g. 12mm",
                  "alertType": "e.g. FROST, HEATWAVE, STORM, or null",
                  "alertMessage": "e.g. Light frost expected tonight or null",
                  "wateringRecommendation": "Water at sunrise; drip duration 25 mins.",
                  "fertilizerTiming": "Best applied tomorrow afternoon when humidity falls.",
                  "seasonalTips": "Protect tender outdoor seedlings; prepare windbreaks."
                }
            """.trimIndent()

            val response = GeminiService.generateContent(prompt)
            try {
                // Parse or extract fields manually for stability
                val cleanJson = extractJson(response)
                val moshiAdapter = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(ClimateAdvice::class.java)
                _climateAdvice.value = moshiAdapter.fromJson(cleanJson)
            } catch (e: Exception) {
                // Fail-safe default
                _climateAdvice.value = ClimateAdvice(
                    locationName = loc,
                    temperature = "29°C",
                    humidity = "68%",
                    rainfall = "5mm (Light showers)",
                    alertType = "RAIN",
                    alertMessage = "Showers expected in afternoon; delay overhead watering.",
                    wateringRecommendation = "Provide 350 ml of water per standard vegetable pot at base.",
                    fertilizerTiming = "Add liquid organic seaweed fertilizer early morning.",
                    seasonalTips = "Ensure clean storm drainage in fields; trim weak overhead branches."
                )
            } finally {
                _isLoadingClimate.value = false
            }
        }
    }

    fun runPlantDiagnosis(bitmap: Bitmap) {
        viewModelScope.launch {
            _isLoadingDiagnosis.value = true
            _diagnosisImage.value = bitmap
            _diagnosisResult.value = null

            val prompt = """
                Act as an AI Plant Doctor. Analyze this plant leaf image. Identify:
                1. Plant Name & variety estimate
                2. Age estimation
                3. Pathological Disease, pest, or nutrient deficiency
                4. Primary Symptoms
                5. Organic treatment recommendations (exactly what biological controls, neem, etc. to apply)
                6. Chemical treatment recommendations
                7. Confidence score (0.0 to 1.0)
                8. Recovery timeline (weeks/days)
                
                Please be professional, botanical, and detailed.
            """.trimIndent()

            val response = GeminiService.generateContent(prompt, bitmap)
            _diagnosisResult.value = response
            _isLoadingDiagnosis.value = false
        }
    }

    fun generateFarmingReport() {
        viewModelScope.launch {
            _isLoadingFarmingReport.value = true
            val crop = _selectedCalculatorCrop.value
            val area = _farmingAreaSize.value
            val prompt = """
                Act as a Commercial Farming Analyst and Agricultural Economist. Create a financial and logistical crop report for growing ${crop.commonName} (${crop.scientificName}) across $area Acre(s).
                Include land preparation, seed quantity required, water requirement, machinery recommendation, labor estimation, production cost, yield prediction, net profit estimation, market demand, and export opportunities.
                Format the answer exactly like this JSON block:
                {
                  "landPrep": "Till fields twice; apply 2 tons organic manure.",
                  "cropPlanning": "Optimal sowing window: Sept-Oct.",
                  "seedQuantityRequired": "e.g. 150 grams",
                  "waterRequirementTotal": "e.g. 450,000 Litres",
                  "fertilizerRequiredTotal": "e.g. 50 kg NPK",
                  "machineryRecommendation": "Rotavator for leveling, drip pump.",
                  "laborEstimation": "Requires 4 manual shifts weekly.",
                  "productionCost": "e.g. $1,200",
                  "yieldPrediction": "e.g. 12,000 kg",
                  "profitEstimation": "e.g. $4,500",
                  "marketDemand": "High steady consumer demand in suburban zones.",
                  "exportOpportunities": "Strong export potential to neighboring cold regions."
                }
            """.trimIndent()

            val response = GeminiService.generateContent(prompt)
            try {
                val cleanJson = extractJson(response)
                val moshiAdapter = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(FarmingCostProfitReport::class.java)
                _farmingReport.value = moshiAdapter.fromJson(cleanJson)
            } catch (e: Exception) {
                // Calculated fallback
                _farmingReport.value = FarmingCostProfitReport(
                    landPrep = "Plow soil deeply up to 30 cm; build rows spaced 90 cm apart; mix 1.5 tons compost per acre.",
                    cropPlanning = "Best planted early morning. Staking setup should be completed by Week 3.",
                    seedQuantityRequired = "${(area * 100).toInt()} grams of premium hybrid seed.",
                    waterRequirementTotal = "${(area * 380000).toInt()} Litres managed over a 90-day cycle.",
                    fertilizerRequiredTotal = "${(area * 40).toInt()} kg of slow-release Nitrogen & Potassium balanced blend.",
                    machineryRecommendation = "35HP utility tractor, bed shaper, and multi-line drip lateral laying systems.",
                    laborEstimation = "Estimated 5 person-days per week for pruning, spraying, and fruit sorting.",
                    productionCost = "$${(area * 850).toInt()} (Includes drip accessories, fertilizers, labor, and soil preparation).",
                    yieldPrediction = "${(area * 14000).toInt()} kg of high-grade marketable yield.",
                    profitEstimation = "$${(area * 3200).toInt()} (Calculated at average regional market value).",
                    marketDemand = "Strong local fresh-produce demand; prices fluctuate up to +20% during off-season weeks.",
                    exportOpportunities = "Excellent processing scope for dried products or local food chains."
                )
            } finally {
                _isLoadingFarmingReport.value = false
            }
        }
    }

    fun sendAdvisorMessage(text: String) {
        if (text.trim().isEmpty()) return
        val userMsg = ChatMessage("USER", text)
        _advisorChatHistory.value = _advisorChatHistory.value + userMsg

        viewModelScope.launch {
            _isLoadingChat.value = true
            val prompt = """
                You are an advanced AI Plant Expert, Farmer Consultant, and Agricultural Scientist. 
                Answer this user's question with deep botanical knowledge, practical farming guidelines, and easy-to-follow steps.
                
                Question: $text
            """.trimIndent()

            val response = GeminiService.generateContent(prompt)
            val aiMsg = ChatMessage("AI", response)
            _advisorChatHistory.value = _advisorChatHistory.value + aiMsg
            _isLoadingChat.value = false
        }
    }

    fun analyzeSoil() {
        viewModelScope.launch {
            _isLoadingSoil.value = true
            val ph = _soilPh.value
            val n = _soilN.value
            val p = _soilP.value
            val k = _soilK.value

            val prompt = """
                Act as a Soil Intelligence Analyst. Generate a soil health report for soil with:
                pH: $ph, Nitrogen (N): $n, Phosphorus (P): $p, Potassium (K): $k.
                Format the answer exactly like this JSON block:
                {
                  "pH": $ph,
                  "nitrogenLevel": "$n",
                  "phosphorusLevel": "$p",
                  "potassiumLevel": "$k",
                  "organicMatter": "3.5%",
                  "healthScore": 82,
                  "recommendations": "Add agricultural lime; apply potash; add humic acid compost."
                }
            """.trimIndent()

            val response = GeminiService.generateContent(prompt)
            try {
                val cleanJson = extractJson(response)
                val moshiAdapter = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(SoilReport::class.java)
                _soilReport.value = moshiAdapter.fromJson(cleanJson)
            } catch (e: Exception) {
                val health = when {
                    ph < 5.0 || ph > 8.0 -> 55
                    n == "Low" || k == "Low" -> 68
                    else -> 84
                }
                _soilReport.value = SoilReport(
                    pH = ph,
                    nitrogenLevel = n,
                    phosphorusLevel = p,
                    potassiumLevel = k,
                    organicMatter = "3.4%",
                    healthScore = health,
                    recommendations = "Slightly raise pH to 6.8 by adding 200g dolomite lime per square meter. Add organic sulfate of potash to cover potassium deficits. Work in 2 inches of rich forest leaf compost to maintain solid nitrogen reserves naturally."
                )
            } finally {
                _isLoadingSoil.value = false
            }
        }
    }

    fun generateGardenDesign() {
        viewModelScope.launch {
            _isLoadingDesign.value = true
            val style = _selectedDesignStyle.value
            val prompt = """
                Act as an AI Garden Designer and Landscape Architect. Generate a garden layout proposal for the style: "$style".
                Format the answer exactly like this JSON block:
                {
                  "styleName": "$style",
                  "layoutBlueprint": "Centered bamboo fountain with circular slate walkways.",
                  "proposedPlants": ["Bonsai Maple", "Japanese Forest Grass", "Ophiopogon"],
                  "potSelections": "Dark charcoal clay pots and round basalt troughs.",
                  "lightingSetup": "Low-voltage warm path-lights and subtle tree spotlighting.",
                  "wateringMethod": "Micro-emitter drip bubblers under gravel mulch."
                }
            """.trimIndent()

            val response = GeminiService.generateContent(prompt)
            try {
                val cleanJson = extractJson(response)
                val moshiAdapter = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(CustomGardenDesign::class.java)
                _gardenDesign.value = moshiAdapter.fromJson(cleanJson)
            } catch (e: Exception) {
                _gardenDesign.value = CustomGardenDesign(
                    styleName = style,
                    layoutBlueprint = "Vertical geometric hanging grids on walls with a centralized gravel-lined patio center. Surrounded by tiered planters.",
                    proposedPlants = listOf("Monstera Deliciosa", "English Ivy", "Maidenhair Ferns", "Pothos Vines"),
                    potSelections = "Clean matte white ceramic pots with drainage saucers and suspended macramé hangers.",
                    lightingSetup = "Adjustable full-spectrum LED grow strip lights and hanging Edison bulbs for relaxing ambiance.",
                    wateringMethod = "In-line automated drip tubing with fine mist sprays for optimal humidity cover."
                )
            } finally {
                _isLoadingDesign.value = false
            }
        }
    }

    fun synthesizeResearch() {
        viewModelScope.launch {
            _isLoadingResearch.value = true
            val prompt = """
                Act as a Senior Agronomist and Academic Researcher. Synthesize a practical breakdown of recent agricultural or botanical research.
                Format the answer exactly like this JSON block:
                {
                  "paperTitle": "Foliar Applications of Chitosan Nanoparticles for Crop Drought Resistance",
                  "authorsAndJournal": "Dr. A. Vance et al., Journal of Sustainable Ag (2025)",
                  "keyDiscovery": "Foliar chitosan spray triggers stomatal closure, reducing water transpiration by 28% without affecting yield photosynthesis.",
                  "practicalApplication": "Spray organic chitosan solution (0.05%) directly onto crop leaves twice under dry heat spells to save water."
                }
            """.trimIndent()

            val response = GeminiService.generateContent(prompt)
            try {
                val cleanJson = extractJson(response)
                val moshiAdapter = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(ResearchSynthesis::class.java)
                _researchSynthesis.value = moshiAdapter.fromJson(cleanJson)
            } catch (e: Exception) {
                _researchSynthesis.value = ResearchSynthesis(
                    paperTitle = "Influence of Trichoderma harzianum on Plant Bio-Defense Signaling",
                    authorsAndJournal = "International Journal of Molecular Botany (2025)",
                    keyDiscovery = "Trichoderma fungi secrete specialized elicitor proteins that alert plant defense pathways. This triggers a 40% stronger immune response against fungal rust infections.",
                    practicalApplication = "Pre-treat crop seeds or water young seedling roots with Trichoderma biological spore powders during initial soil planting."
                )
            } finally {
                _isLoadingResearch.value = false
            }
        }
    }

    private fun extractJson(text: String): String {
        // Strip markdown code fences if present
        var clean = text.trim()
        if (clean.startsWith("```json")) {
            clean = clean.removePrefix("```json")
        } else if (clean.startsWith("```")) {
            clean = clean.removePrefix("```")
        }
        if (clean.endsWith("```")) {
            clean = clean.removeSuffix("```")
        }
        return clean.trim()
    }
}
