// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        jcenter()
        maven { url = uri("http://dl.bintray.com/kotlin/kotlin-eap") }
//        maven { url = uri("https://maven.fabric.io/public") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.0-alpha13")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}")
        classpath("org.jetbrains.kotlin:kotlin-allopen:${versions.kotlin}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${versions.navigation}")
//        classpath("io.fabric.tools:gradle:1.31.2")
        classpath("gradle.plugin.com.betomorrow.gradle:appcenter-plugin:1.2.1")
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
