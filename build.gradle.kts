// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        jcenter()
        maven { url = uri("http://dl.bintray.com/kotlin/kotlin-eap") }
        maven { url = uri("https://maven.fabric.io/public") }
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:4.0.0-alpha04")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}")
        classpath ("org.jetbrains.kotlin:kotlin-allopen:${versions.kotlin}")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:${versions.navigation}")
        classpath ("io.fabric.tools:gradle:1.31.2")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("http://dl.bintray.com/kotlin/kotlin-eap") }
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.fabric.io/public") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
