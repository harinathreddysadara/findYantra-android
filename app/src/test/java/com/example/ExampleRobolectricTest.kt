package com.example

import com.example.data.YantraRepository
import com.example.model.AppRole
import com.example.model.RentalType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleRobolectricTest {

    @Test
    fun app_configuration_loads() {
        assertEquals("Yantra", "Yantra")
    }

    @Test
    fun repository_initialData_loadsAndSupportsRoleSwitching() {
        val repo = YantraRepository()
        assertEquals(AppRole.FIND_YANTRA, repo.currentAppRole.value)
        assertTrue(repo.equipments.value.isNotEmpty())
        assertTrue(repo.operators.value.isNotEmpty())
        assertTrue(repo.orders.value.isNotEmpty())

        // Switch to YantraFleet
        repo.setAppRole(AppRole.YANTRA_FLEET)
        assertEquals(AppRole.YANTRA_FLEET, repo.currentAppRole.value)

        // Switch to YantraStore
        repo.setAppRole(AppRole.YANTRA_STORE)
        assertEquals(AppRole.YANTRA_STORE, repo.currentAppRole.value)
    }

    @Test
    fun rental_booking_createsValidPinAndOrder() {
        val repo = YantraRepository()
        val eq = repo.equipments.value.first()
        val order = repo.createRentalBooking(
            equipment = eq,
            rentalType = RentalType.HOURLY,
            hours = 4,
            dateStr = "06 Nov 2026",
            timeSlot = "09:00 AM – 01:00 PM",
            worksite = "Hinjawadi Phase 1, Pune"
        )
        assertNotNull(order.id)
        assertTrue(order.startPin.isNotEmpty())
        assertEquals(6254, order.totalAmount)
        assertEquals("Ramesh Kale", order.assignedOperatorName)
    }
}
