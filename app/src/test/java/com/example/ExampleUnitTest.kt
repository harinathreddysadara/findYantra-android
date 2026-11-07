package com.example

import com.example.data.YantraRepository
import com.example.model.AppRole
import com.example.model.OrderStatus
import com.example.model.RentalType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun repository_initialState_loadsCorrectly() {
        val repo = YantraRepository()
        assertEquals(AppRole.FIND_YANTRA, repo.currentAppRole.value)
        assertEquals(5, repo.equipments.value.size)
        assertEquals(4, repo.operators.value.size)
        assertEquals(3, repo.orders.value.size)
    }

    @Test
    fun repository_appRoleSwitching_works() {
        val repo = YantraRepository()
        repo.setAppRole(AppRole.YANTRA_FLEET)
        assertEquals(AppRole.YANTRA_FLEET, repo.currentAppRole.value)

        repo.setAppRole(AppRole.YANTRA_STORE)
        assertEquals(AppRole.YANTRA_STORE, repo.currentAppRole.value)
    }

    @Test
    fun repository_createRentalBooking_calculatesPricingAndPin() {
        val repo = YantraRepository()
        val jcb = repo.equipments.value.first { it.name.contains("JCB") }
        val order = repo.createRentalBooking(
            equipment = jcb,
            rentalType = RentalType.HOURLY,
            hours = 4,
            dateStr = "06 Nov 2026",
            timeSlot = "09:00 AM – 01:00 PM",
            worksite = "Hinjawadi Phase 1, Pune"
        )

        assertNotNull(order.id)
        assertEquals(4800, order.baseAmount)
        assertEquals(500, order.transportAmount)
        assertEquals(954, order.gstAmount)
        assertEquals(6254, order.totalAmount)
        assertEquals("4821".length, order.startPin.length)
        assertEquals(OrderStatus.EN_ROUTE, order.status)
    }

    @Test
    fun repository_assignOperator_updatesStatus() {
        val repo = YantraRepository()
        val orderId = "FY-2049"
        repo.assignOperatorToOrder(orderId, "op-2")

        val updated = repo.orders.value.first { it.id == orderId }
        assertEquals("Sunil Jadhav", updated.assignedOperatorName)
        assertEquals(OrderStatus.EN_ROUTE, updated.status)
    }

    @Test
    fun repository_cartOperations_work() {
        val repo = YantraRepository()
        val filter = repo.storeItems.value.first()
        repo.addToCart(filter)
        assertEquals(1, repo.cart.value.size)
        assertEquals(1, repo.cart.value.first().quantity)

        repo.updateCartQuantity(filter.id, 1)
        assertEquals(2, repo.cart.value.first().quantity)

        repo.clearCart()
        assertTrue(repo.cart.value.isEmpty())
    }
}
