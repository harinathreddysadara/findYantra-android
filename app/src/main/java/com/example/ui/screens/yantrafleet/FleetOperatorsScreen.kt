package com.example.ui.screens.yantrafleet

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Operator
import com.example.model.OperatorStatus
import com.example.ui.theme.*

@Composable
fun FleetOperatorsScreen(
    operators: List<Operator>,
    onAddOperator: (String, String, String) -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<OperatorStatus?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredList = remember(operators, searchQuery, selectedFilter) {
        operators.filter { op ->
            (searchQuery.isEmpty() || op.name.contains(searchQuery, ignoreCase = true) || op.role.contains(searchQuery, ignoreCase = true)) &&
            (selectedFilter == null || op.status == selectedFilter)
        }
    }

    val onDutyCount = operators.count { it.status == OperatorStatus.ON_DUTY }
    val availableCount = operators.count { it.status == OperatorStatus.AVAILABLE }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = YantraPrimary,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.PersonAdd, contentDescription = "Add") },
                text = { Text("Add operator or driver", fontWeight = FontWeight.Bold) },
                modifier = Modifier.padding(bottom = 60.dp)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(YantraBg)
                .padding(padding),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Header
            item {
                Surface(
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Operators & drivers",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = YantraTextPrimary
                                )
                                Text(
                                    text = "${operators.size} team members · $onDutyCount on duty today",
                                    fontSize = 13.sp,
                                    color = YantraTextSecondary
                                )
                            }
                            IconButton(onClick = { showAddDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = "Add",
                                    tint = YantraPrimary
                                )
                            }
                        }
                    }
                }
                HorizontalDivider(color = YantraBorder)
            }

            // Search bar
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search name or licence") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = YantraTextSecondary)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )
                }
            }

            // Filter Pills (All 6, On duty 4, Available 1)
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        OperatorFilterPill(
                            label = "All ${operators.size}",
                            isSelected = selectedFilter == null,
                            onClick = { selectedFilter = null }
                        )
                    }
                    item {
                        OperatorFilterPill(
                            label = "On duty $onDutyCount",
                            isSelected = selectedFilter == OperatorStatus.ON_DUTY,
                            onClick = { selectedFilter = OperatorStatus.ON_DUTY }
                        )
                    }
                    item {
                        OperatorFilterPill(
                            label = "Available $availableCount",
                            isSelected = selectedFilter == OperatorStatus.AVAILABLE,
                            onClick = { selectedFilter = OperatorStatus.AVAILABLE }
                        )
                    }
                }
            }

            // Compliance green notice matching Mock 8
            item {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFDCFCE7),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF15803D),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "All ${operators.size} driving licences are up to date",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF15803D)
                        )
                    }
                }
            }

            // Operator cards
            items(filteredList) { op ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)) {
                    OperatorCard(
                        operator = op,
                        onCallClick = {
                            Toast.makeText(context, "Calling ${op.name} (${op.phone})...", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddOperatorDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, role, phone ->
                onAddOperator(name, role, phone)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun OperatorCard(
    operator: Operator,
    onCallClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, YantraBorder, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = YantraPrimaryLight,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = operator.initials,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = YantraPrimaryDark
                            )
                        }
                    }

                    Column {
                        Text(
                            text = operator.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = YantraTextPrimary
                        )
                        Text(
                            text = operator.role,
                            fontSize = 12.sp,
                            color = YantraTextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = when (operator.status) {
                        OperatorStatus.ON_DUTY -> Color(0xFFE0F2FE)
                        OperatorStatus.AVAILABLE -> Color(0xFFDCFCE7)
                        OperatorStatus.OFF_DUTY -> Color(0xFFF1F5F9)
                    }
                ) {
                    Text(
                        text = operator.status.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (operator.status) {
                            OperatorStatus.ON_DUTY -> Color(0xFF0369A1)
                            OperatorStatus.AVAILABLE -> Color(0xFF15803D)
                            OperatorStatus.OFF_DUTY -> YantraTextSecondary
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Assigned vehicle or status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    if (operator.assignedEquipmentName != null) {
                        Text(
                            text = operator.assignedEquipmentName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = YantraTextPrimary
                        )
                    } else {
                        Text(
                            text = "No assignment",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = YantraTextSecondary
                        )
                    }
                    Text(
                        text = operator.locationDesc,
                        fontSize = 11.sp,
                        color = YantraTextSecondary
                    )
                }

                IconButton(
                    onClick = onCallClick,
                    modifier = Modifier
                        .size(36.dp)
                        .background(YantraPrimaryLight, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Call",
                        tint = YantraPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun OperatorFilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) YantraHeaderDark else Color.White,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, if (isSelected) YantraHeaderDark else YantraBorder, RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else YantraTextSecondary,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun AddOperatorDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Backhoe operator") }
    var phone by remember { mutableStateOf("+91 98220 55100") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add Operator or Driver", fontWeight = FontWeight.Bold, color = YantraTextPrimary)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Operator Full Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Role (e.g. Excavator / Tipper driver)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Contact Phone") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onAdd(name, role, phone)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = YantraPrimary)
            ) {
                Text("Add Operator")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
