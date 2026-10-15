package com.newafzal.solarpoint.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*

// ----------------------------------------------------
// Data Models (English)
// ----------------------------------------------------
data class BillItem(
    val id: String,
    val customerName: String,
    val phone: String,
    val itemType: String,
    val quantity: Int,
    val rate: Double,
    val totalAmount: Double,
    val paidAmount: Double,
    val isCredit: Boolean,
    val date: String
)

data class InventoryStockItem(
    val id: String,
    val name: String,
    val category: String, // Solar Panels, Batteries, Inverters, DC Cables & Accessories
    val stockQty: Int,
    val unitPrice: Double,
    val alertLevel: Int
)

data class CustomerKhataRecord(
    val id: String,
    val name: String,
    val city: String,
    val phone: String,
    val balanceDue: Double,
    val lastPaymentDate: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContainer() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Billing.route

    // Live In-Memory Billing State
    var billList by remember {
        mutableStateOf(
            listOf(
                BillItem("INV-101", "Chaudhry Tariq Mehmood", "0300-7861122", "Longi 585W Hi-MO 6 Solar (x12)", 12, 17500.0, 210000.0, 150000.0, true, "15 Oct 2026"),
                BillItem("INV-102", "Haji Farooq Hasilpur", "0301-5544332", "Osaka Tubular Battery TX-2500 (x2)", 2, 48000.0, 96000.0, 96000.0, false, "15 Oct 2026"),
                BillItem("INV-103", "Chaudhry Bashir Ahmed", "0302-9988776", "Inverex Veyron 3.2kW Inverter (x1)", 1, 145000.0, 145000.0, 100000.0, true, "14 Oct 2026")
            )
        )
    }

    // Live In-Memory Inventory Stock State (Add, Edit, Delete supported)
    var inventoryList by remember {
        mutableStateOf(
            listOf(
                InventoryStockItem("STK-1", "Longi 585W Hi-MO 6 Solar", "Solar Panels", 48, 17500.0, 10),
                InventoryStockItem("STK-2", "Osaka Tubular TX-2500 (Deep Cycle)", "Batteries", 14, 48000.0, 4),
                InventoryStockItem("STK-3", "Phoenix TX-1800 Tubular", "Batteries", 18, 38000.0, 4),
                InventoryStockItem("STK-4", "Inverex Nitrox 6kW Hybrid 48V", "Inverters", 5, 275000.0, 2),
                InventoryStockItem("STK-5", "Crown 3.2kW Solar Inverter", "Inverters", 8, 138000.0, 2),
                InventoryStockItem("STK-6", "Pakistan Cables 6mm DC Solar Wire (90m)", "DC Cables", 12, 28000.0, 3)
            )
        )
    }

    // Live Customer Khata / Ledger State (Add, Edit, Delete supported)
    var customerList by remember {
        mutableStateOf(
            listOf(
                CustomerKhataRecord("C-1", "Chaudhry Tariq Mehmood", "Hasilpur City", "0300-7861122", 60000.0, "15 Oct 2026"),
                CustomerKhataRecord("C-2", "Chaudhry Bashir Ahmed", "Vehari Road Chak 58", "0302-9988776", 45000.0, "14 Oct 2026"),
                CustomerKhataRecord("C-3", "Malik Waseem Solar Farm", "Choona Wala", "0301-4455667", 185000.0, "10 Oct 2026"),
                CustomerKhataRecord("C-4", "Rana Imtiaz Tubewell", "Fatehpur", "0304-1122334", 120000.0, "08 Oct 2026")
            )
        )
    }

    var showNewBillDialog by remember { mutableStateOf(false) }

    val bottomBarTabs = listOf(
        Screen.Billing,
        Screen.Inventory,
        Screen.Khata,
        Screen.Reports
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFD32F2F),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "New Afzal Battery Center & Solar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Vehari Road, Hasilpur • POS & ERP v1.0",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D47A1))
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                bottomBarTabs.forEach { tab ->
                    val selected = currentRoute == tab.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { tab.icon?.let { Icon(it, contentDescription = tab.title) } },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 11.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF0D47A1),
                            selectedTextColor = Color(0xFF0D47A1),
                            indicatorColor = Color(0xFFEFF3FF)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Billing.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Billing.route) {
                BillingWorkingScreen(
                    bills = billList,
                    onOpenNewEntry = { showNewBillDialog = true }
                )
            }
            composable(Screen.Inventory.route) {
                InventoryFullManagerScreen(
                    stockItems = inventoryList,
                    onAddItem = { newItem -> inventoryList = listOf(newItem) + inventoryList },
                    onUpdateItem = { updatedItem ->
                        inventoryList = inventoryList.map { if (it.id == updatedItem.id) updatedItem else it }
                    },
                    onDeleteItem = { id -> inventoryList = inventoryList.filter { it.id != id } }
                )
            }
            composable(Screen.Khata.route) {
                CustomerKhataFullManagerScreen(
                    customers = customerList,
                    onAddCustomer = { newCust -> customerList = listOf(newCust) + customerList },
                    onUpdateCustomer = { updated ->
                        customerList = customerList.map { if (it.id == updated.id) updated else it }
                    },
                    onDeleteCustomer = { id -> customerList = customerList.filter { it.id != id } }
                )
            }
            composable(Screen.Reports.route) {
                ReportsWorkingScreen(bills = billList, customers = customerList, stockItems = inventoryList)
            }
        }

        if (showNewBillDialog) {
            NewBillEntryDialog(
                availableStock = inventoryList,
                onDismiss = { showNewBillDialog = false },
                onSaveBill = { newBill ->
                    billList = listOf(newBill) + billList
                    showNewBillDialog = false
                }
            )
        }
    }
}

// ====================================================
// 1. POS & BILLING SCREEN (English)
// ====================================================
@Composable
fun BillingWorkingScreen(
    bills: List<BillItem>,
    onOpenNewEntry: () -> Unit
) {
    val totalSalesToday = bills.sumOf { it.totalAmount }
    val cashReceived = bills.sumOf { it.paidAmount }
    val creditBalance = totalSalesToday - cashReceived

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FF))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "POS & Sales Billing",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1)
                        )
                        Button(
                            onClick = onOpenNewEntry,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("New Invoice +", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = Color(0xFFEFF3FF))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SummaryMiniBox("Today's Gross Sales", "PKR ${String.format("%,.0f", totalSalesToday)}", Color(0xFF0D47A1))
                        SummaryMiniBox("Cash Received", "PKR ${String.format("%,.0f", cashReceived)}", Color(0xFF2E7D32))
                        SummaryMiniBox("Credit (Udhar)", "PKR ${String.format("%,.0f", creditBalance)}", Color(0xFFD32F2F))
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Sales Invoices",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = "${bills.size} records",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        items(bills) { bill ->
            val due = bill.totalAmount - bill.paidAmount
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = bill.customerName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0D47A1))
                            Text(text = "${bill.id} • ${bill.date}", fontSize = 11.sp, color = Color.Gray)
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (due > 0) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = if (due > 0) "Credit Due: PKR ${String.format("%,.0f", due)}" else "Paid in Full",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (due > 0) Color(0xFFD32F2F) else Color(0xFF2E7D32)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Items: ${bill.itemType}", fontSize = 13.sp, color = Color(0xFF334155))
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Cash Paid: PKR ${String.format("%,.0f", bill.paidAmount)}", fontSize = 12.sp, color = Color(0xFF2E7D32))
                        Text(text = "Total Bill: PKR ${String.format("%,.0f", bill.totalAmount)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                    }
                }
            }
        }
    }
}

// ====================================================
// 2. NEW INVOICE DIALOG (Interactive Entry)
// ====================================================
@Composable
fun NewBillEntryDialog(
    availableStock: List<InventoryStockItem>,
    onDismiss: () -> Unit,
    onSaveBill: (BillItem) -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var selectedStockItem by remember { mutableStateOf(availableStock.firstOrNull()) }
    var quantityText by remember { mutableStateOf("1") }
    var rateText by remember { mutableStateOf(selectedStockItem?.unitPrice?.toInt()?.toString() ?: "17500") }
    var paidAmountText by remember { mutableStateOf("") }

    val qty = quantityText.toIntOrNull() ?: 1
    val rate = rateText.toDoubleOrNull() ?: 0.0
    val totalBill = qty * rate
    val paid = paidAmountText.toDoubleOrNull() ?: totalBill
    val balanceDue = (totalBill - paid).coerceAtLeast(0.0)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Create New Invoice", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                    Divider(color = Color(0xFFEFF3FF))
                }

                item {
                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text("Customer Name *") },
                        placeholder = { Text("e.g. Haji Muhammad Tariq") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                item {
                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = { customerPhone = it },
                        label = { Text("Phone / WhatsApp") },
                        placeholder = { Text("0300-1234567") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                item {
                    Text("Select Item from Inventory:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF0D47A1))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        availableStock.take(5).forEach { stk ->
                            val isChosen = selectedStockItem?.id == stk.id
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isChosen) Color(0xFFEFF3FF) else Color.Transparent,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedStockItem = stk
                                        rateText = stk.unitPrice.toInt().toString()
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(stk.name, fontSize = 12.sp, color = if (isChosen) Color(0xFF0D47A1) else Color.Black)
                                    Text("PKR ${stk.unitPrice.toInt()}", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = quantityText,
                            onValueChange = { quantityText = it },
                            label = { Text("Quantity") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = rateText,
                            onValueChange = { rateText = it },
                            label = { Text("Unit Price (PKR)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1.5f),
                            singleLine = true
                        )
                    }
                }

                item {
                    Surface(color = Color(0xFFEFF3FF), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Invoice Amount:", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                            Text("PKR ${String.format("%,.0f", totalBill)}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF0D47A1))
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = paidAmountText,
                        onValueChange = { paidAmountText = it },
                        label = { Text("Cash Received (Leave empty if full)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                if (balanceDue > 0) {
                    item {
                        Surface(color = Color(0xFFFFEBEE), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Remaining Udhar / Credit: PKR ${String.format("%,.0f", balanceDue)}",
                                color = Color(0xFFD32F2F),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = {
                            val newBill = BillItem(
                                id = "INV-${(100..999).random()}",
                                customerName = customerName.ifBlank { "Walk-in Customer" },
                                phone = customerPhone.ifBlank { "0300-XXXXXXX" },
                                itemType = "${selectedStockItem?.name ?: "Solar Item"} (x$qty)",
                                quantity = qty,
                                rate = rate,
                                totalAmount = totalBill,
                                paidAmount = if (paidAmountText.isBlank()) totalBill else (paidAmountText.toDoubleOrNull() ?: totalBill),
                                isCredit = balanceDue > 0,
                                date = "15 Oct 2026"
                            )
                            onSaveBill(newBill)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save & Print Invoice", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ====================================================
// 3. INVENTORY STOCK MANAGEMENT SCREEN (English)
// ====================================================
@Composable
fun InventoryFullManagerScreen(
    stockItems: List<InventoryStockItem>,
    onAddItem: (InventoryStockItem) -> Unit,
    onUpdateItem: (InventoryStockItem) -> Unit,
    onDeleteItem: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var itemToEdit by remember { mutableStateOf<InventoryStockItem?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<InventoryStockItem?>(null) }

    val categories = listOf("All", "Solar Panels", "Batteries", "Inverters", "DC Cables")

    val filteredItems = stockItems.filter { item ->
        val matchesSearch = item.name.contains(searchQuery, ignoreCase = true) || item.category.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "All" || item.category == selectedCategory
        matchesSearch && matchesCategory
    }

    val totalStockUnits = stockItems.sumOf { it.stockQty }
    val totalStockValuation = stockItems.sumOf { it.stockQty * it.unitPrice }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FF))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Inventory & Warehouse", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                        Button(
                            onClick = { showAddDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.AddBox, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add Product +", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = Color(0xFFEFF3FF))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Stock Valuation", fontSize = 11.sp, color = Color.Gray)
                            Text("PKR ${String.format("%,.0f", totalStockValuation)}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFEFF3FF)
                        ) {
                            Text(
                                text = "$totalStockUnits Units in Stock",
                                color = Color(0xFF0D47A1),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by model, brand or category...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF0D47A1)) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = null)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0D47A1),
                    unfocusedBorderColor = Color(0xFFCBD5E1),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = cat == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF0D47A1),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        if (filteredItems.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No items found. Tap 'Add Product +' to insert items.", color = Color.Gray, fontSize = 14.sp)
                }
            }
        }

        items(filteredItems) { stock ->
            val isLowStock = stock.stockQty <= stock.alertLevel
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(text = stock.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0D47A1))
                            Text(text = "${stock.category} • Unit Price: PKR ${String.format("%,.0f", stock.unitPrice)}", fontSize = 12.sp, color = Color(0xFF475569))
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isLowStock) Color(0xFFFFEBEE) else Color(0xFFEFF3FF)
                        ) {
                            Text(
                                text = "${stock.stockQty} in stock",
                                color = if (isLowStock) Color(0xFFD32F2F) else Color(0xFF0D47A1),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isLowStock) "⚠️ Low Stock Alert!" else "Total Value: PKR ${String.format("%,.0f", stock.stockQty * stock.unitPrice)}",
                            fontSize = 11.sp,
                            fontWeight = if (isLowStock) FontWeight.Bold else FontWeight.Normal,
                            color = if (isLowStock) Color(0xFFD32F2F) else Color.Gray
                        )

                        Row {
                            OutlinedButton(
                                onClick = { itemToDelete = stock },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Delete", fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { itemToEdit = stock },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Edit", fontSize = 12.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        InventoryEditDialog(
            title = "Add New Item to Stock",
            initialItem = null,
            onDismiss = { showAddDialog = false },
            onSave = { newItem ->
                onAddItem(newItem)
                showAddDialog = false
            }
        )
    }

    itemToEdit?.let { item ->
        InventoryEditDialog(
            title = "Edit Stock Details",
            initialItem = item,
            onDismiss = { itemToEdit = null },
            onSave = { updatedItem ->
                onUpdateItem(updatedItem)
                itemToEdit = null
            }
        )
    }

    itemToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text("Remove from Inventory?", fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F)) },
            text = { Text("Are you sure you want to delete ${item.name} from the active inventory list?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteItem(item.id)
                        itemToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun InventoryEditDialog(
    title: String,
    initialItem: InventoryStockItem?,
    onDismiss: () -> Unit,
    onSave: (InventoryStockItem) -> Unit
) {
    var name by remember { mutableStateOf(initialItem?.name ?: "") }
    var category by remember { mutableStateOf(initialItem?.category ?: "Solar Panels") }
    var qtyText by remember { mutableStateOf(initialItem?.stockQty?.toString() ?: "10") }
    var priceText by remember { mutableStateOf(initialItem?.unitPrice?.toInt()?.toString() ?: "17500") }
    var alertText by remember { mutableStateOf(initialItem?.alertLevel?.toString() ?: "3") }

    val categories = listOf("Solar Panels", "Batteries", "Inverters", "DC Cables")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
                Divider(color = Color(0xFFEFF3FF))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item Name & Model *") },
                    placeholder = { Text("e.g. Longi 585W Solar") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("Category:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = cat == category
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFF0D47A1) else Color(0xFFEFF3FF),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { category = cat }
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 6.dp)) {
                                Text(cat, fontSize = 10.sp, color = if (isSelected) Color.White else Color.Black, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = qtyText,
                        onValueChange = { qtyText = it },
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Price (PKR)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1.3f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = alertText,
                    onValueChange = { alertText = it },
                    label = { Text("Low Stock Alert Threshold") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = {
                        val qty = qtyText.toIntOrNull() ?: 1
                        val price = priceText.toDoubleOrNull() ?: 0.0
                        val alert = alertText.toIntOrNull() ?: 2
                        val newRecord = InventoryStockItem(
                            id = initialItem?.id ?: "STK-${(100..999).random()}",
                            name = name.ifBlank { "New Product" },
                            category = category,
                            stockQty = qty,
                            unitPrice = price,
                            alertLevel = alert
                        )
                        onSave(newRecord)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save to Inventory", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ====================================================
// 4. CUSTOMER KHATA / LEDGER SCREEN (English)
// ====================================================
@Composable
fun CustomerKhataFullManagerScreen(
    customers: List<CustomerKhataRecord>,
    onAddCustomer: (CustomerKhataRecord) -> Unit,
    onUpdateCustomer: (CustomerKhataRecord) -> Unit,
    onDeleteCustomer: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var customerToEdit by remember { mutableStateOf<CustomerKhataRecord?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var customerToDelete by remember { mutableStateOf<CustomerKhataRecord?>(null) }

    val filteredCustomers = customers.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.phone.contains(searchQuery) ||
                it.city.contains(searchQuery, ignoreCase = true)
    }

    val totalUdhar = customers.sumOf { it.balanceDue }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FF))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Customer Ledger / Khata", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                        Button(
                            onClick = { showAddDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("New Customer +", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = Color(0xFFEFF3FF))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Outstanding Credit Due", fontSize = 11.sp, color = Color.Gray)
                            Text("PKR ${String.format("%,.0f", totalUdhar)}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFEFF3FF)
                        ) {
                            Text(
                                text = "${customers.size} Accounts Registered",
                                color = Color(0xFF0D47A1),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by name, phone or address...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF0D47A1)) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = null)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0D47A1),
                    unfocusedBorderColor = Color(0xFFCBD5E1),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
        }

        items(filteredCustomers) { cust ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(text = cust.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF0D47A1))
                            Text(text = "${cust.city} • ${cust.phone}", fontSize = 12.sp, color = Color(0xFF475569))
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "PKR ${String.format("%,.0f", cust.balanceDue)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFFD32F2F)
                            )
                            Text(text = "Last Entry: ${cust.lastPaymentDate}", fontSize = 11.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { customerToDelete = cust },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Delete", fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { customerToEdit = cust },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        CustomerEditDialog(
            title = "Register New Customer Account",
            initialCustomer = null,
            onDismiss = { showAddDialog = false },
            onSave = { newCust ->
                onAddCustomer(newCust)
                showAddDialog = false
            }
        )
    }

    customerToEdit?.let { cust ->
        CustomerEditDialog(
            title = "Edit Customer Account",
            initialCustomer = cust,
            onDismiss = { customerToEdit = null },
            onSave = { updatedCust ->
                onUpdateCustomer(updatedCust)
                customerToEdit = null
            }
        )
    }

    customerToDelete?.let { cust ->
        AlertDialog(
            onDismissRequest = { customerToDelete = null },
            title = { Text("Delete Customer Account?", fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F)) },
            text = { Text("Are you sure you want to delete ${cust.name} (${cust.phone}) and their khata history?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteCustomer(cust.id)
                        customerToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { customerToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CustomerEditDialog(
    title: String,
    initialCustomer: CustomerKhataRecord?,
    onDismiss: () -> Unit,
    onSave: (CustomerKhataRecord) -> Unit
) {
    var name by remember { mutableStateOf(initialCustomer?.name ?: "") }
    var city by remember { mutableStateOf(initialCustomer?.city ?: "Hasilpur") }
    var phone by remember { mutableStateOf(initialCustomer?.phone ?: "") }
    var balanceText by remember { mutableStateOf(initialCustomer?.balanceDue?.toInt()?.toString() ?: "0") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
                Divider(color = Color(0xFFEFF3FF))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Customer Full Name *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number (WhatsApp)") },
                    placeholder = { Text("0300-1234567") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City / Area / Chak No.") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = balanceText,
                    onValueChange = { balanceText = it },
                    label = { Text("Opening Credit Balance (PKR)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = {
                        val amount = balanceText.toDoubleOrNull() ?: 0.0
                        val newRecord = CustomerKhataRecord(
                            id = initialCustomer?.id ?: "C-${(100..999).random()}",
                            name = name.ifBlank { "Customer" },
                            city = city.ifBlank { "Hasilpur" },
                            phone = phone.ifBlank { "0300-XXXXXXX" },
                            balanceDue = amount,
                            lastPaymentDate = "15 Oct 2026"
                        )
                        onSave(newRecord)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Customer", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ====================================================
// 5. REPORTS & ANALYTICS SCREEN (English)
// ====================================================
@Composable
fun ReportsWorkingScreen(
    bills: List<BillItem>,
    customers: List<CustomerKhataRecord>,
    stockItems: List<InventoryStockItem>
) {
    val totalSales = bills.sumOf { it.totalAmount }
    val cashInHand = bills.sumOf { it.paidAmount }
    val marketUdhar = customers.sumOf { it.balanceDue }
    val totalStockValuation = stockItems.sumOf { it.stockQty * it.unitPrice }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FF))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Financial Analytics & Profit Summary", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                    Text("New Afzal Battery Center & Solar Point", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(14.dp))

                    ReportDataRow("Total Sales Revenue", "PKR ${String.format("%,.0f", totalSales)}", Color(0xFF0D47A1))
                    ReportDataRow("Cash In Hand Received", "PKR ${String.format("%,.0f", cashInHand)}", Color(0xFF2E7D32))
                    ReportDataRow("Customer Market Credit (Udhar)", "PKR ${String.format("%,.0f", marketUdhar)}", Color(0xFFD32F2F))
                    ReportDataRow("Current Inventory Valuation", "PKR ${String.format("%,.0f", totalStockValuation)}", Color(0xFF0D47A1))

                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFEFF3FF))

                    ReportDataRow("Estimated Gross Profit (12%)", "PKR ${String.format("%,.0f", totalSales * 0.12)}", Color(0xFF2E7D32), isBold = true)
                }
            }
        }
    }
}

@Composable
fun ReportDataRow(title: String, amount: String, color: Color, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontSize = if (isBold) 14.sp else 12.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal, color = Color(0xFF334155))
        Text(amount, fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold, fontSize = if (isBold) 15.sp else 13.sp, color = color)
    }
}

@Composable
fun SummaryMiniBox(title: String, value: String, color: Color) {
    Column {
        Text(text = title, fontSize = 11.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
