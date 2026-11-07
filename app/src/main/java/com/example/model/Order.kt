package com.example.model

enum class OrderSource(val label: String) {
    FINDYANTRA("FINDYANTRA"),
    DIRECT_BOOKING("DIRECT BOOKING")
}

enum class OrderStatus(val label: String) {
    NEEDS_ASSIGNMENT("Needs assignment"),
    EN_ROUTE("En route"),
    IN_PROGRESS("In progress"),
    COMPLETED("Completed")
}

enum class RentalType(val label: String) {
    HOURLY("Hourly"),
    DAILY("Daily"),
    PER_TRIP("Per trip")
}

data class Order(
    val id: String, // e.g. FY-2048
    val source: OrderSource,
    val equipmentId: String,
    val equipmentName: String,
    val registrationNo: String,
    val customerName: String,
    val worksite: String,
    val dateStr: String = "06 Nov 2026",
    val timeSlot: String,
    val rentalType: RentalType,
    val durationText: String,
    val baseAmount: Int,
    val transportAmount: Int = 500,
    val gstAmount: Int,
    val totalAmount: Int,
    val status: OrderStatus,
    val assignedOperatorId: String? = null,
    val assignedOperatorName: String? = null,
    val assignedOperatorPhone: String? = "+91 98220 44123",
    val startPin: String = "4821",
    val etaMinutes: Int = 12,
    val notes: String = "Operator included. Diesel billed at actuals."
)
