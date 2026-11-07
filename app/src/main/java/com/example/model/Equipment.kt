package com.example.model

enum class EquipmentStatus(val label: String) {
    AVAILABLE("Available"),
    EN_ROUTE("En route"),
    WORKING("Working"),
    IN_SERVICE("In service")
}

enum class EquipmentCategory(val label: String) {
    FARMING("Farming"),
    INFRASTRUCTURE("Infrastructure"),
    CONSTRUCTION("Construction"),
    HEAVY_LIFTING("Heavy lifting")
}

data class Equipment(
    val id: String,
    val name: String,
    val registrationNo: String,
    val ownerName: String,
    val category: EquipmentCategory,
    val rating: Float,
    val reviewCount: Int,
    val distanceKm: Float,
    val powerHp: Int,
    val bucketCapacity: String,
    val operatorIncluded: Boolean = true,
    val hourlyRate: Int,
    val dailyRate: Int? = null,
    val perTripRate: Int? = null,
    val status: EquipmentStatus = EquipmentStatus.AVAILABLE,
    val worksiteLocation: String = "Hinjawadi Phase 1, Pune",
    val nextAvailableSlot: String = "Today, 02:00 PM",
    val engineHoursToService: Int = 46,
    val isPublicOnFindYantra: Boolean = true,
    val assignedDriverName: String? = null,
    val currentOrderNo: String? = null,
    val notes: String = "Verified owner & operator. Well maintained.",
    val imageRes: Int? = null
)
