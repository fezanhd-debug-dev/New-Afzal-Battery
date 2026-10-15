package com.newafzal.solarpoint.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

// ڈیٹا ماڈلز
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
    val name: String,
    val category: String,
    val stockQty: Int,
    val unitPrice: Double,
    val alertLevel: Int
)

data class CustomerKhataRecord(
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

    // لائیو لوکل اسٹیٹ (تمام ریکارڈز)
    var billList by remember {
        mutableStateOf(
            listOf(
                BillItem("INV-101", "محمد طارق چوہدری", "0300-7861122", "Longi 585W Hi-MO 6 Solar (x12)", 12, 17500.0, 210000.0, 150000.0, true, "15 Oct 2026"),
                BillItem("INV-102", "حاجی فاروق حاصل پور", "0301-5544332", "Osaka Tubular Battery TX-2500", 2, 48000.0, 96000.0, 96000.0, false, "15 Oct 2026"),
                BillItem("INV-103", "چوہدری بشیر احمد", "0302-9988776", "Inverex Veyron 3.2kW Inverter", 1, 145000.0, 145000.0, 100000.0, true, "14 Oct 2026")
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
                                text = "Vehari Road, Hasilpur • 0300-XXXXXXX",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D47A1)
                )
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
                        icon = { tab.icon?.let { Icon(it, contentDescription = tab.titleUrdu) } },
                        label = {
                            Text(
                                text = tab.titleUrdu,
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
                InventoryWorkingScreen()
            }
            composable(Screen.Khata.route) {
                CustomerKhataWorkingScreen()
            }
            composable(Screen.Reports.route) {
                ReportsWorkingScreen(bills = billList)
            }
        }

        // نیا اندراج مکمل فنکشنل ڈائیلاگ
        if (showNewBillDialog) {
            NewBillEntryDialog(
                onDismiss = { showNewBillDialog = false },
                onSaveBill = { newBill ->
                    billList = listOf(newBill) + billList
                    showNewBillDialog = false
                }
            )
        }
    }
}

// ==========================================
// 1. سیل و بلنگ ورکنگ اسکرین
// ==========================================
@Composable
fun BillingWorkingScreen(
    bills: List<BillItem>,
    onOpenNewEntry: () -> Unit
) {
    val totalSalesToday = bills.sumOf { it.totalAmount }
    val cashReceived = bills.sumOf { it.paidAmount }
    val udharToday = totalSalesToday - cashReceived

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FF))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ٹاپ کوئیک ایکشن اور آج کا خلاصہ
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
                        Button(
                            onClick = onOpenNewEntry,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("نیا بل بنائیں (اندراج)", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Text(
                            text = "سیل و بلنگ (POS)",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = Color(0xFFEFF3FF))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SummaryMiniBox("کل سیل", "PKR ${String.format("%,.0f", totalSalesToday)}", Color(0xFF0D47A1))
                        SummaryMiniBox("نقد وصولی", "PKR ${String.format("%,.0f", cashReceived)}", Color(0xFF2E7D32))
                        SummaryMiniBox("بقایا ادھار", "PKR ${String.format("%,.0f", udharToday)}", Color(0xFFD32F2F))
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
                    text = "${bills.size} بلز ریکارڈ",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "حالیہ سیلز واؤچرز",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            }
        }

        // تمام بلز کی فہرست
        items(bills) { bill ->
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
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (bill.totalAmount - bill.paidAmount > 0) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = if (bill.totalAmount - bill.paidAmount > 0) "بقایا ادھار: PKR ${String.format("%,.0f", bill.totalAmount - bill.paidAmount)}" else "مکمل وصول",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (bill.totalAmount - bill.paidAmount > 0) Color(0xFFD32F2F) else Color(0xFF2E7D32)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = bill.customerName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0D47A1))
                            Text(text = "${bill.id} • ${bill.date}", fontSize = 11.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "سامان: ${bill.itemType}", fontSize = 13.sp, color = Color(0xFF334155))
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "وصول: PKR ${String.format("%,.0f", bill.paidAmount)}",
                            fontSize = 12.sp,
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "ٹوٹل بل: PKR ${String.format("%,.0f", bill.totalAmount)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1)
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. نیا بل اندراج ڈائیلاگ (Interactive Form)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBillEntryDialog(
    onDismiss: () -> Unit,
    onSaveBill: (BillItem) -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var itemName by remember { mutableStateOf("Longi 585W Solar Panel") }
    var quantityText by remember { mutableStateOf("1") }
    var rateText by remember { mutableStateOf("17500") }
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
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
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                        Text(
                            text = "نیا کسٹمر بل اندراج",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1)
                        )
                    }
                    Divider(color = Color(0xFFEFF3FF))
                }

                item {
                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text("گاہک کا نام (Customer Name)") },
                        placeholder = { Text("مثلاً: چوہدری جمیل") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                item {
                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = { customerPhone = it },
                        label = { Text("موبائل نمبر (WhatsApp)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                item {
                    Text("سامان منتخب کریں:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0D47A1))
                    val itemsPreset = listOf(
                        "Longi 585W Solar Panel",
                        "Osaka TX-2500 Battery",
                        "Phoenix TX-1800 Tubular",
                        "Inverex 3.2kW Solar Inverter",
                        "Crown 6kW Hybrid Inverter"
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        itemsPreset.forEach { preset ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        itemName = preset
                                        if (preset.contains("Panel")) rateText = "17500"
                                        else if (preset.contains("Osaka")) rateText = "48000"
                                        else if (preset.contains("Phoenix")) rateText = "38000"
                                        else rateText = "145000"
                                    }
                                    .background(if (itemName == preset) Color(0xFFEFF3FF) else Color.Transparent)
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(preset, fontSize = 12.sp, color = if (itemName == preset) Color(0xFF0D47A1) else Color.Black)
                                if (itemName == preset) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF0D47A1), modifier = Modifier.size(16.dp))
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
                            label = { Text("تعداد (Qty)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = rateText,
                            onValueChange = { rateText = it },
                            label = { Text("ریٹ (Rate PKR)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1.5f),
                            singleLine = true
                        )
                    }
                }

                item {
                    Surface(
                        color = Color(0xFFEFF3FF),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("ٹوٹل بل رقم:", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                            Text("PKR ${String.format("%,.0f", totalBill)}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF0D47A1))
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = paidAmountText,
                        onValueChange = { paidAmountText = it },
                        label = { Text("نقد وصول رقم (Cash Paid)") },
                        placeholder = { Text("خالی چھوڑنے پر فل وصول") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                if (balanceDue > 0) {
                    item {
                        Surface(
                            color = Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "بقایا ادھار کھاتہ: PKR ${String.format("%,.0f", balanceDue)}",
                                color = Color(0xFFD32F2F),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            val newBill = BillItem(
                                id = "INV-${(100..999).random()}",
                                customerName = customerName.ifBlank { "واک اِن گاہک" },
                                phone = customerPhone.ifBlank { "0300-XXXXXXX" },
                                itemType = "$itemName (x$qty)",
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("محفوظ کریں اور پرنٹ بنائیں", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. سامان سٹاک (Inventory Screen)
// ==========================================
@Composable
fun InventoryWorkingScreen() {
    val items = remember {
        listOf(
            InventoryStockItem("Longi 585W Hi-MO 6 Solar", "سولر پینل", 48, 17500.0, 10),
            InventoryStockItem("Jinko 580W N-Type Tiger Pro", "سولر پینل", 24, 18000.0, 10),
            InventoryStockItem("Osaka Tubular TX-2500 (Deep Cycle)", "بیٹری", 14, 48000.0, 4),
            InventoryStockItem("Phoenix TX-1800 Tubular", "بیٹری", 18, 38000.0, 4),
            InventoryStockItem("Inverex Nitrox 6kW Hybrid 48V", "انورٹر", 5, 275000.0, 2),
            InventoryStockItem("Crown 3.2kW Solar Inverter", "انورٹر", 8, 138000.0, 2),
            InventoryStockItem("Pakistan Cables 6mm Solar DC Wire (90m)", "تار و سامان", 12, 28000.0, 3)
        )
    }

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
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D47A1)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("سامان گودام و سٹاک لائیو", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("دکان اور گودام میں موجود بیٹری، سولر پینل، انورٹر ریٹ لسٹ", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }

        items(items) { stock ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (stock.stockQty <= stock.alertLevel) Color(0xFFFFEBEE) else Color(0xFFEFF3FF)
                    ) {
                        Text(
                            text = "${stock.stockQty} دانے موجود",
                            color = if (stock.stockQty <= stock.alertLevel) Color(0xFFD32F2F) else Color(0xFF0D47A1),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = stock.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0D47A1))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "${stock.category} • ریٹ: PKR ${String.format("%,.0f", stock.unitPrice)}", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

// ==========================================
// 4. کسٹمر ادھار کھاتہ (Customer Khata Screen)
// ==========================================
@Composable
fun CustomerKhataWorkingScreen() {
    val customers = remember {
        listOf(
            CustomerKhataRecord("محمد طارق چوہدری", "حاصل پور سٹی", "0300-7861122", 60000.0, "15 Oct 2026"),
            CustomerKhataRecord("چوہدری بشیر احمد", "وہاڑی روڈ چک 58", "0302-9988776", 45000.0, "14 Oct 2026"),
            CustomerKhataRecord("ملک وسیم سولر فارم", "چونا والا", "0301-4455667", 185000.0, "10 Oct 2026"),
            CustomerKhataRecord("رانا امتیاز ٹیوب ویل", "فتح پور", "0304-1122334", 120000.0, "08 Oct 2026")
        )
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
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD32F2F)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("کل واجب الادا ادھار (مارکیٹ ریکوری)", fontSize = 13.sp, color = Color.White.copy(alpha = 0.9f))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("PKR ${String.format("%,.0f", totalUdhar)}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("4 اہم پارٹیوں سے وصولی باقی ہے", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }

        items(customers) { c ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "PKR ${String.format("%,.0f", c.balanceDue)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFFD32F2F)
                        )
                        Text(text = "اخری بل: ${c.lastPaymentDate}", fontSize = 10.sp, color = Color.Gray)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = c.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0D47A1))
                        Text(text = "${c.city} • ${c.phone}", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

// ==========================================
// 5. رپورٹس و منافع (Reports Working Screen)
// ==========================================
@Composable
fun ReportsWorkingScreen(bills: List<BillItem>) {
    val totalSales = bills.sumOf { it.totalAmount }
    val cashInHand = bills.sumOf { it.paidAmount }
    val marketUdhar = totalSales - cashInHand
    val estimatedProfit = totalSales * 0.12 // 12% اوسط منافع مارجن

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
                    Text("مالیاتی تجزیہ و منافع رپورٹ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                    Text("New Afzal Battery Center & Solar Point (حاصل پور)", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(14.dp))

                    ReportDataRow("کل کاروبار سیل (Total Revenue)", "PKR ${String.format("%,.0f", totalSales)}", Color(0xFF0D47A1))
                    ReportDataRow("نقد کیش ان ہینڈ (Cash In Hand)", "PKR ${String.format("%,.0f", cashInHand)}", Color(0xFF2E7D32))
                    ReportDataRow("کسٹمر ادھار بیلنس (Market Credit)", "PKR ${String.format("%,.0f", marketUdhar)}", Color(0xFFD32F2F))

                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFEFF3FF))

                    ReportDataRow("تخمینہ خالص منافع (Estimated Profit)", "PKR ${String.format("%,.0f", estimatedProfit)}", Color(0xFF2E7D32), isBold = true)
                }
            }
        }
    }
}

@Composable
fun ReportDataRow(title: String, amount: String, color: Color, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(amount, fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold, fontSize = if (isBold) 15.sp else 13.sp, color = color)
        Text(title, fontSize = if (isBold) 14.sp else 12.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal, color = Color(0xFF334155))
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
