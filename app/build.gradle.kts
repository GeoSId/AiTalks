plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.com.google.devtools)
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.gradle.plugin)
    alias(libs.plugins.secrets)
}

android {
    namespace = "com.geosid.aitalks"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.geosid.aitalks"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    secrets {
        propertiesFileName = "secrets.properties"
    }
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.compose.hilt)

    /*Sub -> ------------------Compose------------------*/
    implementation(libs.androidx.core.ktx)
    implementation(libs.constraintlayout.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    /*Sub -> ------------------UI------------------*/
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout)
    /*Sub -> ------------------Retrofit2------------------*/
    implementation(libs.retrofit.runtime)
    implementation(libs.retrofit.adapter.rx.java2)
    implementation(libs.retrofit.mock)
    implementation(libs.retrofit.gson.converter)
    /*Sub -> ------------------kotlinx-coroutines------------------*/
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.coroutines.android)
    /*Sub -> ------------------AI------------------*/
    implementation(libs.generativeai)
    /*Sub -> ------------------Coil for image loading------------------*/
    implementation(libs.io.coil.kt.coil.compose)
    implementation(libs.io.coil.kt.coil.compose.ext)
    /*Sub -> ------------------Halibo------------------*/
    implementation(libs.halib)
    implementation(libs.halib.mat)
    implementation(libs.halib.mat3)
    /*Sub -> ------------------Google------------------*/
    implementation(libs.com.google.dagger.hilt.android)
    ksp(libs.com.google.dagger.hilt.android.compiler)
    // OpenAI
    implementation(libs.openai)
}