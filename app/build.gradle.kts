plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.hasanalic.ecommerce"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hasanalic.ecommerce"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
}

dependencies {

    // Barcode scanner
    implementation ("com.github.yuriy-budiyev:code-scanner:2.3.2")
    implementation("com.google.zxing:core:3.4.1")

    // Room and Room Pagination
    implementation ("androidx.room:room-runtime:2.6.1")
    implementation("androidx.activity:activity:1.8.0")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.room:room-paging:2.6.1")

    // Coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Networking
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-common:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // Fragment
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    // Glide
    implementation ("com.github.bumptech.glide:glide:4.15.0")

    // for CircularProgressBar
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")

    // Splash
    implementation ("androidx.core:core-splashscreen:1.0.0-beta02")

    // Location
    implementation ("com.google.android.gms:play-services-location:21.2.0")

    // Navigation
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    //Testing
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:2.11.0")
    testImplementation ("android.arch.core:core-testing:1.1.1")
    testImplementation ("com.google.truth:truth:1.1")

    // For Robolectric tests
    testImplementation("com.google.dagger:hilt-android-testing:2.28-alpha")

    // For instrumented tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.28-alpha")

    // ...with Kotlin
    kaptTest("com.google.dagger:hilt-android-compiler:2.40.5")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}