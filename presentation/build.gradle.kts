@file:Suppress("UnstableApiUsage")

import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())
val naverMapClientId: String = properties.getProperty("naver.map.client.id")

@Suppress("UnstableApiUsage")
android {
    namespace = "com.whyranoid.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        manifestPlaceholders["naverMapClientId"] = naverMapClientId
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
}

dependencies {

    implementation(project(":domain"))

    implementation(libs.bundles.android.base)
    implementation(libs.play.services.oss.licenses)

    val composeBom = platform("androidx.compose:compose-bom:2024.05.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Material Design 3
    implementation("androidx.compose.material3:material3")

    // Material Design 2
    implementation("androidx.compose.material:material")

    implementation("androidx.compose.foundation:foundation")

    implementation("androidx.compose.ui:ui")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // custom design system based on Foundation)
    implementation("androidx.compose.material:material-icons-core")
    // Optional - Add full set of material icons
    implementation("androidx.compose.material:material-icons-extended")
    // Optional - Add window size utils
    implementation("androidx.compose.material3:material3-window-size-class")

    // Optional - Integration with activities
    implementation("androidx.activity:activity-compose:1.9.0")
    // Optional - Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")

    val navVersion = "2.5.3"
    implementation("androidx.navigation:navigation-compose:$navVersion")

    val koinVersion = "3.4.0"
    // koin-core
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-test:$koinVersion")

    // koin-android
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-android-compat:$koinVersion")
    implementation("io.insert-koin:koin-androidx-compose:$koinVersion")

    // orbit
    // TODO: change To Core For Compose Multiplatform
    val orbitVersion = "4.6.1"
    implementation("org.orbit-mvi:orbit-viewmodel:$orbitVersion")
    implementation("org.orbit-mvi:orbit-compose:$orbitVersion")
    // Tests
    testImplementation("org.orbit-mvi:orbit-test:$orbitVersion")

    // coil
    val coilVersion = "2.3.0"
    implementation("io.coil-kt:coil-compose:$coilVersion")

    val naverMapComposeVersion = "1.3.3"
    implementation("io.github.fornewid:naver-map-compose:$naverMapComposeVersion")

    // ComposeCalendar
    val composeCalendarVersion = "1.1.0"
    implementation("io.github.boguszpawlowski.composecalendar:composecalendar:$composeCalendarVersion")
    implementation("io.github.boguszpawlowski.composecalendar:kotlinx-datetime:$composeCalendarVersion")

    // RunningModule
    implementation("io.github.bngsh:runningdata:0.0.5")

    // Google Location
    val googleLocationVersion = "21.0.1"
    implementation("com.google.android.gms:play-services-location:$googleLocationVersion")

    // Paging
//    implementation("androidx.paging:paging-runtime:3.1.1")
//    implementation("androidx.paging:paging-compose:3.2.0-rc01")
    val pagingVersion = "3.2.1"
    val pagingForComposeVersion = "1.0.0-alpha18"
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")
    implementation("androidx.paging:paging-compose:$pagingForComposeVersion")
    implementation("com.google.accompanist:accompanist-pager:0.33.2-alpha")

    // FireBase
    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
    implementation("com.google.android.gms:play-services-auth:20.6.0")

    // System UI Controller
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.33.2-alpha")
}
