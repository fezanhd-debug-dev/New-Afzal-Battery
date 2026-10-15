package com.newafzal.solarpoint.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContainer() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Billing.route

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
                    Column {
                        Text(
                            text = "New Afzal Battery & Solar",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Vehari Road, Hasilpur",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D47A1)
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
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
                GenericDashboardScreen("سیل و بلنگ (POS)", "نیا بل بنائیں، پرنٹ کریں، کسٹمر کو واٹس ایپ کریں")
            }
            composable(Screen.Inventory.route) {
                GenericDashboardScreen("سامان سٹاک (Inventory)", "بیٹری، سولر پینل، انورٹر کا لائیو سٹاک")
            }
            composable(Screen.Khata.route) {
                GenericDashboardScreen("کسٹمر کھاتہ (Udhar)", "ادھار کسٹمرز کا لیجر اور وصولی ریکارڈ")
            }
            composable(Screen.Reports.route) {
                GenericDashboardScreen("رپورٹس و منافع", "روزانہ اور ماہانہ منافع، نقد و بینک بیلنس")
            }
        }
    }
}

@Composable
fun GenericDashboardScreen(title: String, subtitle: String) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FF))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D47A1)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("نیا اندراج کریں +", color = Color.White)
                    }
                }
            }
        }
    }
}
