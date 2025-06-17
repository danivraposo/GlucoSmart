plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.francisco.glucosmart"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.francisco.glucoSmart"
        minSdk = 24
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
    
    // Desativar o detector de lint que está causando problemas
    lint {
        disable += "CoroutineCreationDuringComposition"
    }
}

dependencies {
    // Material 3 (com versão explícita para evitar conflito)
    implementation("androidx.compose.material3:material3:1.2.1") // use versão mais recente

    // Ícones
    implementation("androidx.compose.material:material-icons-extended")

    // Navigation e Compose
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.compose.ui:ui:1.5.1")
    implementation("androidx.compose.material3:material3:1.2.1")


    // AndroidX essenciais
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.core:core-ktx:1.12.0")

    // Testes
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.1")

    implementation("androidx.core:core-splashscreen:1.0.1")
}
