plugins {
    id("java")
    id("java-gradle-plugin")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.github.javaparser:javaparser-core:3.28.2")
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("unusedImportsPlugin") {
            id = "org.example.unused-imports"
            implementationClass = "org.example.UnusedImportsPlugin"
        }
    }
}