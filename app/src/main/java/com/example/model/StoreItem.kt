package com.example.model

enum class StoreCategory(val label: String) {
    ALL("All"),
    SPARE_PARTS("Spare parts"),
    MATERIALS("Materials"),
    SERVICES("Services")
}

data class StoreItem(
    val id: String,
    val name: String,
    val category: StoreCategory,
    val partOrSpecNo: String,
    val specDetails: String,
    val price: Int,
    val deliveryTimeText: String,
    val distanceKmText: String? = null,
    val inStock: Boolean = true,
    val rating: Float = 4.8f,
    val reviewCount: Int = 86,
    val compatibleWithEquipment: String = "JCB 3DX Backhoe (MH 14 JX 4821)",
    val sellerName: String = "Deccan Spares, Pune",
    val sellerVerified: Boolean = true,
    val isGstInvoiceIncluded: Boolean = true,
    val description: String,
    val isService: Boolean = false,
    val serviceDurationHours: String? = null,
    val inclusions: List<String> = emptyList(),
    val imageRes: Int? = null
)

data class CartItem(
    val item: StoreItem,
    var quantity: Int = 1
)

data class ServiceBooking(
    val id: String,
    val serviceTitle: String,
    val workshopName: String,
    val equipmentName: String,
    val registrationNo: String,
    val scheduledDate: String,
    val scheduledSlot: String,
    val location: String,
    val totalAmount: Int,
    val status: String = "Scheduled"
)
