import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "org.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    val androidxComposeVersion = libs.versions.androidxCompose.get()

    constraints {
        implementation("androidx.compose.runtime:runtime-android:$androidxComposeVersion")
        implementation("androidx.compose.runtime:runtime-saveable-android:$androidxComposeVersion")
        implementation("androidx.compose.runtime:runtime-annotation-android:$androidxComposeVersion")
        implementation("androidx.compose.runtime:runtime-retain-android:$androidxComposeVersion")

        implementation("androidx.compose.ui:ui-android:$androidxComposeVersion")
        implementation("androidx.compose.ui:ui-tooling-android:$androidxComposeVersion")
        implementation("androidx.compose.ui:ui-tooling-preview-android:$androidxComposeVersion")
        implementation("androidx.compose.ui:ui-tooling-data-android:$androidxComposeVersion")
        implementation("androidx.compose.ui:ui-graphics-android:$androidxComposeVersion")
        implementation("androidx.compose.ui:ui-text-android:$androidxComposeVersion")
        implementation("androidx.compose.ui:ui-unit-android:$androidxComposeVersion")
        implementation("androidx.compose.ui:ui-geometry-android:$androidxComposeVersion")
        implementation("androidx.compose.ui:ui-util-android:$androidxComposeVersion")

        implementation("androidx.compose.foundation:foundation-android:$androidxComposeVersion")
        implementation("androidx.compose.foundation:foundation-layout-android:$androidxComposeVersion")

        implementation("androidx.compose.animation:animation-android:$androidxComposeVersion")
        implementation("androidx.compose.animation:animation-core-android:$androidxComposeVersion")
    }

    debugImplementation(libs.compose.uiTooling)
}

