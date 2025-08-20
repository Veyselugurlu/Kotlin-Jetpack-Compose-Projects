plugins {
    kotlin("kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.jetareader"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.jetareader"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.common.ktx)
    implementation(libs.androidx.foundation.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.16.0"))
    // Add the dependency for the Firebase SDK for Google Analytics
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("androidx.navigation:navigation-compose:2.7.7")

//Hilt
    // Android için Hilt çekirdek kütüphanesi
    implementation("com.google.dagger:hilt-android:2.56.1")
// Hilt ile oluşturulan sınıfların derleme zamanında işlenmesi için gerekli compiler
    ksp("com.google.dagger:hilt-android-compiler:2.56.1")
// Jetpack Compose ile Navigation kullanırken Hilt entegrasyonu sağlar
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    //Room
    val room_version = "2.7.0"

// Room runtime bileşeni, veritabanı işlemlerinin çalışması için ana kütüphane
    implementation("androidx.room:room-runtime:$room_version")
// Room için annotation işlemcisidir, DAO ve Entity'leri işler
    ksp("androidx.room:room-compiler:$room_version")

// Coroutine ve LiveData desteği için KTX uzantıları
    implementation("androidx.room:room-ktx:$room_version")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    // Coroutines
    // Kotlin dilinde platformdan bağımsız olarak coroutine desteği sunar
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    // Android için coroutine desteği (örneğin Main dispatcher)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.2")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //coil
    implementation("io.coil-kt:coil-compose:2.5.0")


    //icon
    implementation("androidx.compose.material:material-icons-extended-android:1.7.8")
}