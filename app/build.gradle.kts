import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
//    id("com.google.devtools.ksp")

}

android {
    namespace = "com.financialcompanion.android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.financialcompanion.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    applicationVariants.all {
        outputs.all {
            if (this is BaseVariantOutputImpl) {
                outputFileName = when (buildType.name) {
                    "debug" -> {
                        val date = SimpleDateFormat("dd.MM.yyyy").format(Date())
                        "FinancialCompanion-$date.apk"
                    }

                    "beta" -> {
                        val vc = versionCode
                        val vn = versionName.replace("(beta)", "")
                        "FinancialCompanion-beta-vc$vc-vn$vn.apk"
                    }

                    else -> {
                        val vc = versionCode
                        val vn = versionName
                        "FinancialCompanion-vc$vc-vn$vn.apk"
                    }
                }
            }
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("boolean", "RELEASE_BUILD_TYPE", "true")
            buildConfigField("boolean", "BETA_BUILD_TYPE", "false")
            buildConfigField("boolean", "DEBUG_BUILD_TYPE", "false")
        }

        register("beta") {
            initWith(getByName("release"))
            applicationIdSuffix = ".beta"
            versionNameSuffix = "(beta)"

            buildConfigField("boolean", "RELEASE_BUILD_TYPE", "false")
            buildConfigField("boolean", "BETA_BUILD_TYPE", "true")
            buildConfigField("boolean", "DEBUG_BUILD_TYPE", "false")
        }

        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "(debug)"
            isDebuggable = true

            buildConfigField("boolean", "RELEASE_BUILD_TYPE", "false")
            buildConfigField("boolean", "BETA_BUILD_TYPE", "false")
            buildConfigField("boolean", "DEBUG_BUILD_TYPE", "true")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.2")

    // UI - Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.lifecycle:lifecycle-runtime-compose")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("io.coil-kt:coil-compose:2.4.0")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // UI - Android
    implementation("androidx.fragment:fragment-ktx:1.6.2") // + MVVM // Некоторые антивирусы начинаю жаловаться при обновлении
    implementation("com.google.android.material:material:1.10.0") // Некоторые антивирусы начинаю жаловаться при обновлении

    // Util
    implementation("io.insert-koin:koin-android:3.3.2")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("joda-time:joda-time:2.10.14")
    implementation("com.chibatching.kotpref:kotpref:2.13.1")
    implementation("com.github.jamal-wia:NavigationController:1.1.3")
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")


    // Tools - memory leaks
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")
    // Tools - exceptions
    debugImplementation("com.github.haroldadmin:WhatTheStack:1.0.0-alpha04")

}