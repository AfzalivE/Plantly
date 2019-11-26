plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdkVersion (28)

    defaultConfig {
        minSdkVersion (19)
        targetSdkVersion (28)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(fileTree("libs") { include("*.jar") })
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${versions.kotlin}")
    implementation ("androidx.appcompat:appcompat:${versions.androidx}")
    implementation ("com.google.android.material:material:${versions.material}")

    testImplementation ("junit:junit:4.12")

    androidTestImplementation ("androidx.test.ext:junit:1.1.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.2.0")
}
repositories {
    maven {
        url = uri("http://dl.bintray.com/kotlin/kotlin-eap")
    }
    mavenCentral()
}