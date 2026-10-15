package com.newafzal.solarpoint.navigation

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

data class ProductItem(
    val id: String,
    val name: String,
    val category: String,
    val brand: String,
    val stockQty: Int,
    val unitPrice: Double,
    val alertLevel: Int,
    val spec: String
)

data class BillRecord(
    val id: String,
    val customerName: String,
    val phone: String,
    val itemName: String,
    val qty: Int,
    val rate: Double,
    val total: Double,
    val paid: Double,
    val date: String
)

data class CustomerAccount(
    val id: String,
    val name: String,
    val phone: String,
    val area: String,
    val balanceDue: Double,
    val lastEntry: String
)

data class SupplierRecord(
    val id: String,
    val companyName: String,
    val brand: String,
    val phone: String,
    val balancePayable: Double,
    val city: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContainer() {
    val context = LocalContext.current
    var isAuthenticated by remember { mutableStateOf(false) }
    var currentTab by remember { mutableStateOf("billing") }

    var stockList by remember {
        mutableStateOf(
            listOf(
                ProductItem("P-1", "Longi 585W Hi-MO 6 Explorer", "Solar Panels", "Longi", 54, 18500.0, 10, "Mono HPBC Cell • 22.8% Eff"),
                ProductItem("P-2", "Jinko 580W Tiger Neo N-Type", "Solar Panels", "Jinko", 38, 18300.0, 10, "Dual Glass SMBB • TopCon"),
                ProductItem("P-3", "CanadianSolar 600W Bi-Facial", "Solar Panels", "Canadian", 6, 19800.0, 8, "TOPHiKu6 High Gain Dual Side"),
                ProductItem("P-4", "Inverex Nitrox 6kW Hybrid 48V", "Inverters & VFD", "Inverex", 11, 365000.0, 3, "Dual MPPT • IP65 Touch Screen"),
                ProductItem("P-5", "INVT 15kW / 20HP Tubewell VFD", "Inverters & VFD", "INVT", 9, 118000.0, 2, "Solar Pumping GD100 Auto MPPT"),
                ProductItem("P-6", "Crown 3.2kW Solar Inverter", "Inverters & VFD", "Crown", 14, 142000.0, 3, "Pure Sine Wave 80A MPPT"),
                ProductItem("P-7", "Osaka Tubular TX-2500 Deep Cycle", "Batteries", "Osaka", 22, 68500.0, 5, "230Ah Heavy Duty Tubular Plate"),
                ProductItem("P-8", "Phoenix TX-1800 Tubular", "Batteries", "Phoenix", 18, 54000.0, 4, "165Ah Deep Cycle Solar Spec"),
                ProductItem("P-9", "Mora Digital Voltage Relay 63A", "Breakers & Relays", "Mora", 75, 2450.0, 15, "Dual Display Volt/Amp Meter"),
                ProductItem("P-10", "Tomzn 63A 2P DC Breaker 500V", "Breakers & Relays", "Tomzn", 120, 1850.0, 20, "Curve C PV String Isolator"),
                ProductItem("P-11", "Tesla 63A 4P AC Main Breaker", "Breakers & Relays", "Tesla", 45, 3200.0, 10, "10kA Industrial Safety Breaker"),
                ProductItem("P-12", "Tomzn Surge Protector (SPD 2P)", "Breakers & Relays", "Tomzn", 60, 2100.0, 10, "DC 1000V 20-40kA Lightning Arrester")
            )
        )
    }

    var billList by remember {
        mutableStateOf(
            listOf(
                BillRecord("INV-101", "Chaudhry Tariq Mehmood", "0300-7861122", "Longi 585W (x12)", 12, 17500.0, 210000.0, 150000.0, "15 Oct 2026"),
                BillRecord("INV-102", "Haji Farooq Hasilpur", "0301-5544332", "Osaka TX-2500 (x2)", 2, 48000.0, 96000.0, 96000.0, "15 Oct 2026"),
                BillRecord("INV-103", "Chaudhry Bashir Ahmed", "0302-9988776", "Inverex Nitrox 6kW", 1, 365000.0, 365000.0, 270000.0, "14 Oct 2026")
            )
        )
    }

    var customerList by remember {
        mutableStateOf(
            listOf(
                CustomerAccount("C-1", "Chaudhry Tariq Mehmood", "0300-7861122", "Hasilpur City", 60000.0, "15 Oct 2026"),
                CustomerAccount("C-2", "Chaudhry Bashir Ahmed", "0302-9988776", "Vehari Road Chak 58", 95000.0, "14 Oct 2026"),
                CustomerAccount("C-3", "Malik Waseem Solar Farm", "0301-4455667", "Choona Wala Road", 185000.0, "10 Oct 2026"),
                CustomerAccount("C-4", "Rana Imtiaz Tubewell", "0304-1122334", "Fatehpur Mor", 120000.0, "08 Oct 2026")
            )
        )
    }

    var supplierList by remember {
        mutableStateOf(
            listOf(
                SupplierRecord("S-1", "Inverex Solar Energy Head Office", "Inverex", "042-111468373", 1450000.0, "Lahore"),
                SupplierRecord("S-2", "Osaka & Phoenix Main Warehouse", "Osaka Battery", "021-32415500", 820000.0, "Multan"),
                SupplierRecord("S-3", "Tomzn & Mora Breakers Importer", "Tomzn/Mora", "0321-8899112", 340000.0, "Karachi")
            )
        )
    }

    var showNewInvoiceDialog by remember { mutableStateOf(false) }
    var showAddProductDialog by remember { mutableStateOf(false) }
    var productToEdit by remember { mutableStateOf<ProductItem?>(null) }
    var productToDelete by remember { mutableStateOf<ProductItem?>(null) }
    var showAddCustomerDialog by remember { mutableStateOf(false) }
    var customerToEdit by remember { mutableStateOf<CustomerAccount?>(null) }
    var customerToDelete by remember { mutableStateOf<CustomerAccount?>(null) }

    if (!isAuthenticated) {
        LoginTerminalScreen(onLoginSuccess = { isAuthenticated = true })
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFD32F2F),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Bolt, contentDescription = "Logo", tint = Color.White)
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
                                text = "Vehari Road, Hasilpur • POS & ERP v2.0",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { Toast.makeText(context, "Cloud sync & database updated!", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                    }
                    IconButton(onClick = { isAuthenticated = false }) {
                        Icon(Icons.Default.Lock, contentDescription = "Lock", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D47A1))
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                val tabs = listOf(
                    Triple("billing", "POS Billing", Icons.Default.PointOfSale),
                    Triple("inventory", "Inventory", Icons.Default.Inventory2),
                    Triple("khata", "Customer Khata", Icons.Default.MenuBook),
                    Triple("reports", "Reports & B2B", Icons.Default.Assessment)
                )
                tabs.forEach { (tabKey, title, icon) ->
                    val isSelected = currentTab == tabKey
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tabKey },
                        icon = { Icon(icon, contentDescription = title) },
                        label = { Text(title, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF0D47A1),
                            selectedTextColor = Color(0xFF0D47A1),
                            indicatorColor = Color(0xFFEFF3FF)
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            when (currentTab) {
                "billing" -> FloatingActionButton(onClick = { showNewInvoiceDialog = true }, containerColor = Color(0xFF0D47A1), contentColor = Color.White) {
                    Icon(Icons.Default.Add, contentDescription = "New Bill")
                }
                "inventory" -> FloatingActionButton(onClick = { showAddProductDialog = true }, containerColor = Color(0xFF0D47A1), contentColor = Color.White) {
                    Icon(Icons.Default.AddBox, contentDescription = "Add Product")
                }
                "khata" -> FloatingActionButton(onClick = { showAddCustomerDialog = true }, containerColor = Color(0xFF0D47A1), contentColor = Color.White) {
                    Icon(Icons.Default.PersonAdd, contentDescription = "Add Customer")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentTab) {
                "billing" -> BillingTabScreen(bills = billList, onNewBill = { showNewInvoiceDialog = true })
                "inventory" -> InventoryTabScreen(stockItems = stockList, onEdit = { productToEdit = it }, onDelete = { productToDelete = it })
                "khata" -> CustomerKhataTabScreen(customers = customerList, onEdit = { customerToEdit = it }, onDelete = { customerToDelete = it })
                "reports" -> ReportsAndSuppliersTabScreen(bills = billList, customers = customerList, suppliers = supplierList, stockItems = stockList)
            }
        }

        if (showNewInvoiceDialog) {
            NewInvoiceDialog(
                availableProducts = stockList,
                onDismiss = { showNewInvoiceDialog = false },
                onSave = { bill ->
                    billList = listOf(bill) + billList
                    showNewInvoiceDialog = false
                    Toast.makeText(context, "Invoice ${bill.id} created & saved!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        if (showAddProductDialog) {
            ProductEditDialog(
                title = "Add New Solar Product / Breaker",
                initialProduct = null,
                onDismiss = { showAddProductDialog = false },
                onSave = { newProd ->
                    stockList = listOf(newProd) + stockList
                    showAddProductDialog = false
                    Toast.makeText(context, "${newProd.name} added to inventory!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        productToEdit?.let { prod ->
            ProductEditDialog(
                title = "Edit Product Details",
                initialProduct = prod,
                onDismiss = { productToEdit = null },
                onSave = { updated ->
                    stockList = stockList.map { if (it.id == updated.id) updated else it }
                    productToEdit = null
                    Toast.makeText(context, "${updated.name} updated!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        productToDelete?.let { prod ->
            AlertDialog(
                onDismissRequest = { productToDelete = null },
                title = { Text("Delete Product?", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold) },
                text = { Text("Are you sure you want to delete ${prod.name} from inventory?") },
                confirmButton = {
                    Button(
                        onClick = {
                            stockList = stockList.filter { it.id != prod.id }
                            productToDelete = null
                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { productToDelete = null }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (showAddCustomerDialog) {
            CustomerEditDialog(
                title = "Register New Customer Account",
                initialCustomer = null,
                onDismiss = { showAddCustomerDialog = false },
                onSave = { newCust ->
                    customerList = listOf(newCust) + customerList
                    showAddCustomerDialog = false
                    Toast.makeText(context, "Customer ${newCust.name} registered!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        customerToEdit?.let { cust ->
            CustomerEditDialog(
                title = "Edit Customer Account",
                initialCustomer = cust,
                onDismiss = { customerToEdit = null },
                onSave = { updated ->
                    customerList = customerList.map { if (it.id == updated.id) updated else it }
                    customerToEdit = null
                    Toast.makeText(context, "Customer updated!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        customerToDelete?.let { cust ->
            AlertDialog(
                onDismissRequest = { customerToDelete = null },
                title = { Text("Delete Account?", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold) },
                text = { Text("Delete ${cust.name} and their transaction history?") },
                confirmButton = {
                    Button(
                        onClick = {
                            customerList = customerList.filter { it.id != cust.id }
                            customerToDelete = null
                            Toast.makeText(context, "Account removed", Toast.LENGTH_SHORT).show()
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
}

@Composable
fun LoginTerminalScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("afzal") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FF))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF0D47A1),
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                }
                Text("New Afzal Solar Point", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                Text("Vehari Road, Hasilpur • Terminal Login", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF0D47A1)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; errorMessage = "" },
                    label = { Text("Password (afzal0300)") },
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF0D47A1)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = Color(0xFFD32F2F), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {
                        if (password == "afzal0300" || password == "admin") {
                            onLoginSuccess()
                        } else {
                            errorMessage = "Incorrect Password! Default: afzal0300"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Login to ERP System", fontWeight = FontWeight.Bold, color = Color.White)
                }
                Divider(color = Color(0xFFEFF3FF))
                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "Biometric Verified: Afzal Admin", Toast.LENGTH_SHORT).show()
                        onLoginSuccess()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0D47A1)),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.Fingerprint, contentDescription = null, tint = Color(0xFF0D47A1))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Quick Biometric Login", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun BillingTabScreen(bills: List<BillRecord>, onNewBill: () -> Unit) {
    val totalSales = bills.sumOf { it.total }
    val cashReceived = bills.sumOf { it.paid }
    val creditDue = totalSales - cashReceived

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FF))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("POS & Sales Billing", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1), fontSize = 16.sp)
                        Button(
                            onClick = onNewBill,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("New Invoice +", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = Color(0xFFEFF3FF))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        KPIBox("Gross Sales", "PKR ${String.format("%,.0f", totalSales)}", Color(0xFF0D47A1))
                        KPIBox("Cash Received", "PKR ${String.format("%,.0f", cashReceived)}", Color(0xFF2E7D32))
                        KPIBox("Credit (Udhar)", "PKR ${String.format("%,.0f", creditDue)}", Color(0xFFD32F2F))
                    }
                }
            }
        }
        item {
            Text("Recent Invoices (${bills.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
        }
        items(bills) { bill ->
            val due = bill.total - bill.paid
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(bill.customerName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0D47A1))
                            Text("${bill.id} • ${bill.date} • ${bill.phone}", fontSize = 11.sp, color = Color.Gray)
                        }
                        Surface(shape = RoundedCornerShape(4.dp), color = if (due > 0) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)) {
                            Text(
                                if (due > 0) "Credit: PKR ${String.format("%,.0f", due)}" else "Paid in Full",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (due > 0) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Item: ${bill.itemName}", fontSize = 12.sp, color = Color(0xFF334155))
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Paid: PKR ${String.format("%,.0f", bill.paid)}", fontSize = 12.sp, color = Color(0xFF2E7D32))
                        Text("Total: PKR ${String.format("%,.0f", bill.total)}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0D47A1))
                    }
                }
            }
        }
    }
}

@Composable
fun InventoryTabScreen(stockItems: List<ProductItem>, onEdit: (ProductItem) -> Unit, onDelete: (ProductItem) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Solar Panels", "Inverters & VFD", "Batteries", "Breakers & Relays")

    val filteredItems = stockItems.filter { item ->
        val matchesSearch = item.name.contains(searchQuery, ignoreCase = true) || item.brand.contains(searchQuery, ignoreCase = true) || item.spec.contains(searchQuery, ignoreCase = true)
        val matchesCat = selectedCategory == "All" || item.category == selectedCategory
        matchesSearch && matchesCat
    }

    val totalValuation = stockItems.sumOf { it.stockQty * it.unitPrice }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FF))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Inventory Valuation", fontSize = 11.sp, color = Color.Gray)
                            Text("PKR ${String.format("%,.0f", totalValuation)}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                        }
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFEFF3FF)) {
                            Text("${stockItems.sumOf { it.stockQty }} Units In Stock", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                }
            }
        }
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search 63A Breaker, Tomzn, Tesla, Mora, Inverters...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF0D47A1)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = cat == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF0D47A1), selectedLabelColor = Color.White)
                    )
                }
            }
        }
        items(filteredItems) { prod ->
            val isLowStock = prod.stockQty <= prod.alertLevel
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFEFF3FF)) {
                                    Text(prod.category, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(shape = RoundedCornerShape(4.dp), color = if (isLowStock) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)) {
                                    Text(if (isLowStock) "⚠️ ${prod.stockQty} Left" else "• ${prod.stockQty} In Stock", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isLowStock) Color(0xFFD32F2F) else Color(0xFF2E7D32), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(prod.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                            Text(prod.spec, fontSize = 11.sp, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Price (PKR)", fontSize = 10.sp, color = Color.Gray)
                            Text("PKR ${String.format("%,.0f", prod.unitPrice)}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0D47A1))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        OutlinedButton(
                            onClick = { onDelete(prod) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Delete", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onEdit(prod) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerKhataTabScreen(customers: List<CustomerAccount>, onEdit: (CustomerAccount) -> Unit, onDelete: (CustomerAccount) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val filtered = customers.filter { it.name.contains(searchQuery, ignoreCase = true) || it.phone.contains(searchQuery) || it.area.contains(searchQuery, ignoreCase = true) }
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
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Total Outstanding Market Credit (Udhar)", fontSize = 11.sp, color = Color.Gray)
                    Text("PKR ${String.format("%,.0f", totalUdhar)}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${customers.size} Registered Accounts in Hasilpur", fontSize = 11.sp, color = Color.Gray)
                }
            }
        }
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search customer name, phone or chak...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF0D47A1)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }
        items(filtered) { cust ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(cust.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0D47A1))
                            Text("${cust.area} • ${cust.phone}", fontSize = 11.sp, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("PKR ${String.format("%,.0f", cust.balanceDue)}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFFD32F2F))
                            Text("Last: ${cust.lastEntry}", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        OutlinedButton(
                            onClick = { onDelete(cust) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Delete", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onEdit(cust) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReportsAndSuppliersTabScreen(bills: List<BillRecord>, customers: List<CustomerAccount>, suppliers: List<SupplierRecord>, stockItems: List<ProductItem>) {
    val totalSales = bills.sumOf { it.total }
    val cashInHand = bills.sumOf { it.paid }
    val marketUdhar = customers.sumOf { it.balanceDue }
    val supplierPayable = suppliers.sumOf { it.balancePayable }
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
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Executive Financial Summary", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                    Text("New Afzal Battery Center & Solar Point", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = Color(0xFFEFF3FF))
                    Spacer(modifier = Modifier.height(8.dp))
                    ReportDataRow("Total Sales Revenue", "PKR ${String.format("%,.0f", totalSales)}", Color(0xFF0D47A1))
                    ReportDataRow("Cash In Hand Collected", "PKR ${String.format("%,.0f", cashInHand)}", Color(0xFF2E7D32))
                    ReportDataRow("Customer Market Credit (Udhar)", "PKR ${String.format("%,.0f", marketUdhar)}", Color(0xFFD32F2F))
                    ReportDataRow("Total Stock Valuation", "PKR ${String.format("%,.0f", totalStockValuation)}", Color(0xFF0D47A1))
                    ReportDataRow("Supplier Payable Debt (B2B)", "PKR ${String.format("%,.0f", supplierPayable)}", Color(0xFFD32F2F))
                    Divider(modifier = Modifier.padding(vertical = 6.dp), color = Color(0xFFEFF3FF))
                    ReportDataRow("Estimated Gross Profit (12%)", "PKR ${String.format("%,.0f", totalSales * 0.12)}", Color(0xFF2E7D32), isBold = true)
                }
            }
        }
        item {
            Text("Principal Suppliers & Distributors (${suppliers.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
        }
        items(suppliers) { sup ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(sup.companyName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0D47A1))
                        Text("${sup.brand} • ${sup.city} • ${sup.phone}", fontSize = 11.sp, color = Color.Gray)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Payable", fontSize = 10.sp, color = Color.Gray)
                        Text("PKR ${String.format("%,.0f", sup.balancePayable)}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFD32F2F))
                    }
                }
            }
        }
    }
}

@Composable
fun ProductEditDialog(title: String, initialProduct: ProductItem?, onDismiss: () -> Unit, onSave: (ProductItem) -> Unit) {
    var name by remember { mutableStateOf(initialProduct?.name ?: "") }
    var category by remember { mutableStateOf(initialProduct?.category ?: "Solar Panels") }
    var brand by remember { mutableStateOf(initialProduct?.brand ?: "Tomzn") }
    var qtyText by remember { mutableStateOf(initialProduct?.stockQty?.toString() ?: "10") }
    var priceText by remember { mutableStateOf(initialProduct?.unitPrice?.toInt()?.toString() ?: "1850") }
    var specText by remember { mutableStateOf(initialProduct?.spec ?: "63A 2P DC Breaker Curve C") }
    val categories = listOf("Solar Panels", "Inverters & VFD", "Batteries", "Breakers & Relays")

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
                Divider(color = Color(0xFFEFF3FF))
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Product Name *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = specText, onValueChange = { specText = it }, label = { Text("Specification / Brand") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Text("Category:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    categories.forEach { cat ->
                        val isChosen = cat == category
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isChosen) Color(0xFF0D47A1) else Color(0xFFEFF3FF),
                            modifier = Modifier.weight(1f).clickable { category = cat }
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 6.dp)) {
                                Text(cat.split(" ").first(), fontSize = 10.sp, color = if (isChosen) Color.White else Color.Black, fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = qtyText, onValueChange = { qtyText = it }, label = { Text("Stock Qty") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = priceText, onValueChange = { priceText = it }, label = { Text("Rate (PKR)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1.3f), singleLine = true)
                }
                Button(
                    onClick = {
                        val qty = qtyText.toIntOrNull() ?: 1
                        val price = priceText.toDoubleOrNull() ?: 0.0
                        onSave(ProductItem(initialProduct?.id ?: "P-${(100..999).random()}", name.ifBlank { "Solar Item" }, category, brand, qty, price, 3, specText))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                ) {
                    Text("Save to Inventory", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun NewInvoiceDialog(availableProducts: List<ProductItem>, onDismiss: () -> Unit, onSave: (BillRecord) -> Unit) {
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var selectedProduct by remember { mutableStateOf(availableProducts.firstOrNull()) }
    var quantityText by remember { mutableStateOf("1") }
    var rateText by remember { mutableStateOf(selectedProduct?.unitPrice?.toInt()?.toString() ?: "1850") }
    var paidText by remember { mutableStateOf("") }
    val qty = quantityText.toIntOrNull() ?: 1
    val rate = rateText.toDoubleOrNull() ?: 0.0
    val total = qty * rate
    val paid = paidText.toDoubleOrNull() ?: total

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Generate Sales Invoice", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
                Divider(color = Color(0xFFEFF3FF))
                OutlinedTextField(value = customerName, onValueChange = { customerName = it }, label = { Text("Customer Name *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = customerPhone, onValueChange = { customerPhone = it }, label = { Text("Phone Number") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth(), singleLine = true)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = quantityText, onValueChange = { quantityText = it }, label = { Text("Qty") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = rateText, onValueChange = { rateText = it }, label = { Text("Rate (PKR)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1.3f), singleLine = true)
                }
                Surface(color = Color(0xFFEFF3FF), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Bill:", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                        Text("PKR ${String.format("%,.0f", total)}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0D47A1))
                    }
                }
                OutlinedTextField(value = paidText, onValueChange = { paidText = it }, label = { Text("Cash Received") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), singleLine = true)
                Button(
                    onClick = {
                        onSave(BillRecord("INV-${(100..999).random()}", customerName.ifBlank { "Walk-in Client" }, customerPhone.ifBlank { "0300-XXXXXXX" }, "${selectedProduct?.name ?: "Solar"} (x$qty)", qty, rate, total, paid, "15 Oct 2026"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                ) {
                    Text("Save & Print Invoice", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CustomerEditDialog(title: String, initialCustomer: CustomerAccount?, onDismiss: () -> Unit, onSave: (CustomerAccount) -> Unit) {
    var name by remember { mutableStateOf(initialCustomer?.name ?: "") }
    var phone by remember { mutableStateOf(initialCustomer?.phone ?: "") }
    var area by remember { mutableStateOf(initialCustomer?.area ?: "Vehari Road, Hasilpur") }
    var balanceText by remember { mutableStateOf(initialCustomer?.balanceDue?.toInt()?.toString() ?: "0") }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
                Divider(color = Color(0xFFEFF3FF))
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Customer Full Name *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number (WhatsApp)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = area, onValueChange = { area = it }, label = { Text("Area / Chak No.") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = balanceText, onValueChange = { balanceText = it }, label = { Text("Opening Udhar Balance (PKR)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), singleLine = true)
                Button(
                    onClick = {
                        val bal = balanceText.toDoubleOrNull() ?: 0.0
                        onSave(CustomerAccount(initialCustomer?.id ?: "C-${(100..999).random()}", name.ifBlank { "Client" }, phone.ifBlank { "0300-XXXXXXX" }, area, bal, "15 Oct 2026"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                ) {
                    Text("Save Customer Account", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ReportDataRow(title: String, amount: String, color: Color, isBold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(title, fontSize = if (isBold) 13.sp else 12.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal, color = Color(0xFF334155))
        Text(amount, fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold, fontSize = if (isBold) 14.sp else 12.sp, color = color)
    }
}

@Composable
fun KPIBox(title: String, value: String, color: Color) {
    Column {
        Text(title, fontSize = 11.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
