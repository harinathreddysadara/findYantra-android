package com.example.data

import com.example.R
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class YantraRepository {

    private val _currentAppRole = MutableStateFlow(AppRole.FIND_YANTRA)
    val currentAppRole: StateFlow<AppRole> = _currentAppRole.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentWorksite = MutableStateFlow("Hinjawadi Phase 1, Pune")
    val currentWorksite: StateFlow<String> = _currentWorksite.asStateFlow()

    private val _shoppingForEquipment = MutableStateFlow("JCB 3DX · MH 14 JX 4821")
    val shoppingForEquipment: StateFlow<String> = _shoppingForEquipment.asStateFlow()

    // Equipments
    private val _equipments = MutableStateFlow<List<Equipment>>(emptyList())
    val equipments: StateFlow<List<Equipment>> = _equipments.asStateFlow()

    // Operators
    private val _operators = MutableStateFlow<List<Operator>>(emptyList())
    val operators: StateFlow<List<Operator>> = _operators.asStateFlow()

    // Orders
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    // Store items
    private val _storeItems = MutableStateFlow<List<StoreItem>>(emptyList())
    val storeItems: StateFlow<List<StoreItem>> = _storeItems.asStateFlow()

    // Cart
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    // Scheduled Services
    private val _scheduledServices = MutableStateFlow<List<ServiceBooking>>(emptyList())
    val scheduledServices: StateFlow<List<ServiceBooking>> = _scheduledServices.asStateFlow()

    // Active tracking order ID for FindYantra
    private val _activeTrackingOrderId = MutableStateFlow<String?>("FY-2048")
    val activeTrackingOrderId: StateFlow<String?> = _activeTrackingOrderId.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val initialEquipments = listOf(
            Equipment(
                id = "eq-1",
                name = "JCB 3DX Backhoe",
                registrationNo = "MH 14 JX 4821",
                ownerName = "Patil Earthmovers",
                category = EquipmentCategory.CONSTRUCTION,
                rating = 4.8f,
                reviewCount = 32,
                distanceKm = 2.4f,
                powerHp = 76,
                bucketCapacity = "1 m³",
                operatorIncluded = true,
                hourlyRate = 1200,
                dailyRate = 8500,
                perTripRate = null,
                status = EquipmentStatus.EN_ROUTE,
                worksiteLocation = "Hinjawadi Phase 1, Pune",
                nextAvailableSlot = "06 Nov · 02:00 PM",
                engineHoursToService = 46,
                isPublicOnFindYantra = true,
                assignedDriverName = "Ramesh Kale",
                currentOrderNo = "FY-2048",
                imageRes = R.drawable.jcb_backhoe_thumb_1793977099159
            ),
            Equipment(
                id = "eq-2",
                name = "Mahindra 575 DI",
                registrationNo = "MH 14 AQ 3311",
                ownerName = "Shinde Agro Rentals",
                category = EquipmentCategory.FARMING,
                rating = 4.7f,
                reviewCount = 24,
                distanceKm = 3.1f,
                powerHp = 45,
                bucketCapacity = "Trolley / Plough ready",
                operatorIncluded = true,
                hourlyRate = 850,
                dailyRate = 6000,
                perTripRate = 1800,
                status = EquipmentStatus.AVAILABLE,
                worksiteLocation = "Wakad, Pune",
                nextAvailableSlot = "Ready now",
                engineHoursToService = 120,
                isPublicOnFindYantra = true,
                assignedDriverName = null,
                currentOrderNo = null,
                imageRes = R.drawable.mahindra_tractor_thumb_1793977128208
            ),
            Equipment(
                id = "eq-3",
                name = "Tata Signa 2823",
                registrationNo = "MH 14 TR 2096",
                ownerName = "Patil Earthmovers",
                category = EquipmentCategory.INFRASTRUCTURE,
                rating = 4.9f,
                reviewCount = 18,
                distanceKm = 4.6f,
                powerHp = 230,
                bucketCapacity = "16 m³ capacity",
                operatorIncluded = true,
                hourlyRate = 1800,
                dailyRate = 14000,
                perTripRate = 3500,
                status = EquipmentStatus.AVAILABLE,
                worksiteLocation = "Wakad → Baner, Pune",
                nextAvailableSlot = "Next order · 10:00 AM",
                engineHoursToService = 85,
                isPublicOnFindYantra = true,
                assignedDriverName = null,
                currentOrderNo = "FY-2049",
                imageRes = R.drawable.tata_signa_thumb_1793977156047
            ),
            Equipment(
                id = "eq-4",
                name = "CAT 320 Excavator",
                registrationNo = "MH 12 EQ 7053",
                ownerName = "Patil Earthmovers",
                category = EquipmentCategory.HEAVY_LIFTING,
                rating = 4.9f,
                reviewCount = 41,
                distanceKm = 6.2f,
                powerHp = 148,
                bucketCapacity = "1.2 m³",
                operatorIncluded = true,
                hourlyRate = 2200,
                dailyRate = 18000,
                perTripRate = null,
                status = EquipmentStatus.IN_SERVICE,
                worksiteLocation = "Deccan Workshop, Pune",
                nextAvailableSlot = "Tomorrow, 08:00 AM",
                engineHoursToService = 0,
                isPublicOnFindYantra = false,
                assignedDriverName = null,
                currentOrderNo = null,
                imageRes = R.drawable.jcb_backhoe_thumb_1793977099159
            ),
            Equipment(
                id = "eq-5",
                name = "Bharat BC 250 Compactor",
                registrationNo = "MH 14 CP 5542",
                ownerName = "Patil Earthmovers",
                category = EquipmentCategory.INFRASTRUCTURE,
                rating = 4.8f,
                reviewCount = 15,
                distanceKm = 5.0f,
                powerHp = 105,
                bucketCapacity = "10 Ton Roller",
                operatorIncluded = true,
                hourlyRate = 1100,
                dailyRate = 7080,
                perTripRate = null,
                status = EquipmentStatus.WORKING,
                worksiteLocation = "Baner worksite",
                nextAvailableSlot = "Today, 05:00 PM",
                engineHoursToService = 62,
                isPublicOnFindYantra = true,
                assignedDriverName = "Akash Pawar",
                currentOrderNo = "FY-2045",
                imageRes = null
            )
        )
        _equipments.value = initialEquipments

        val initialOperators = listOf(
            Operator(
                id = "op-1",
                name = "Ramesh Kale",
                initials = "RK",
                role = "Backhoe operator",
                status = OperatorStatus.ON_DUTY,
                assignedEquipmentId = "eq-1",
                assignedEquipmentName = "JCB 3DX · MH 14 JX 4821",
                registrationNo = "MH 14 JX 4821",
                currentOrderNo = "FY-2048",
                locationDesc = "FY-2048 · En route to Hinjawadi",
                phone = "+91 98220 44123",
                experienceYears = 8,
                rating = 4.9f,
                licenseValid = true
            ),
            Operator(
                id = "op-2",
                name = "Sunil Jadhav",
                initials = "SJ",
                role = "Heavy vehicle driver",
                status = OperatorStatus.AVAILABLE,
                assignedEquipmentId = null,
                assignedEquipmentName = null,
                registrationNo = null,
                currentOrderNo = null,
                locationDesc = "Tipper, dumper · Ready in Wakad",
                phone = "+91 97654 88201",
                experienceYears = 6,
                rating = 4.7f,
                licenseValid = true
            ),
            Operator(
                id = "op-3",
                name = "Akash Pawar",
                initials = "AP",
                role = "Compactor operator",
                status = OperatorStatus.ON_DUTY,
                assignedEquipmentId = "eq-5",
                assignedEquipmentName = "Bharat BC 250",
                registrationNo = "MH 14 CP 5542",
                currentOrderNo = "FY-2045",
                locationDesc = "Baner worksite · Until 05:00 PM",
                phone = "+91 99210 11984",
                experienceYears = 5,
                rating = 4.8f,
                licenseValid = true
            ),
            Operator(
                id = "op-4",
                name = "Meena Shinde",
                initials = "MS",
                role = "Excavator operator",
                status = OperatorStatus.OFF_DUTY,
                assignedEquipmentId = null,
                assignedEquipmentName = null,
                registrationNo = null,
                currentOrderNo = null,
                locationDesc = "Next shift · 07 Nov, 08:00 AM",
                phone = "+91 94231 77652",
                experienceYears = 7,
                rating = 4.9f,
                licenseValid = true,
                nextShift = "07 Nov, 08:00 AM"
            )
        )
        _operators.value = initialOperators

        val initialOrders = listOf(
            Order(
                id = "FY-2049",
                source = OrderSource.FINDYANTRA,
                equipmentId = "eq-3",
                equipmentName = "Tata Signa 2823",
                registrationNo = "MH 14 TR 2096",
                customerName = "Kiran Infra",
                worksite = "Wakad → Baner",
                timeSlot = "10:00 AM · 2 trips",
                rentalType = RentalType.PER_TRIP,
                durationText = "2 trips",
                baseAmount = 7000,
                transportAmount = 0,
                gstAmount = 1260,
                totalAmount = 8260,
                status = OrderStatus.NEEDS_ASSIGNMENT,
                assignedOperatorId = null,
                assignedOperatorName = null,
                notes = "Sand & gravel transport"
            ),
            Order(
                id = "FY-2048",
                source = OrderSource.FINDYANTRA,
                equipmentId = "eq-1",
                equipmentName = "JCB 3DX Backhoe",
                registrationNo = "MH 14 JX 4821",
                customerName = "Sanjay Deshmukh",
                worksite = "Hinjawadi Phase 1",
                timeSlot = "09:00 AM – 01:00 PM",
                rentalType = RentalType.HOURLY,
                durationText = "4 hours",
                baseAmount = 4800,
                transportAmount = 500,
                gstAmount = 954,
                totalAmount = 6254,
                status = OrderStatus.EN_ROUTE,
                assignedOperatorId = "op-1",
                assignedOperatorName = "Ramesh Kale",
                assignedOperatorPhone = "+91 98220 44123",
                startPin = "4821",
                etaMinutes = 12,
                notes = "Trench digging and level earthworks"
            ),
            Order(
                id = "FY-2045",
                source = OrderSource.DIRECT_BOOKING,
                equipmentId = "eq-5",
                equipmentName = "Bharat BC 250 Compactor",
                registrationNo = "MH 14 CP 5542",
                customerName = "Baner Roadworks Ltd",
                worksite = "Baner Road",
                timeSlot = "08:00 AM – 05:00 PM",
                rentalType = RentalType.DAILY,
                durationText = "Daily",
                baseAmount = 6000,
                transportAmount = 0,
                gstAmount = 1080,
                totalAmount = 7080,
                status = OrderStatus.IN_PROGRESS,
                assignedOperatorId = "op-3",
                assignedOperatorName = "Akash Pawar",
                assignedOperatorPhone = "+91 99210 11984"
            )
        )
        _orders.value = initialOrders

        val initialStoreItems = listOf(
            StoreItem(
                id = "store-1",
                name = "Hydraulic oil filter",
                category = StoreCategory.SPARE_PARTS,
                partOrSpecNo = "Part no. 32/925346",
                specDetails = "OEM specification",
                price = 1850,
                deliveryTimeText = "Delivery tomorrow, 07 Nov",
                distanceKmText = null,
                inStock = true,
                rating = 4.8f,
                reviewCount = 86,
                compatibleWithEquipment = "JCB 3DX Backhoe (MH 14 JX 4821)",
                sellerName = "Deccan Spares, Pune",
                sellerVerified = true,
                isGstInvoiceIncluded = true,
                description = "High-efficiency filtration for the hydraulic circuit. Spin-on fitment with sealing gasket included. Check the part number before installation.",
                imageRes = R.drawable.oil_filter_thumb_1793977176267
            ),
            StoreItem(
                id = "store-2",
                name = "PPC cement · 50 kg",
                category = StoreCategory.MATERIALS,
                partOrSpecNo = "AURA PPC 53 Grade",
                specDetails = "Construction materials",
                price = 390,
                deliveryTimeText = "In stock",
                distanceKmText = "1.8 km away",
                inStock = true,
                rating = 4.9f,
                reviewCount = 142,
                compatibleWithEquipment = "Universal construction material",
                sellerName = "Pune Building Supplies",
                sellerVerified = true,
                isGstInvoiceIncluded = true,
                description = "High strength Portland Pozzolana Cement for heavy foundation works and civil construction.",
                imageRes = R.drawable.cement_bag_thumb_1793977224662
            ),
            StoreItem(
                id = "store-3",
                name = "JCB 500-hour service",
                category = StoreCategory.SERVICES,
                partOrSpecNo = "Service code: SVC-500H",
                specDetails = "Deccan Equipment Care · Pune",
                price = 2499,
                deliveryTimeText = "On-site · 2–3 hrs",
                distanceKmText = null,
                inStock = true,
                rating = 4.8f,
                reviewCount = 126,
                compatibleWithEquipment = "JCB 3DX · MH 14 JX 4821",
                sellerName = "Deccan Equipment Care",
                sellerVerified = true,
                isGstInvoiceIncluded = true,
                isService = true,
                serviceDurationHours = "2–3 hrs",
                inclusions = listOf(
                    "Engine & hydraulic system inspection",
                    "Filter replacement labour & fluid checks",
                    "Greasing, safety checks & service report"
                ),
                description = "Comprehensive periodic maintenance by certified heavy machinery technicians. Doorstep inspection and report upload to YantraFleet.",
                imageRes = R.drawable.service_mechanic_thumb_1793977200886
            ),
            StoreItem(
                id = "store-4",
                name = "Heavy Duty Hydraulic Hose (200 Bar)",
                category = StoreCategory.SPARE_PARTS,
                partOrSpecNo = "Part no. HYD-200B-4M",
                specDetails = "High pressure flexible steel braided",
                price = 3200,
                deliveryTimeText = "Delivery today in 3 hrs",
                distanceKmText = "3.4 km away",
                inStock = true,
                rating = 4.7f,
                reviewCount = 38,
                compatibleWithEquipment = "JCB 3DX & CAT 320 Excavator",
                sellerName = "HydroTech Spares",
                sellerVerified = true,
                description = "Reinforced high-temperature and extreme pressure hydraulic line for backhoes, loaders and excavators.",
                imageRes = null
            )
        )
        _storeItems.value = initialStoreItems
    }

    fun setAppRole(role: AppRole) {
        _currentAppRole.value = role
    }

    fun setLoggedIn(loggedIn: Boolean) {
        _isLoggedIn.value = loggedIn
    }

    fun updateWorksite(location: String) {
        _currentWorksite.value = location
    }

    fun setShoppingForEquipment(equipmentInfo: String) {
        _shoppingForEquipment.value = equipmentInfo
    }

    fun togglePublishOnFindYantra(equipmentId: String, publish: Boolean) {
        _equipments.update { list ->
            list.map { eq ->
                if (eq.id == equipmentId) eq.copy(isPublicOnFindYantra = publish) else eq
            }
        }
    }

    fun updateEquipmentRates(equipmentId: String, hourly: Int, daily: Int?) {
        _equipments.update { list ->
            list.map { eq ->
                if (eq.id == equipmentId) eq.copy(hourlyRate = hourly, dailyRate = daily) else eq
            }
        }
    }

    fun assignOperatorToOrder(orderId: String, operatorId: String) {
        val operator = _operators.value.find { it.id == operatorId } ?: return
        _orders.update { list ->
            list.map { ord ->
                if (ord.id == orderId) {
                    ord.copy(
                        status = OrderStatus.EN_ROUTE,
                        assignedOperatorId = operator.id,
                        assignedOperatorName = operator.name,
                        assignedOperatorPhone = operator.phone
                    )
                } else ord
            }
        }
        _operators.update { list ->
            list.map { op ->
                if (op.id == operatorId) {
                    op.copy(
                        status = OperatorStatus.ON_DUTY,
                        currentOrderNo = orderId,
                        locationDesc = "$orderId · En route"
                    )
                } else op
            }
        }
    }

    fun createRentalBooking(
        equipment: Equipment,
        rentalType: RentalType,
        hours: Int,
        dateStr: String,
        timeSlot: String,
        worksite: String
    ): Order {
        val base = when (rentalType) {
            RentalType.HOURLY -> equipment.hourlyRate * hours
            RentalType.DAILY -> equipment.dailyRate ?: (equipment.hourlyRate * 8)
            RentalType.PER_TRIP -> equipment.perTripRate ?: 3500
        }
        val transport = 500
        val gst = ((base + transport) * 0.18f).toInt()
        val total = base + transport + gst

        val newOrder = Order(
            id = "FY-${(2050..2099).random()}",
            source = OrderSource.FINDYANTRA,
            equipmentId = equipment.id,
            equipmentName = equipment.name,
            registrationNo = equipment.registrationNo,
            customerName = "Sanjay Deshmukh",
            worksite = worksite,
            dateStr = dateStr,
            timeSlot = timeSlot,
            rentalType = rentalType,
            durationText = if (rentalType == RentalType.HOURLY) "$hours hours" else rentalType.label,
            baseAmount = base,
            transportAmount = transport,
            gstAmount = gst,
            totalAmount = total,
            status = OrderStatus.EN_ROUTE,
            assignedOperatorId = "op-1",
            assignedOperatorName = "Ramesh Kale",
            assignedOperatorPhone = "+91 98220 44123",
            startPin = (1000..9999).random().toString(),
            etaMinutes = 12
        )

        _orders.update { listOf(newOrder) + it }
        _activeTrackingOrderId.value = newOrder.id
        return newOrder
    }

    fun addToCart(item: StoreItem) {
        _cart.update { current ->
            val existing = current.find { it.item.id == item.id }
            if (existing != null) {
                current.map {
                    if (it.item.id == item.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                current + CartItem(item, 1)
            }
        }
    }

    fun updateCartQuantity(itemId: String, delta: Int) {
        _cart.update { current ->
            current.mapNotNull {
                if (it.item.id == itemId) {
                    val newQty = it.quantity + delta
                    if (newQty > 0) it.copy(quantity = newQty) else null
                } else it
            }
        }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    fun scheduleService(
        serviceItem: StoreItem,
        equipmentName: String,
        registrationNo: String,
        date: String,
        slot: String,
        location: String,
        totalPayable: Int
    ): ServiceBooking {
        val booking = ServiceBooking(
            id = "SVC-${(100..999).random()}",
            serviceTitle = serviceItem.name,
            workshopName = serviceItem.sellerName,
            equipmentName = equipmentName,
            registrationNo = registrationNo,
            scheduledDate = date,
            scheduledSlot = slot,
            location = location,
            totalAmount = totalPayable,
            status = "Scheduled"
        )
        _scheduledServices.update { listOf(booking) + it }
        return booking
    }

    fun addEquipment(
        name: String,
        regNo: String,
        category: EquipmentCategory,
        powerHp: Int,
        hourlyRate: Int,
        dailyRate: Int?
    ) {
        val newEq = Equipment(
            id = "eq-${System.currentTimeMillis()}",
            name = name,
            registrationNo = regNo,
            ownerName = "Patil Earthmovers",
            category = category,
            rating = 5.0f,
            reviewCount = 1,
            distanceKm = 1.0f,
            powerHp = powerHp,
            bucketCapacity = "Standard",
            operatorIncluded = true,
            hourlyRate = hourlyRate,
            dailyRate = dailyRate,
            status = EquipmentStatus.AVAILABLE,
            worksiteLocation = _currentWorksite.value,
            isPublicOnFindYantra = true
        )
        _equipments.update { listOf(newEq) + it }
    }

    fun addOperator(name: String, role: String, phone: String) {
        val initials = name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").uppercase()
        val newOp = Operator(
            id = "op-${System.currentTimeMillis()}",
            name = name,
            initials = if (initials.isNotEmpty()) initials else "OP",
            role = role,
            status = OperatorStatus.AVAILABLE,
            locationDesc = "Ready in Hinjawadi",
            phone = phone,
            experienceYears = 5,
            rating = 4.8f,
            licenseValid = true
        )
        _operators.update { listOf(newOp) + it }
    }
}
