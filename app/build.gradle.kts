plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.peminjamanmobile.admin"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.peminjamanmobile.admin"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))

    implementation ("com.squareup.picasso:picasso:2.8")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    implementation(libs.firebase.storage)
    implementation(libs.recyclerview)
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Tab Layout
    implementation(libs.viewpager2)

    // Circle Image View
    implementation(libs.hdodenhof.circleimageview)
}