plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "edu.cs4730.animatedvectordrawabledemo_kt"
    compileSdk = 37

    defaultConfig {
        applicationId = "edu.cs4730.animatedvectordrawabledemo_kt"
        minSdk = 32
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.vectordrawable)
    implementation(libs.androidx.vectordrawable.animated)
    implementation(libs.google.material)
    implementation(libs.androidx.activity.ktx)
}