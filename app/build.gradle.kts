import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
    kotlin("kapt")
    id("kotlin-allopen")
    id("androidx.navigation.safeargs")
    id("io.fabric")
}

allOpen {
    // allows mocking for classes w/o directly opening them for release builds
    annotation("com.spacebitlabs.plantly.OpenClass")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "com.spacebitlabs.plantly"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 11
        versionName = "1.0-alpha9"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        resValue("string", "images_file_path", "Android/data/$applicationId.debug/files/Pictures")

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    signingConfigs {
        create("release") {
            val keyProps = Properties()
            val keyProperties = file("../keystore.properties")
            if (keyProperties.exists()) {
                keyProps.load(FileInputStream(keyProperties))

                storeFile = file(keyProps["store"]!!)
                keyAlias = keyProps["alias"] as String?
                storePassword = keyProps["storePass"] as String?
                keyPassword = keyProps["pass"] as String?
            }
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }

        getByName("release") {
            // isDebuggable = true
            isMinifyEnabled = false
            proguardFiles("proguard-android.txt", "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            ext.properties["betaDistributionGroupAliases"] = "all"
            ext.properties["betaDistributionReleaseNotes"] = "Allow deleting a plant"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    useLibrary("android.test.runner")
    useLibrary("android.test.base")
    useLibrary("android.test.mock")
}

dependencies {
    implementation(fileTree("libs") { include("*.jar") })

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${versions.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.0")
    implementation("com.jakewharton:process-phoenix:2.0.0")

// support libs
    implementation("androidx.appcompat:appcompat:${versions.androidx}")
    implementation("com.google.android.material:material:${versions.material}")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta3")
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")

// architecture libs
    implementation("androidx.lifecycle:lifecycle-extensions:${versions.androidxArch}")
    kapt("androidx.lifecycle:lifecycle-common-java8:${versions.androidxArch}")
    implementation("io.reactivex.rxjava2:rxjava:2.2.9")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.0")
    implementation("io.reactivex.rxjava2:rxkotlin:2.3.0")
    implementation("androidx.navigation:navigation-fragment-ktx:${versions.navigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${versions.navigation}")
    implementation("androidx.work:work-runtime-ktx:${versions.work}")

// data libs
    implementation("androidx.room:room-runtime:${versions.room}")
    implementation("androidx.room:room-ktx:${versions.room}")
    implementation("androidx.room:room-rxjava2:${versions.room}")
    kapt("androidx.room:room-compiler:${versions.room}")
    implementation("com.jakewharton.threetenabp:threetenabp:1.2.1")
    implementation("com.github.kizitonwose.time:time:1.0.2")
    implementation("com.github.kizitonwose.time:time-android:1.0.2")
    implementation(project(path = ":amountformats-android"))

// UI libs
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("com.cesarferreira.colorize:colorize:0.2.2")
    implementation("de.hdodenhof:circleimageview:3.0.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("androidx.recyclerview:recyclerview-selection:1.0.0")
    implementation(project(":photopicker"))
    implementation("androidx.preference:preference-ktx:1.1.0")

// debug libs
    implementation("com.jakewharton.timber:timber:4.7.1")
    debugImplementation("com.amitshekhar.android:debug-db:1.0.6")

    implementation("com.crashlytics.sdk.android:crashlytics:2.10.1@aar") {
        isTransitive = true
    }

// test libs
    testImplementation("androidx.test.ext:junit:1.1.1")
    testImplementation("junit:junit:4.12")
    testImplementation("androidx.test:core:1.2.0")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.room:room-testing:${versions.room}")
    testImplementation("androidx.test.ext:truth:1.2.0")
    testImplementation("androidx.test.espresso:espresso-intents:3.2.0")
    testImplementation("androidx.test.espresso:espresso-core:3.2.0")
    testImplementation("io.mockk:mockk:1.9.3.kotlin12")
    testImplementation("org.robolectric:robolectric:4.3")

    androidTestImplementation("androidx.test:core:1.2.0")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test:rules:1.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    androidTestImplementation("androidx.test.ext:truth:1.2.0")
}
