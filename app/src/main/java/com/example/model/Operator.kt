package com.example.model

enum class OperatorStatus(val label: String) {
    ON_DUTY("On duty"),
    AVAILABLE("Available"),
    OFF_DUTY("Off duty")
}

data class Operator(
    val id: String,
    val name: String,
    val initials: String,
    val role: String,
    val status: OperatorStatus,
    val assignedEquipmentId: String? = null,
    val assignedEquipmentName: String? = null,
    val registrationNo: String? = null,
    val currentOrderNo: String? = null,
    val locationDesc: String,
    val phone: String = "+91 98220 44123",
    val experienceYears: Int = 8,
    val rating: Float = 4.9f,
    val licenseValid: Boolean = true,
    val nextShift: String? = null
)
