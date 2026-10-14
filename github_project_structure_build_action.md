# New Afzal Battery Center & Solar Point (Vehari Road, Hasilpur)
## GitHub Ready Android Project with GitHub Actions Auto APK Build (.github/workflows/build-apk.yml)

Yeh complete guide aur project structure hai jise aap seedha GitHub par upload kar sakte hain. GitHub Actions runner khud ba khud APK build karega aur aapko downloadable Release/Debug APK provide karega.

---

### Project File & Directory Structure

```text
new-afzal-solar-app/
├── .github/
│   └── workflows/
│       └── build-apk.yml
├── .gitignore
├── README.md
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.properties
│       └── gradle-wrapper.jar
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/
│           │   └── com/
│           │       └── newafzal/
│           │           └── solarpoint/
│           │               ├── MainActivity.kt
│           │               ├── navigation/
│           │               │   ├── NavRoutes.kt
│           │               │   └── AppNavHost.kt
│           │               ├── data/
│           │               │   ├── AppDatabase.kt
│           │               │   ├── model/
│           │               │   │   ├── Models.kt
│           │               │   │   └── StaffMember.kt
│           │               │   └── dao/
│           │               │       └── Daos.kt
│           │               ├── ui/
│           │               │   ├── theme/
│           │               │   │   ├── Color.kt
│           │               │   │   └── Theme.kt
│           │               │   └── screens/
│           │               │       ├── SalesBillingScreen.kt
│           │               │       ├── InventoryScreen.kt
│           │               │       ├── CustomerKhataScreen.kt
│           │               │       ├── RoznamchaScreen.kt
│           │               │       ├── StaffLedgerScreen.kt
│           │               │       ├── SolarQuotationScreen.kt
│           │               │       ├── WarrantyTrackingScreen.kt
│           │               │       └── BackupRestoreScreen.kt
│           └── res/
│               ├── values/
│               │   ├── strings.xml
│               │   ├── colors.xml
│               │   └── themes.xml
│               └── drawable/
```

---

### File 1: `.github/workflows/build-apk.yml` (Auto Build on GitHub)
```yaml
name: Build Android APK

on:
  push:
    branches: [ "main", "master" ]
  pull_request:
    branches: [ "main", "master" ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout Repository
      uses: actions/checkout@v4

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: gradle

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew || true

    - name: Build Debug APK
      run: ./gradlew assembleDebug --stacktrace

    - name: Upload Debug APK Artifact
      uses: actions/upload-artifact@v4
      with:
        name: NewAfzalSolar-Debug-APK
        path: app/build/outputs/apk/debug/app-debug.apk
        retention-days: 14
```

---

### File 2: `settings.gradle.kts`
```kotlin
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://jitpack.io") }
    }
}

rootProject.name = "NewAfzalSolar"
include(":app")
```

---

### File 3: `build.gradle.kts` (Project Root)
```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
```

---

### File 4: `gradle.properties`
```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
android.enableJetifier=true
kotlin.code.style=official
android.nonTransitiveRClass=true
```

---

### File 5: `app/build.gradle.kts`
```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.newafzal.solarpoint"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.newafzal.solarpoint"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Jetpack Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // WorkManager (Google Drive Auto Backup)
    implementation("androidx.work:work-runtime-ktx:2.9.0")
}
```

---

### File 6: `app/src/main/AndroidManifest.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
    <uses-permission android:name="android.permission.BLUETOOTH_SCAN" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="New Afzal Solar"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.NewAfzalSolar"
        tools:targetApi="31">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="New Afzal Solar"
            android:theme="@style/Theme.NewAfzalSolar">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

---

### File 7: `app/src/main/java/com/newafzal/solarpoint/MainActivity.kt`
```kotlin
package com.newafzal.solarpoint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.newafzal.solarpoint.navigation.MainAppContainer
import com.newafzal.solarpoint.ui.theme.NewAfzalSolarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewAfzalSolarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainAppContainer()
                }
            }
        }
    }
}
```

---

### File 8: `app/src/main/java/com/newafzal/solarpoint/navigation/NavRoutes.kt`
```kotlin
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
```

---

### File 9: `app/src/main/java/com/newafzal/solarpoint/navigation/AppNavHost.kt`
```kotlin
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
```

---

### File 10: `.gitignore`
```gitignore
*.iml
.gradle
/local.properties
/.idea/caches
/.idea/libraries
/.idea/modules.xml
/.idea/workspace.xml
.DS_Store
/build
/captures
.externalNativeBuild
.cxx
local.properties
app/build/
```
