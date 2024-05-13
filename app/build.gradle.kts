plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.app_covid19_konrad"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.app_covid19_konrad"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    /*
    Sekcja dependencies w pliku build.gradle.kts określa wszystkie biblioteki, których aplikacja
    potrzebuje do działania. Biblioteki te mogą być bibliotekami standardowymi Androida,
    bibliotekami innych firm lub bibliotekami utworzonymi przez Ciebie.
    Na przykład, w powyższym pliku build.gradle.kts sekcja dependencies zawiera następujące biblioteki:
     */
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")  // standardowa bibliotaka języka Kotlin
    implementation("androidx.core:core-ktx:1.13.1") // biblioteka rozszerzeń dla podstawowych funkcji Androida
    implementation("com.android.volley:volley:1.2.1") // biblioteka do obsługi API REST, żądań HTTP
    implementation("androidx.appcompat:appcompat:1.6.1") // biblioteka zapewniająca obsługe starszych wersji Androida
    implementation("com.google.android.material:material:1.12.0") // biblioteka zapewniająca komponenty Material Design
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // biblioteka zapewniająca obsługe constraint layout

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}