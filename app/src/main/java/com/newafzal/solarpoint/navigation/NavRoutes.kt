package com.newafzal.solarpoint.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val titleUrdu: String, val icon: ImageVector? = null) {
    object Billing : Screen("billing", "سیل و بلنگ", Icons.Default.PointOfSale)
    object Inventory : Screen("inventory", "سامان سٹاک", Icons.Default.Inventory2)
    object Khata : Screen("khata", "کسٹمر کھاتہ", Icons.Default.MenuBook)
    object Reports : Screen("reports", "رپورٹس و منافع", Icons.Default.Assessment)

    // Sub screens
    object Roznamcha : Screen("roznamcha", "روزنامچہ کیش کلوزنگ")
    object StaffLedger : Screen("staff_ledger", "ملازمین تنخواہ کھاتہ")
    object SolarQuotation : Screen("solar_quotation", "سولر کوٹیشن ایسٹیمیٹ")
    object WarrantyTracking : Screen("warranty_tracking", "وارنٹی کلیم ٹریکنگ")
    object BackupRestore : Screen("backup_restore", "کلاؤڈ آٹو بیک اپ")
}
