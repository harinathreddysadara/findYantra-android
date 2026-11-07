package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.YantraRepository
import com.example.model.*
import com.example.ui.components.AppRoleSwitcherDialog
import com.example.ui.components.YantraBottomNav
import com.example.ui.components.YantraTopBar
import com.example.ui.screens.account.AccountProfileScreen
import com.example.ui.screens.account.BackendDocsDialog
import com.example.ui.screens.ai.YantraAiAssistantDialog
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.findyantra.FindYantraBookingsScreen
import com.example.ui.screens.findyantra.FindYantraExploreScreen
import com.example.ui.screens.findyantra.RentalBookingScreen
import com.example.ui.screens.findyantra.TrackBookingScreen
import com.example.ui.screens.yantrafleet.FleetInventoryScreen
import com.example.ui.screens.yantrafleet.FleetOperatorsScreen
import com.example.ui.screens.yantrafleet.FleetOrdersScreen
import com.example.ui.screens.yantrafleet.FleetOverviewScreen
import com.example.ui.screens.yantrafleet.ManageEquipmentScreen
import com.example.ui.screens.yantrastore.EquipmentServiceScreen
import com.example.ui.screens.yantrastore.SparePartDetailScreen
import com.example.ui.screens.yantrastore.StoreMarketplaceScreen
import com.example.ui.screens.yantrastore.StoreOrdersScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.YantraBg

class MainActivity : ComponentActivity() {

    private val repository = YantraRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                YantraApp(repository = repository)
            }
        }
    }
}

sealed class SubScreen {
    object None : SubScreen()
    data class RentalBooking(val equipment: Equipment) : SubScreen()
    data class TrackBooking(val order: Order) : SubScreen()
    data class ManageEquipment(val equipment: Equipment) : SubScreen()
    data class SparePartDetail(val item: StoreItem) : SubScreen()
    data class EquipmentService(val item: StoreItem) : SubScreen()
}

@Composable
fun YantraApp(repository: YantraRepository) {
    val currentRole by repository.currentAppRole.collectAsStateWithLifecycle()
    val isLoggedIn by repository.isLoggedIn.collectAsStateWithLifecycle()
    val equipments by repository.equipments.collectAsStateWithLifecycle()
    val operators by repository.operators.collectAsStateWithLifecycle()
    val orders by repository.orders.collectAsStateWithLifecycle()
    val storeItems by repository.storeItems.collectAsStateWithLifecycle()
    val cart by repository.cart.collectAsStateWithLifecycle()
    val scheduledServices by repository.scheduledServices.collectAsStateWithLifecycle()
    val currentWorksite by repository.currentWorksite.collectAsStateWithLifecycle()
    val shoppingForEquipment by repository.shoppingForEquipment.collectAsStateWithLifecycle()

    // Navigation state
    var currentTab by remember(currentRole) {
        mutableStateOf(
            when (currentRole) {
                AppRole.FIND_YANTRA -> "explore"
                AppRole.YANTRA_FLEET -> "overview"
                AppRole.YANTRA_STORE -> "browse"
            }
        )
    }

    var subScreen by remember { mutableStateOf<SubScreen>(SubScreen.None) }

    // Dialogs
    var showAppSwitcher by remember { mutableStateOf(false) }
    var showAiAssistant by remember { mutableStateOf(false) }
    var showBackendDocs by remember { mutableStateOf(false) }
    var showWorksitePicker by remember { mutableStateOf(false) }
    var showMachineContextPicker by remember { mutableStateOf(false) }

    // Back handling
    BackHandler(enabled = subScreen !is SubScreen.None) {
        subScreen = SubScreen.None
    }

    if (!isLoggedIn) {
        AuthScreen(
            initialRole = currentRole,
            onLoginSuccess = { role ->
                repository.setAppRole(role)
                repository.setLoggedIn(true)
            }
        )
        return
    }

    Scaffold(
        topBar = {
            if (subScreen is SubScreen.None) {
                YantraTopBar(
                    currentRole = currentRole,
                    onAppSwitchClick = { showAppSwitcher = true },
                    onAiAssistantClick = { showAiAssistant = true },
                    onNotificationClick = { /* Notifications */ },
                    cartItemCount = cart.sumOf { it.quantity },
                    onCartClick = { currentTab = "my_orders" }
                )
            }
        },
        bottomBar = {
            if (subScreen is SubScreen.None) {
                YantraBottomNav(
                    currentRole = currentRole,
                    currentTab = currentTab,
                    onTabSelected = { tabId ->
                        if (tabId == "yantrastore" && currentRole != AppRole.YANTRA_STORE) {
                            // Directly transitions into the integrated YantraStore
                            repository.setAppRole(AppRole.YANTRA_STORE)
                            currentTab = "browse"
                        } else {
                            currentTab = tabId
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(YantraBg)
                .padding(innerPadding)
        ) {
            // Render sub-screens or tab content
            when (val screen = subScreen) {
                is SubScreen.RentalBooking -> {
                    RentalBookingScreen(
                        equipment = screen.equipment,
                        initialWorksite = currentWorksite,
                        onBackClick = { subScreen = SubScreen.None },
                        onConfirmBooking = { eq, rentalType, hours, dateStr, timeSlot, site ->
                            val newOrder = repository.createRentalBooking(
                                equipment = eq,
                                rentalType = rentalType,
                                hours = hours,
                                dateStr = dateStr,
                                timeSlot = timeSlot,
                                worksite = site
                            )
                            subScreen = SubScreen.TrackBooking(newOrder)
                        }
                    )
                }

                is SubScreen.TrackBooking -> {
                    TrackBookingScreen(
                        order = screen.order,
                        onBackClick = { subScreen = SubScreen.None }
                    )
                }

                is SubScreen.ManageEquipment -> {
                    ManageEquipmentScreen(
                        equipment = screen.equipment,
                        onBackClick = { subScreen = SubScreen.None },
                        onTogglePublish = { id, pub -> repository.togglePublishOnFindYantra(id, pub) },
                        onUpdateRates = { id, h, d -> repository.updateEquipmentRates(id, h, d) },
                        onScheduleServiceWithYantraStore = {
                            val serviceItem = storeItems.firstOrNull { it.isService }
                            if (serviceItem != null) {
                                subScreen = SubScreen.EquipmentService(serviceItem)
                            }
                        }
                    )
                }

                is SubScreen.SparePartDetail -> {
                    SparePartDetailScreen(
                        item = screen.item,
                        onBackClick = { subScreen = SubScreen.None },
                        onBuyNow = { item, qty ->
                            repository.addToCart(item)
                            subScreen = SubScreen.None
                            currentTab = "my_orders"
                        }
                    )
                }

                is SubScreen.EquipmentService -> {
                    EquipmentServiceScreen(
                        serviceItem = screen.item,
                        shoppingForEquipment = shoppingForEquipment,
                        currentWorksite = currentWorksite,
                        onBackClick = { subScreen = SubScreen.None },
                        onScheduleService = { svc, eqName, regNo, date, slot, loc, total ->
                            repository.scheduleService(svc, eqName, regNo, date, slot, loc, total)
                            subScreen = SubScreen.None
                            currentTab = "my_orders"
                        }
                    )
                }

                is SubScreen.None -> {
                    // Display current tab content based on active persona
                    when (currentRole) {
                        AppRole.FIND_YANTRA -> {
                            when (currentTab) {
                                "explore" -> {
                                    FindYantraExploreScreen(
                                        equipments = equipments,
                                        currentWorksite = currentWorksite,
                                        onWorksiteClick = { showWorksitePicker = true },
                                        onEquipmentClick = { eq ->
                                            subScreen = SubScreen.RentalBooking(eq)
                                        },
                                        onOpenYantraStore = {
                                            repository.setAppRole(AppRole.YANTRA_STORE)
                                            currentTab = "browse"
                                        }
                                    )
                                }
                                "bookings" -> {
                                    FindYantraBookingsScreen(
                                        orders = orders,
                                        onOrderClick = { ord ->
                                            subScreen = SubScreen.TrackBooking(ord)
                                        }
                                    )
                                }
                                "account" -> {
                                    AccountProfileScreen(
                                        currentRole = currentRole,
                                        onRoleSelected = { role ->
                                            repository.setAppRole(role)
                                        },
                                        onOpenDocs = { showBackendDocs = true },
                                        onSignOut = { repository.setLoggedIn(false) }
                                    )
                                }
                            }
                        }

                        AppRole.YANTRA_FLEET -> {
                            when (currentTab) {
                                "overview" -> {
                                    FleetOverviewScreen(
                                        equipments = equipments,
                                        orders = orders,
                                        onNavigateToOrders = { currentTab = "orders" },
                                        onNavigateToFleet = { currentTab = "fleet" },
                                        onNavigateToFindYantra = {
                                            repository.setAppRole(AppRole.FIND_YANTRA)
                                            currentTab = "explore"
                                        }
                                    )
                                }
                                "fleet" -> {
                                    FleetInventoryScreen(
                                        equipments = equipments,
                                        onEquipmentClick = { eq ->
                                            subScreen = SubScreen.ManageEquipment(eq)
                                        },
                                        onAddEquipment = { name, reg, cat, hp, hr, dr ->
                                            repository.addEquipment(name, reg, cat, hp, hr, dr)
                                        }
                                    )
                                }
                                "orders" -> {
                                    FleetOrdersScreen(
                                        orders = orders,
                                        onAssignOperator = { orderId, opId ->
                                            repository.assignOperatorToOrder(orderId, opId)
                                        },
                                        onOrderClick = { ord ->
                                            subScreen = SubScreen.TrackBooking(ord)
                                        }
                                    )
                                }
                                "team" -> {
                                    FleetOperatorsScreen(
                                        operators = operators,
                                        onAddOperator = { name, role, phone ->
                                            repository.addOperator(name, role, phone)
                                        }
                                    )
                                }
                            }
                        }

                        AppRole.YANTRA_STORE -> {
                            when (currentTab) {
                                "browse" -> {
                                    StoreMarketplaceScreen(
                                        storeItems = storeItems,
                                        shoppingForEquipment = shoppingForEquipment,
                                        currentWorksite = currentWorksite,
                                        onWorksiteClick = { showWorksitePicker = true },
                                        onChangeEquipmentContext = { showMachineContextPicker = true },
                                        onProductClick = { item ->
                                            subScreen = SubScreen.SparePartDetail(item)
                                        },
                                        onServiceClick = { svc ->
                                            subScreen = SubScreen.EquipmentService(svc)
                                        },
                                        onAddToCart = { item ->
                                            repository.addToCart(item)
                                        }
                                    )
                                }
                                "my_orders" -> {
                                    StoreOrdersScreen(
                                        cartItems = cart,
                                        scheduledServices = scheduledServices
                                    )
                                }
                                "services" -> {
                                    val svcItem = storeItems.firstOrNull { it.isService } ?: storeItems.first()
                                    EquipmentServiceScreen(
                                        serviceItem = svcItem,
                                        shoppingForEquipment = shoppingForEquipment,
                                        currentWorksite = currentWorksite,
                                        onBackClick = { currentTab = "browse" },
                                        onScheduleService = { svc, eqName, regNo, date, slot, loc, total ->
                                            repository.scheduleService(svc, eqName, regNo, date, slot, loc, total)
                                            currentTab = "my_orders"
                                        }
                                    )
                                }
                                "account" -> {
                                    AccountProfileScreen(
                                        currentRole = currentRole,
                                        onRoleSelected = { role ->
                                            repository.setAppRole(role)
                                        },
                                        onOpenDocs = { showBackendDocs = true },
                                        onSignOut = { repository.setLoggedIn(false) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Dialogs
    if (showAppSwitcher) {
        AppRoleSwitcherDialog(
            currentRole = currentRole,
            onRoleSelected = { newRole ->
                repository.setAppRole(newRole)
                subScreen = SubScreen.None
            },
            onOpenDocs = { showBackendDocs = true },
            onDismiss = { showAppSwitcher = false }
        )
    }

    if (showAiAssistant) {
        YantraAiAssistantDialog(
            onDismiss = { showAiAssistant = false },
            onBookMachineRecommendation = {
                showAiAssistant = false
                val jcb = equipments.firstOrNull { it.name.contains("JCB") } ?: equipments.first()
                subScreen = SubScreen.RentalBooking(jcb)
            }
        )
    }

    if (showBackendDocs) {
        BackendDocsDialog(
            onDismiss = { showBackendDocs = false }
        )
    }

    if (showWorksitePicker) {
        val worksiteOptions = listOf(
            "Hinjawadi Phase 1, Pune",
            "Wakad Bridge Site, Pune",
            "Baner Road Expansion, Pune",
            "Chakan MIDC Industrial Park, Pune",
            "Kothrud Metro Works, Pune"
        )
        AlertDialog(
            onDismissRequest = { showWorksitePicker = false },
            title = { Text("Select Worksite Location", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    worksiteOptions.forEach { site ->
                        Surface(
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                            color = if (currentWorksite == site) com.example.ui.theme.YantraPrimaryLight else androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    repository.updateWorksite(site)
                                    showWorksitePicker = false
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = site,
                                fontSize = 14.sp,
                                fontWeight = if (currentWorksite == site) FontWeight.Bold else FontWeight.Normal,
                                color = com.example.ui.theme.YantraTextPrimary,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showWorksitePicker = false }) {
                    Text("Close")
                }
            }
        )
    }

    if (showMachineContextPicker) {
        val machineContexts = equipments.map { "${it.name} · ${it.registrationNo}" }
        AlertDialog(
            onDismissRequest = { showMachineContextPicker = false },
            title = { Text("Shopping Fitment Machine", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    machineContexts.forEach { contextInfo ->
                        Surface(
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                            color = if (shoppingForEquipment == contextInfo) com.example.ui.theme.YantraPrimaryLight else androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    repository.setShoppingForEquipment(contextInfo)
                                    showMachineContextPicker = false
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = contextInfo,
                                fontSize = 13.sp,
                                fontWeight = if (shoppingForEquipment == contextInfo) FontWeight.Bold else FontWeight.Normal,
                                color = com.example.ui.theme.YantraTextPrimary,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMachineContextPicker = false }) {
                    Text("Close")
                }
            }
        )
    }
}
