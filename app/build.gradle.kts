plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.lalaecomerce"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lalaecomerce"
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


}

dependencies {
    // Firebase Authentication
    implementation(libs.firebase.auth)

    // Firebase Firestore
    implementation(libs.firebase.firestore)

    // Material Design
    implementation(libs.constraintlayout.v213)
    implementation(libs.cardview)
    implementation (libs.material.v190)
    implementation (libs.glide)
    annotationProcessor (libs.compiler)


    implementation ("pub.devrel:easypermissions:3.0.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}