plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt") version "1.9.0"
    id("com.google.dagger.hilt.android") version "2.51.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.example.coastertrack"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.coastertrack"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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
    implementation(libs.androidx.material3.v140alpha18)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.hilt.android.v2511)
    implementation(libs.hilt.android.v2511)
    implementation( libs.androidx.hilt.navigation.compose)

    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)


    implementation(libs.converter.scalars)

    //image form url libary
    implementation(libs.coil.compose)

    //more icons
    implementation(libs.androidx.material.icons.extended)

    // allows user to pull down to refresh
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.datastore.preferences)
    kapt(libs.hilt.android.compiler)

    // data store dependencies
    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.javalite)


    // jetpack version for carousel
    implementation(libs.androidx.material3)

    // Room database
    implementation( libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    implementation(libs.androidx.room.ktx)

    // rearrangeable list
    implementation(libs.reorderable)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}