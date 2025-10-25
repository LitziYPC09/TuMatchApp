plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.tumatchapp_prueba"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.tumatchapp_prueba"
        minSdk = 24
        targetSdk = 36
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

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.cardview)
    // Glide para carga y cache de imágenes
    implementation(libs.glide)
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
// Converter para JSON usando Gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


}