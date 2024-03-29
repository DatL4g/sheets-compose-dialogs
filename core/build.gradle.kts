import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

/*
 *  Copyright (C) 2022-2024. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.serialization)
}

android {
    namespace = Modules.CORE.namespace
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

kotlin {
    androidTarget()
    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    macosX64()
    macosArm64()

    js(IR) {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.materialIconsExtended)
            implementation(compose.material3)
            implementation(compose.components.resources)

            implementation(libs.datetime)
            implementation(libs.serialization)
        }

        val nonAndroidMain by creating {
            dependsOn(commonMain.get())

            jvmMain.orNull?.dependsOn(this)
            nativeMain.orNull?.dependsOn(this)
            jsMain.orNull?.dependsOn(this)
        }

        val wasmJsMain by getting {
            dependsOn(nonAndroidMain)
        }
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar)
}
