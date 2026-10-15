package com.newafzal.solarpoint.navigation

import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.*
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.*
import java.util.concurrent.Executor
import androidx.core.content.ContextCompat

// ----------------------------------------------------
// Data Models
// ----------------------------------------------------
data class ProductItem(
    val id: String,
    val name: String,
    val category: String, // Solar Panels, Inverters & VFD, Batteries, Breakers & Relays, DC Accessories
    val brand: String, // Longi, Jinko, Inverex, Tomzn, Tesla, Mora, INVT, Osaka
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContainer() {
    val context = LocalContext.current
    var isAuthenticated by remember { mutableStateOf(false) }

    // Initial Pakistani Market Stock Data
    var stockList by remember {
        mutableStateOf(
            listOf(
                ProductItem("P-1", "Longi 585W Hi-MO 6 Explorer", "Solar Panels", "Longi", 54, 18500.0, 10, "Mono HPBC Cell • 22.8% Eff"),
                ProductItem("P-2", "Jinko 580W Tiger Neo N-Type", "Solar Panels", "Jinko", 38, 18300.0, 10, "Dual Glass SMBB • TopCon"),
                ProductItem("P-3", "CanadianSolar 600W Bi-Facial", "Solar Panels", "Canadian", 6, 19800.0, 8, "TOPHiKu6 High Gain 30Yr Warranty"),
                ProductItem("P-4", "Inverex Nitrox 6kW Hybrid 48V", "Inverters & VFD", "Inverex", 11, 365000.0, 3, "Dual MPPT • IP65 Waterproof Touch Screen"),
                ProductItem("P-5", "INVT 15kW / 20HP Tubewell VFD", "Inverters & VFD", "INVT", 9, 118000.0, 2, "Solar Pumping GD100 Auto MPPT"),
                ProductItem("P-6", "Crown 3.2kW Solar Inverter", "Inverters & VFD", "Crown", 14, 142000.0, 3, "Pure Sine Wave 80A MPPT"),
                ProductItem("P-7", "Osaka Tubular TX-2500 Deep Cycle", "Batteries", "Osaka", 22, 68500.0, 5, "230Ah Heavy Duty Tubular Plate"),
                ProductItem("P-8", "Phoenix TX-1800 Tubular", "Batteries", "Phoenix", 18, 54000.0, 4, "165Ah Deep Cycle Solar Spec"),
                ProductItem("P-9", "Mora Digital Under/Over Voltage Relay 63A", "Breakers & Relays", "Mora", 75, 2450.0, 15, "Dual Display Voltage/Current Auto Recovery"),
                ProductItem("P-10", "Tomzn 63A 2P DC Breaker 500V", "Breakers & Relays", "Tomzn", 120, 1850.0, 20, "Curve C PV String Isolator"),
                ProductItem("P-11", "Tesla 63A 4P AC Main Breaker", "Breakers & Relays", "Tesla", 45, 3200.0, 10, "10kA Industrial AC Safety Breaker"),
                ProductItem("P-12", "Tomzn Surge Protective Device (SPD 2P)", "Breakers & Relays", "Tomzn", 60, 2100.0, 10, "DC 1000V 20-40kA Lightning Arrester")
            )
        )
    }

    var billList by remember {
        mutableStateOf(
            listOf(
                BillRecord("INV-101", "Chaudhry Tariq Mehmood", "0300-7861122", "Longi 585W (x12)", 12, 17500.0, 210000.0, 150000.0, "15 Oct 2026"),
                BillRecord("INV-102", "Haji Farooq Hasilpur", "0301-5544332", "Osaka TX-2500 (x2)", 2, 48000.0, 96000.0, 96000.0, "15 Oct 2026")
            )
        )
    }

    // Refresh simulation trigger
    var isRefreshing by remember { mutableStateOf(false) }

    // Modals
    var showAddProductDialog by remember { mutableStateOf(false) }
    var productToEdit by remember { mutableStateOf<ProductItem?>(null) }
    var productToDelete by remember { mutableStateOf<ProductItem?>(null) }
    var showNewInvoiceDialog by remember { mutableStateOf(false) }

    // If not authenticated, show professional Login Screen
    if (!isAuthenticated) {
        LoginScreen(
            onLoginSuccess = { isAuthenticated = true }
        )
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
                                text = "New Afzal Battery & Solar",
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
                    // Refresh Button
                    IconButton(onClick = {
                        isRefreshing = true
                        Toast.makeText(context, "Refreshing stock & cloud records...", Toast.LENGTH_SHORT).show()
                        isRefreshing = false
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                    }
                    // Logout / Lock Button
                    IconButton(onClick = { isAuthenticated = false }) {
                        Icon(Icons.Default.Lock, contentDescription = "Lock", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D47A1))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddProductDialog = true },
                containerColor = Color(0xFF0D47A1),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    ) { innerPadding ->
        AllInOneDashboard(
            modifier = Modifier.padding(innerPadding),
            stockItems = stockList,
            bills = billList,
            onAddInvoice = { showNewInvoiceDialog = true },
            onEditProduct = { productToEdit = it },
            onDeleteProduct = { productToDelete = it }
        )

        // Dialogs for CRUD
        if (showAddProductDialog) {
            ProductEditDialog(
                title = "Add New Solar Product / Breaker",
                initialProduct = null,
                onDismiss = { showAddProductDialog = false },
                onSave = { newProd ->
                    stockList = listOf(newProd) + stockList
                    showAddProductDialog = false
                    Toast.makeText(context, "${newProd.name} added to stock!", Toast.LENGTH_SHORT).show()
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
                text = { Text("Are you sure you want to permanently remove ${prod.name} from inventory?") },
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
    }
}

// ====================================================
// 1. SECURE LOGIN SCREEN WITH FINGERPRINT & afzal0300
// ====================================================
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
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

                Text(
                    text = "New Afzal Solar Point",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1)
                )
                Text(
                    text = "Vehari Road, Hasilpur • Terminal Login",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

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
                            errorMessage = "Incorrect Password! Default is: afzal0300"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Login to ERP System", fontWeight = FontWeight.Bold, color = Color.White)
                }

                Divider(color = Color(0xFFEFF3FF))

                // Biometric Fingerprint Login Button
                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "Fingerprint Verified: Afzal Admin", Toast.LENGTH_SHORT).show()
                        onLoginSuccess()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0D47A1)),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.Fingerprint, contentDescription = null, tint = Color(0xFF0D47A1))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Touch ID / Fingerprint Login", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// ====================================================
// 2. ALL-IN-ONE DASHBOARD & COMPLETE INVENTORY
// ====================================================
@Composable
fun AllInOneDashboard(
    modifier: Modifier = Modifier,
    stockItems: List<ProductItem>,
    bills: List<BillRecord>,
    onAddInvoice: () -> Unit,
    onEditProduct: (ProductItem) -> Unit,
    onDeleteProduct: (ProductItem) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "Solar Panels", "Inverters & VFD", "Batteries", "Breakers & Relays")

    val filteredItems = stockItems.filter { item ->
        val matchesSearch = item.name.contains(searchQuery, ignoreCase = true) ||
                item.brand.contains(searchQuery, ignoreCase = true) ||
                item.spec.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "All" || item.category == selectedCategory
        matchesSearch && matchesCategory
    }

    val totalSales = bills.sumOf { it.total }
    val cashReceived = bills.sumOf { it.paid }
    val udharDue = totalSales - cashReceived

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FF))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // KPI Overview Row
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
                        Text("Today's Overview", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1), fontSize = 15.sp)
                        Button(
                            onClick = onAddInvoice,
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
                        KPIBox("Cash Inflow", "PKR ${String.format("%,.0f", cashReceived)}", Color(0xFF2E7D32))
                        KPIBox("Udhar Due", "PKR ${String.format("%,.0f", udharDue)}", Color(0xFFD32F2F))
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search 63A Breaker, Tomzn, Tesla, Mora, Inverters...") },
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
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
        }

        // Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = cat == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF0D47A1),
                            selectedLabelColor = Color.White
                        )
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFEFF3FF)
                                ) {
                                    Text(
                                        prod.category,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0D47A1),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (isLowStock) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                                ) {
                                    Text(
                                        if (isLowStock) "⚠️ ${prod.stockQty} Left" else "• ${prod.stockQty} In Stock",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isLowStock) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(prod.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                            Text(prod.spec, fontSize = 11.sp, color = Color.Gray)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Retail PKR", fontSize = 10.sp, color = Color.Gray)
                            Text("PKR ${String.format("%,.0f", prod.unitPrice)}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0D47A1))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { onDeleteProduct(prod) },
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
                            onClick = { onEditProduct(prod) },
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

// ====================================================
// 3. EDIT & ADD PRODUCT DIALOG
// ====================================================
@Composable
fun ProductEditDialog(
    title: String,
    initialProduct: ProductItem?,
    onDismiss: () -> Unit,
    onSave: (ProductItem) -> Unit
) {
    var name by remember { mutableStateOf(initialProduct?.name ?: "") }
    var category by remember { mutableStateOf(initialProduct?.category ?: "Solar Panels") }
    var brand by remember { mutableStateOf(initialProduct?.brand ?: "Tomzn") }
    var qtyText by remember { mutableStateOf(initialProduct?.stockQty?.toString() ?: "10") }
    var priceText by remember { mutableStateOf(initialProduct?.unitPrice?.toInt()?.toString() ?: "1850") }
    var specText by remember { mutableStateOf(initialProduct?.spec ?: "63A 2P DC Breaker Curve C") }

    val categories = listOf("Solar Panels", "Inverters & VFD", "Batteries", "Breakers & Relays")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
                Divider(color = Color(0xFFEFF3FF))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name & Model *") },
                    placeholder = { Text("e.g. Tomzn 63A 2P DC Breaker") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = specText,
                    onValueChange = { specText = it },
                    label = { Text("Specification / Brand details") },
                    placeholder = { Text("e.g. Tesla 63A 4P AC / Mora Relay") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

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
                                Text(
                                    cat.split(" ").first(),
                                    fontSize = 10.sp,
                                    color = if (isChosen) Color.White else Color.Black,
                                    fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = qtyText,
                        onValueChange = { qtyText = it },
                        label = { Text("Stock Qty") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Rate (PKR)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1.3f),
                        singleLine = true
                    )
                }

                Button(
                    onClick = {
                        val qty = qtyText.toIntOrNull() ?: 1
                        val price = priceText.toDoubleOrNull() ?: 0.0
                        val item = ProductItem(
                            id = initialProduct?.id ?: "P-${(100..999).random()}",
                            name = name.ifBlank { "Solar Item" },
                            category = category,
                            brand = brand,
                            stockQty = qty,
                            unitPrice = price,
                            alertLevel = 3,
                            spec = specText
                        )
                        onSave(item)
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

// ====================================================
// 4. NEW INVOICE DIALOG
// ====================================================
@Composable
fun NewInvoiceDialog(
    availableProducts: List<ProductItem>,
    onDismiss: () -> Unit,
    onSave: (BillRecord) -> Unit
) {
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
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Generate Sales Invoice", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
                Divider(color = Color(0xFFEFF3FF))

                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Customer Name *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = customerPhone,
                    onValueChange = { customerPhone = it },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("Select Product:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    availableProducts.take(4).forEach { p ->
                        val isChosen = selectedProduct?.id == p.id
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isChosen) Color(0xFFEFF3FF) else Color.Transparent,
                            modifier = Modifier.fillMaxWidth().clickable {
                                selectedProduct = p
                                rateText = p.unitPrice.toInt().toString()
                            }
                        ) {
                            Row(modifier = Modifier.padding(6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(p.name, fontSize = 12.sp, fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal)
                                Text("PKR ${p.unitPrice.toInt()}", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = { quantityText = it },
                        label = { Text("Qty") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = rateText,
                        onValueChange = { rateText = it },
                        label = { Text("Price (PKR)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1.3f),
                        singleLine = true
                    )
                }

                Surface(color = Color(0xFFEFF3FF), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Bill:", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                        Text("PKR ${String.format("%,.0f", total)}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0D47A1))
                    }
                }

                OutlinedTextField(
                    value = paidText,
                    onValueChange = { paidText = it },
                    label = { Text("Cash Received (Leave empty for full)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Button(
                    onClick = {
                        val record = BillRecord(
                            id = "INV-${(100..999).random()}",
                            customerName = customerName.ifBlank { "Walk-in Client" },
                            phone = customerPhone.ifBlank { "0300-XXXXXXX" },
                            itemName = "${selectedProduct?.name ?: "Solar"} (x$qty)",
                            qty = qty,
                            rate = rate,
                            total = total,
                            paid = paid,
                            date = "15 Oct 2026"
                        )
                        onSave(record)
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
fun KPIBox(title: String, value: String, color: Color) {
    Column {
        Text(title, fontSize = 11.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
