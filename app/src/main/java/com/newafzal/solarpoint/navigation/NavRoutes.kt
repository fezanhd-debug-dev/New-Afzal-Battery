package com.newafzal.solarpoint.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Billing : Screen("billing", "POS & Billing", Icons.Default.PointOfSale)
    object Inventory : Screen("inventory", "Inventory Stock", Icons.Default.Inventory2)
    object Khata : Screen("khata", "Customer Ledger", Icons.Default.MenuBook)
    object Reports : Screen("reports", "Reports & Analytics", Icons.Default.Assessment)

    // Sub screens with default icon = null
    object Roznamcha : Screen("roznamcha", "Daily Cash Closing", null)
    object StaffLedger : Screen("staff_ledger", "Staff Salary Ledger", null)
    object SolarQuotation : Screen("solar_quotation", "Solar Quotation", null)
    object WarrantyTracking : Screen("warranty_tracking", "Warranty Tracking", null)
    object BackupRestore : Screen("backup_restore", "Cloud Auto Backup", null)
}
