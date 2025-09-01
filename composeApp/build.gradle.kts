import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
//    id("com.github.gmazzo.buildconfig") version "5.6.7"
}

//@Sohan
//Special Note: Current Firebase sdk does not support Ktor 3.x.x versions yet,
//On the other hand Compose multiplatform default support for Ktor is >= 3.x.x versions,
//Hence I had to write this resolutionStrategy to overcome dependency mismatch crash
//In future, we need to update this when Firebase sdk will support Ktor >= 3.x.x versions
//So keep that in mind.
configurations.all {
    resolutionStrategy {
        eachDependency {
            if (requested.group == "io.ktor") {
                useVersion("2.3.2")
                because("This version is tested and verified for our app")
            }
        }
    }
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Flowcent"
            isStatic = true
            linkerOpts.add("-framework")
            linkerOpts.add("Network")
        }
    }

    sourceSets {
        named { it.lowercase().startsWith("ios") }.configureEach {
            languageSettings {
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.android)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.core.ktx)
            implementation(libs.play.services.auth)
            implementation(libs.androidx.credentials)
            implementation(libs.googleid)
            implementation(libs.credentials.play.services.auth)
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.ai)
            implementation(libs.androidx.multidex)
            implementation(libs.billing)
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(libs.navigation.compose)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.compose)
            implementation(libs.koin.viewmodel)
            implementation(libs.koin.viewmodel.navigation)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.bundles.ktor)
            implementation(libs.material.icons.extended)
            implementation(libs.moko.permissions)
            implementation(libs.moko.permissions.compose)
            implementation(libs.moko.permissions.microphone)
            implementation(libs.moko.permissions.contacts)
            implementation(libs.firebase.firestore)
            implementation(libs.kmpauth.google) //Google One Tap Sign-In
            implementation(libs.kmpauth.firebase) //Integrated Authentications with Firebase
            implementation(libs.kmpauth.uihelper) //UiHelper SignIn buttons (AppleSignIn, GoogleSignInButton)
            api(libs.datastore.preferences)
            api(libs.datastore)
            implementation(libs.napier)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.purchases.core)
            implementation(libs.purchases.ui)
            implementation(libs.purchases.datetime)
            implementation(libs.purchases.either)
            implementation(libs.purchases.result)
            implementation(libs.connectivity.compose.device)
            implementation(libs.connectivity.compose.http)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    namespace = "com.aiapp.flowcent"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    val appName = "flowcent"

    defaultConfig {
        applicationId = "com.aiapp.flowcent"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 7
        versionName = "1.0.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        getByName("debug") {
            keyAlias = "fcdebug"
            keyPassword = "fcdebug"
            storeFile = file("../keys/flowcent.debug.keystore")
            storePassword = "fcdebug"
        }
        create("release") {
            if (keystorePropertiesFile.exists()) {
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            versionNameSuffix = "-DEBUG"
            signingConfig = signingConfigs.getByName("debug")

        }
        getByName("release") {
            isMinifyEnabled = true
            versionNameSuffix = "-RELEASE"
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro" // This points to the file you just created
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }


    applicationVariants.all {
        val variant = this
        variant.outputs.all {
            val output = this
            if (output is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                output.outputFileName = "${appName}-${variant.name}-${variant.versionName}.apk"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}


dependencies {
    debugImplementation(compose.uiTooling)
}