package com.newafzal.solarpoint.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.newafzal.solarpoint.ui.screens.*

@Composable
fun MainAppContainer() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarTabs = listOf(
        Screen.Billing,
        Screen.Inventory,
        Screen.Khata,
        Screen.Reports
    )

    val showBottomBar = bottomBarTabs.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = NavigationBarDefaults.Elevation
                ) {
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
                            icon = {
                                tab.icon?.let { Icon(imageVector = it, contentDescription = tab.titleUrdu) }
                            },
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
                                indicatorColor = Color(0xFFEFF3FF),
                                unselectedIconColor = Color(0xFF74777F),
                                unselectedTextColor = Color(0xFF74777F)
                            )
                        )
                    }
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
                SalesBillingScreen(
                    onOpenQuotation = { navController.navigate(Screen.SolarQuotation.route) },
                    onOpenRoznamcha = { navController.navigate(Screen.Roznamcha.route) },
                    onWarrantyTrack = { navController.navigate(Screen.WarrantyTracking.route) },
                    onOpenStaff = { navController.navigate(Screen.StaffLedger.route) },
                    onOpenBackup = { navController.navigate(Screen.BackupRestore.route) }
                )
            }
            composable(Screen.Inventory.route) {
                InventoryScreen()
            }
            composable(Screen.Khata.route) {
                CustomerKhataScreen()
            }
            composable(Screen.Reports.route) {
                ReportsScreen()
            }
            composable(Screen.Roznamcha.route) {
                RoznamchaScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.StaffLedger.route) {
                StaffLedgerScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.SolarQuotation.route) {
                SolarQuotationScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.WarrantyTracking.route) {
                WarrantyTrackingScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.BackupRestore.route) {
                BackupRestoreScreen(onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}
