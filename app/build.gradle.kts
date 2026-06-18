plugins {

    alias(libs.plugins.android.application)

    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.ksp)

}


android {

    namespace = "com.example.hs_map_frontend"

    compileSdk {

        version = release(36) {

            minorApiLevel = 1

        }

    }


    defaultConfig {

        applicationId = "com.example.hs_map_frontend"

        minSdk = 24

        targetSdk = 36

        versionCode = 1

        versionName = "1.0"


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }


    buildTypes {

        release {

            optimization {

                enable = false

            }

        }

    }

    compileOptions {

        sourceCompatibility = JavaVersion.VERSION_11

        targetCompatibility = JavaVersion.VERSION_11

    }

    buildFeatures {

        compose = true

    }

    packaging {

        jniLibs {

            useLegacyPackaging = false

        }

    }

}


dependencies {

// android core

    implementation("androidx.core:core-ktx:1.13.1")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")

    implementation("androidx.activity:activity-compose:1.9.0")


// compose core

    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")

    implementation(composeBom)

    implementation("androidx.compose.ui:ui")

    implementation("androidx.compose.ui:ui-graphics")

    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation("androidx.compose.material3:material3")

    debugImplementation("androidx.compose.ui:ui-tooling")


// compose navigation

    implementation("androidx.navigation:navigation-compose:2.7.7")


// maplibre

    implementation("org.maplibre.gl:android-sdk:13.3.0")


// compose-destinations

    implementation("io.github.raamcosta.compose-destinations:core:2.2.0")

    ksp("io.github.raamcosta.compose-destinations:ksp:2.3.0")

// azure speech services

    implementation("com.microsoft.cognitiveservices.speech:client-sdk:1.37.0")


// retrofit, networking

    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")


// local storage

    implementation("androidx.security:security-crypto:1.1.0-alpha06")

} 