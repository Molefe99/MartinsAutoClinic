plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.martinsautoclinic"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.martinsautoclinic"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    {
        exclude(group = "com.android.support")
    }
    implementation(libs.androidx.appcompat)
    implementation (libs.androidx.lifecycle.viewmodel.ktx.v261)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.kotlinx.coroutines.android)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.gridlayout)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    implementation(libs.firebase.database)
    implementation(libs.adapters)
    implementation (libs.androidx.cardview)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.recyclerview)
    implementation(libs.compiler)
    implementation(libs.firebase.auth.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

configurations.all {
    resolutionStrategy {
        force("androidx.core:core:1.13.1")
        force("com.google.guava:guava:31.1-jre")
    }
}
