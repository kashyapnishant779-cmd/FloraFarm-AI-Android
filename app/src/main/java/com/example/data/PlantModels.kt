package com.example.data

data class PlantEncyclopediaItem(
    val commonName: String,
    val scientificName: String,
    val localNames: String,
    val family: String,
    val genus: String,
    val species: String,
    val originCountry: String,
    val nativeRegion: String,
    val botanicalInfo: String,
    val uses: String,
    val medicinalUses: String,
    val nutritionalInfo: String,
    val economicImportance: String,
    val scientificResearch: String,
    val bestGrowingSeason: String,
    val temperatureRequirement: String,
    val humidityRequirement: String,
    val sunlightRequirement: String,
    val soilType: String,
    val soilPh: String,
    val soilPreparation: String,
    val waterQuantity: String,     // e.g. "500 ml/day"
    val fertilizerQuantity: String, // e.g. "15 grams/month"
    val compostQuantity: String,    // e.g. "1.5 kg/sq meter"
    val spacingDistance: String,    // e.g. "45 cm"
    val plantingDepth: String,      // e.g. "1.5 cm"
    val potOrContainerSize: String, // e.g. "12 inch pot"
    val fieldSpacing: String,       // e.g. "Row-to-row: 75 cm"
    val timeline: List<TimelineStep>,
    val imageUrl: String
)

data class TimelineStep(
    val day: Int,
    val title: String,
    val description: String
)

data class DiagnosisResult(
    val plantName: String,
    val ageEstimate: String,
    val condition: String,
    val confidence: Double,
    val description: String,
    val affectedParts: String,
    val organicTreatment: String,
    val chemicalTreatment: String,
    val recoveryTimeline: String
)

data class ClimateAdvice(
    val locationName: String,
    val temperature: String,
    val humidity: String,
    val rainfall: String,
    val alertType: String? = null, // "FROST", "HEATWAVE", "STORM", "RAIN" or null
    val alertMessage: String? = null,
    val wateringRecommendation: String,
    val fertilizerTiming: String,
    val seasonalTips: String
)

data class VarietyComparison(
    val varietyName: String,
    val expectedYield: String,
    val tasteFlavorProfile: String,
    val climateResistance: String,
    val diseaseResistance: String,
    val marketValue: String
)

data class SoilReport(
    val pH: Double,
    val nitrogenLevel: String,   // N
    val phosphorusLevel: String, // P
    val potassiumLevel: String,  // K
    val organicMatter: String,
    val healthScore: Int,        // 0-100
    val recommendations: String
)

data class FarmingCostProfitReport(
    val landPrep: String,
    val cropPlanning: String,
    val seedQuantityRequired: String,
    val waterRequirementTotal: String,
    val fertilizerRequiredTotal: String,
    val machineryRecommendation: String,
    val laborEstimation: String,
    val productionCost: String,
    val yieldPrediction: String,
    val profitEstimation: String,
    val marketDemand: String,
    val exportOpportunities: String
)

data class ResearchSynthesis(
    val paperTitle: String,
    val authorsAndJournal: String,
    val keyDiscovery: String,
    val practicalApplication: String
)

data class CustomGardenDesign(
    val styleName: String,
    val layoutBlueprint: String,
    val proposedPlants: List<String>,
    val potSelections: String,
    val lightingSetup: String,
    val wateringMethod: String
)

data class CommunityPost(
    val id: String,
    val author: String,
    val authorRole: String, // "Botanist", "Home Gardener", "Commercial Farmer"
    val timeAgo: String,
    val content: String,
    val likes: Int,
    val repliesCount: Int,
    val plantGroup: String,
    val imageUrl: String? = null
)

// Pre-defined database of rich plants
object PlantDataset {
    val plants = listOf(
        PlantEncyclopediaItem(
            commonName = "Tomato",
            scientificName = "Solanum lycopersicum",
            localNames = "Tamatar (Hindi), Tomate (Spanish), Pomodoro (Italian), Fanqie (Chinese)",
            family = "Solanaceae",
            genus = "Solanum",
            species = "S. lycopersicum",
            originCountry = "Peru & Ecuador",
            nativeRegion = "Andean region of South America",
            botanicalInfo = "A deeply-lobed vine-like herbaceous perennial or annual. Features pinnate compound green leaves, yellow self-pollinating flowers, and fleshy berries (fruits) rich in lycopene.",
            uses = "Widely consumed fresh in salads, cooked in curries, processed into ketchup, tomato paste, and sauces.",
            medicinalUses = "High in lycopene (antioxidant), Vitamin C, and Potassium. Supports cardiovascular health, skin barrier synthesis, and visual acuity.",
            nutritionalInfo = "Calories: 18 kcal, Water: 95%, Protein: 0.9g, Carbs: 3.9g, Lycopene: 2.5mg per 100g.",
            economicImportance = "High-value cash crop worldwide. Represents over 15% of global vegetable production value, powering substantial processing and logistics sectors.",
            scientificResearch = "Harvard Health study links regular lycopene consumption to a 30% reduction in cardiovascular disease. Active studies focus on drought-tolerant wild varieties (S. pimpinellifolium).",
            bestGrowingSeason = "Spring to early Summer (Warm Season)",
            temperatureRequirement = "21°C to 29°C (Night min: 15°C)",
            humidityRequirement = "60% to 75%",
            sunlightRequirement = "6 to 8 hours of direct, intense sunlight daily",
            soilType = "Rich, well-draining sandy loam with high organic compost",
            soilPh = "6.0 to 6.8 (Slightly Acidic)",
            soilPreparation = "Deep till to 30 cm, mix 4 inches of aged organic compost and multi-strain mycorrhizal fungi inoculants.",
            waterQuantity = "500 ml to 1 litre per day per plant",
            fertilizerQuantity = "20 grams of 5-10-10 low-nitrogen NPK per month",
            compostQuantity = "2 kg per square meter",
            spacingDistance = "45 cm to 60 cm",
            plantingDepth = "1.5 cm",
            potOrContainerSize = "5-gallon container (12-14 inches diameter)",
            fieldSpacing = "Row spacing: 90-120 cm, Plant spacing: 45-60 cm",
            timeline = listOf(
                TimelineStep(1, "Soil Preparation & Seed Sowing", "Till soil deeply, mix compost, and sow seeds in warm starter soil."),
                TimelineStep(7, "Germination & Sprouts", "Seedlings break through the soil; keep in bright light and humid air."),
                TimelineStep(21, "Transplantation", "Move seedlings into deep 5-gallon pots or open fields; secure stakes."),
                TimelineStep(45, "Staking & Sucker Pruning", "Prune lower non-fruiting suckers and secure vines to wire support cages."),
                TimelineStep(60, "First Flowering", "Yellow blossoms emerge. Water consistently at base; add potassium boost."),
                TimelineStep(75, "Fruit Setting", "Small green spherical berries form. Guard against blossom end rot."),
                TimelineStep(90, "Deep Red Harvest", "Tomatoes turn rich, glossy red. Harvest carefully with stem attached.")
            ),
            imageUrl = "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=400"
        ),
        PlantEncyclopediaItem(
            commonName = "Lavender",
            scientificName = "Lavandula angustifolia",
            localNames = "Lavender (English), Lavanda (Spanish/Italian), Alfazema (Portuguese)",
            family = "Lamiaceae",
            genus = "Lavandula",
            species = "L. angustifolia",
            originCountry = "Mediterranean Basin",
            nativeRegion = "Dry, rocky mountainous regions of Spain, France, and Italy",
            botanicalInfo = "A compact, aromatic evergreen shrub with silver-green linear leaves and terminal spikes of highly fragrant purple flowers.",
            uses = "Essential oil distillation, aromatherapy, herbal teas, culinary garnish, soaps, and perfumes.",
            medicinalUses = "Contains linalool and linalyl acetate. Reduces anxiety, promotes restful sleep, alleviates minor skin burns, and acts as an antiseptic.",
            nutritionalInfo = "Mainly used as herb or oil. Trace vitamins A, C, and iron in dried culinary buds.",
            economicImportance = "High-value crop for fragrance, cosmetic, and wellness tourism industries. Grasse region of France leads global trade.",
            scientificResearch = "Clinical trials show inhaling lavender essential oil significantly reduces salivary cortisol and elevates alpha wave activity, indicating direct calming effects on the nervous system.",
            bestGrowingSeason = "Late Spring to Summer (Perennial)",
            temperatureRequirement = "18°C to 30°C",
            humidityRequirement = "35% to 50% (Requires dry, breezy climate)",
            sunlightRequirement = "8+ hours of direct sunlight",
            soilType = "Sandy, rocky, extremely well-draining alkaline soil",
            soilPh = "6.5 to 7.8 (Prefers Alkaline)",
            soilPreparation = "Add coarse builders sand, pea gravel, and agricultural lime to elevate pH and guarantee zero root-water retention.",
            waterQuantity = "150 ml every 4 to 5 days (Allow complete drying)",
            fertilizerQuantity = "Zero. High nutrients reduce aroma oils",
            compostQuantity = "Minimal (0.2 kg per plant per year)",
            spacingDistance = "30 cm to 45 cm",
            plantingDepth = "0.5 cm",
            potOrContainerSize = "10 inch terracotta container (for optimal root aerating)",
            fieldSpacing = "Row-to-row: 90 cm, Plant-to-plant: 45 cm",
            timeline = listOf(
                TimelineStep(1, "Cold Stratification", "Keep seeds in refrigerator for 3 weeks to trigger germination hormones."),
                TimelineStep(25, "Sowing Seeds", "Sow on sandy surface under warm heat mats; spray mist sparingly."),
                TimelineStep(60, "Pricking Out", "Transfer small, silver-leaved seedlings into single well-ventilated pots."),
                TimelineStep(90, "Transplanting", "Plant out in fully sunlit sandy beds; surround base with light gravel mulch."),
                TimelineStep(150, "Bush Structuring", "Prune top growth gently to encourage low bushy branching; avoid woody stems."),
                TimelineStep(365, "First Purple Blooms", "Fragrant violet spikes burst open. Harvest buds just as first flowers open.")
            ),
            imageUrl = "https://images.unsplash.com/photo-1528183429752-a97d0bf99b5a?w=400"
        ),
        PlantEncyclopediaItem(
            commonName = "Neem Tree",
            scientificName = "Azadirachta indica",
            localNames = "Margosa (English), Neem (Hindi), Nimba (Sanskrit), Veppai (Tamil)",
            family = "Meliaceae",
            genus = "Azadirachta",
            species = "A. indica",
            originCountry = "India & Myanmar",
            nativeRegion = "Tropical and semi-arid Indian subcontinent",
            botanicalInfo = "A fast-growing evergreen tree reaching up to 20 meters. Features serrated pinnate green leaves, small white fragrant flowers, and single-seeded green drupes.",
            uses = "Organic biopesticides, cosmetics, dental care twigs, soil conditioners, livestock feed, and agroforestry.",
            medicinalUses = "Extremely rich in azadirachtin, nimbin, and nimbidin. Anti-inflammatory, anti-fungal, anti-bacterial. Clears acne, dandruff, and treats dermatological disorders.",
            nutritionalInfo = "Leaves contain high levels of protein, calcium, iron, carotenoids, and protective polyphenols.",
            economicImportance = "Major source of natural biopesticides for organic farming. Powers rural cottage industries and global botanical exports.",
            scientificResearch = "World Health Organization highlights neem extract as a safe, non-toxic mosquito repellent and organic agricultural insecticide. It does not harm bees or beneficial earthworms.",
            bestGrowingSeason = "Year-round in warm tropical climates",
            temperatureRequirement = "25°C to 45°C (Can survive up to 50°C; frost sensitive)",
            humidityRequirement = "40% to 80%",
            sunlightRequirement = "Full, intense direct sunlight",
            soilType = "Drought-tolerant. Thrives in dry, sandy, gravelly, or red clay soils",
            soilPh = "6.2 to 8.2 (Highly Adaptable)",
            soilPreparation = "Mix field soil with 30% river sand to prevent waterlogging. Create a deep root well.",
            waterQuantity = "2 to 3 litres twice a week (Very low water consumption once matured)",
            fertilizerQuantity = "50g bone meal or organic manure twice a year",
            compostQuantity = "5 kg around tree drip zone annually",
            spacingDistance = "4 to 5 meters (As a tree)",
            plantingDepth = "5.0 cm",
            potOrContainerSize = "15-20 inch starter drum (then transplant to ground)",
            fieldSpacing = "Orchard row spacing: 6 meters, Tree-to-tree: 5 meters",
            timeline = listOf(
                TimelineStep(1, "Seed Extract Selection", "Remove fruit pulp and select fresh white seeds. Plant within 10 days."),
                TimelineStep(10, "Rapid Sprouts", "Young deep-red leaves emerge from sandy soil cups. Keep warm."),
                TimelineStep(45, "Starter Hardening", "Expose young trees to full sun. Water sparingly to stimulate deep taproot."),
                TimelineStep(180, "Sapling Placement", "Dig a 2-foot deep pit in dry fields. Plant sapling with rootball intact."),
                TimelineStep(365, "Trunk Hardening", "Main trunk turns grey and woody. Prune lateral branches up to 1 meter."),
                TimelineStep(1000, "Mature Tree Crown", "Tree reaches 4 meters, developing dense insect-repellent shade canopy.")
            ),
            imageUrl = "https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=400"
        ),
        PlantEncyclopediaItem(
            commonName = "Wheat",
            scientificName = "Triticum aestivum",
            localNames = "Gehun (Hindi), Trigo (Spanish), Blé (French), Wheat (English)",
            family = "Poaceae",
            genus = "Triticum",
            species = "T. aestivum",
            originCountry = "Fertile Crescent (Middle East)",
            nativeRegion = "Western Asia and Mediterranean",
            botanicalInfo = "An erect cereal grass with flat, narrow leaves, hollow stems, and terminal flower spikes (ears) bearing grain kernels.",
            uses = "Staple food staple used to produce flour for breads, chapatis, pasta, pastry, and livestock feed straw.",
            medicinalUses = "Wheatgerm oil is loaded with Vitamin E and phytosterols. Whole wheat fiber prevents metabolic syndrome.",
            nutritionalInfo = "Per 100g: Carbohydrates: 71g, Fiber: 12.2g, Protein: 12.6g, Vitamin B6, Iron, Magnesium.",
            economicImportance = "Second most-produced global cereal. Primary staple food sustaining over 35% of the human population, driving global trade and futures markets.",
            scientificResearch = "Genetic editing (CRISPR-Cas9) is currently unlocking rust-resistant wheat lineages to counter global climate shift threats.",
            bestGrowingSeason = "Autumn to early Spring (Cool Season)",
            temperatureRequirement = "12°C to 25°C",
            humidityRequirement = "50% to 65%",
            sunlightRequirement = "6 to 8 hours of direct winter sun",
            soilType = "Rich, loamy clay soils with good moisture retention",
            soilPh = "6.2 to 7.2 (Neutral)",
            soilPreparation = "Deep plow fields 3 times, level with precision, and create shallow seed drills spaced 20 cm apart.",
            waterQuantity = "Field flooding: 4 critical water cycles of 50,000 litres/acre",
            fertilizerQuantity = "60 kg Nitrogen, 30 kg Phosphorus per acre",
            compostQuantity = "5 tons per acre pre-sowing",
            spacingDistance = "5 cm",
            plantingDepth = "3.5 cm to 5.0 cm",
            potOrContainerSize = "Not recommended (Sow in broad beds or fields)",
            fieldSpacing = "Row spacing: 22 cm, Plant spacing within row: 5 cm",
            timeline = listOf(
                TimelineStep(1, "Precision Tillage & Sowing", "Sow seeds at 4cm depth in damp leveled fields after deep manure till."),
                TimelineStep(10, "Crown Root Initiation", "First green blades carpet the field. Initiate first light irrigation cycle."),
                TimelineStep(30, "Tillering Phase", "Lateral side shoots emerge, multiplying head count. Apply urea."),
                TimelineStep(60, "Jointing Stage", "Stems extend rapidly upwards, forming nodes. Keep moisture optimal."),
                TimelineStep(75, "Booting & Heading", "Green seed spikes push out of flag leaf. Extremely critical water stage."),
                TimelineStep(100, "Milky & Dough Phase", "Grain shells fill with white milk, which hardens into starch dough."),
                TimelineStep(120, "Golden Harvesting", "Stalks dry into shining golden straw. Reaping, threshing, and bagging.")
            ),
            imageUrl = "https://images.unsplash.com/photo-1574323347407-f5e1ad6d020b?w=400"
        ),
        PlantEncyclopediaItem(
            commonName = "Monstera Deliciosa",
            scientificName = "Monstera deliciosa",
            localNames = "Swiss Cheese Plant (English), Ceriman (Spanish), Monstera (Italian)",
            family = "Araceae",
            genus = "Monstera",
            species = "M. deliciosa",
            originCountry = "Southern Mexico & Panama",
            nativeRegion = "Tropical rainforest understories",
            botanicalInfo = "An evergreen hemiepiphytic vine with thick, fleshy aerial roots and large, glossy green cordate leaves that develop deep fenestrations (slits) as they mature.",
            uses = "Highly popular indoor air-purifying plant, ornamental interior design, and edible tropical fruit (when fully ripe).",
            medicinalUses = "Mainly used as air purifier. Filters toxic formaldehyde, benzene, and trichloroethylene from enclosed indoor spaces.",
            nutritionalInfo = "Ripe fruit has a pineapple-banana taste, loaded with Vitamin C, but unripe fruit contains toxic calcium oxalate needles.",
            economicImportance = "Extremely popular in global commercial indoor nurseries and ornamental foliage trade. Retails at premium pricing.",
            scientificResearch = "NASA Clean Air Study confirms Monstera's broad leaf area index highly accelerates rate of airborne microparticle absorption.",
            bestGrowingSeason = "Year-round (Indoor Tropical)",
            temperatureRequirement = "18°C to 27°C (Sensitive to drafts)",
            humidityRequirement = "70% to 85% (Requires misting)",
            sunlightRequirement = "Bright, indirect, dappled sunlight (Direct sun scorches leaves)",
            soilType = "Rich peat moss, perlite, pine bark chunky orchid mix",
            soilPh = "5.5 to 6.5 (Acidic)",
            soilPreparation = "Create a light, chunky potting mix using equal parts peat moss, orchid bark, perlite, and worm castings.",
            waterQuantity = "350 ml every 7 days (Allow top 2 inches to dry fully)",
            fertilizerQuantity = "10 ml liquid organic seaweed fertilizer monthly",
            compostQuantity = "1 cup of worm castings mixed in soil twice a year",
            spacingDistance = "60 cm (Prefers climbing support poles)",
            plantingDepth = "4.0 cm",
            potOrContainerSize = "10 to 12 inch well-ventilating nursery pot",
            fieldSpacing = "Keep indoor plants separated by 1 meter to allow leaf span",
            timeline = listOf(
                TimelineStep(1, "Stem Cutting Prep", "Take a stem cutting containing at least one leaf node and healthy aerial root."),
                TimelineStep(2, "Water Propagation", "Submerge stem node in clean distilled water. Change water every 3 days."),
                TimelineStep(21, "Root Bursting", "Thick white secondary roots emerge from node. Allow to reach 5 cm long."),
                TimelineStep(45, "Potting in Chunk Soil", "Pot rooted stem in loose orchid bark mix. Insert a sturdy moss climbing pole."),
                TimelineStep(75, "First New Leaf Shoot", "A pale green tightly curled leaf sheath emerges. Maintain humidity."),
                TimelineStep(90, "Leaf Unfurling", "Leaf unfurls into a large leaf. Initial leaf fenestrations (slits) emerge.")
            ),
            imageUrl = "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?w=400"
        )
    )
}
