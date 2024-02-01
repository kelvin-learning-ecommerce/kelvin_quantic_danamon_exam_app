plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.kelvin.pastisystem"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kelvin.pastisystem"
        minSdk = 24
        targetSdk = 34
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
        kotlinCompilerExtensionVersion = "1.4.6"
    }

    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
            )
        )
    }
}

dependencies {
    val nav_version = "2.7.6"

    implementation(platform("androidx.compose:compose-bom:2023.04.01"))

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material:1.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.navigation:navigation-compose:$nav_version")
//    implementation ("org.mongodb:mongodb-driver-kotlin:1.7.2")
    implementation("androidx.compose.foundation:foundation:1.6.0")

    implementation("com.jakewharton.timber:timber:5.0.1")

    //DI
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    kapt("androidx.hilt:hilt-compiler:1.1.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("androidx.hilt:hilt-navigation-fragment:1.1.0")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.amshove.kluent:kluent:1.73")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    //accompanist
    implementation("com.google.accompanist:accompanist-swiperefresh:0.23.1")
    implementation("com.airbnb.android:lottie-compose:5.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.6")
    implementation("io.coil-kt:coil-compose:2.4.0")

    //Test
    implementation("io.mockk:mockk:1.13.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    implementation("com.google.truth:truth:1.1.3")
}

kapt {
    correctErrorTypes = true
}
