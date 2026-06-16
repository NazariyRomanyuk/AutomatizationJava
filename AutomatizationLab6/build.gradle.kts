plugins {
    id("java")
    id("checkstyle")
    id("maven-publish")
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.platform:junit-platform-suite")
    testRuntimeOnly("org.junit.platform:junit-platform-suite-engine")
}

tasks.test {
    useJUnitPlatform()
}

checkstyle {
    toolVersion = "13.6.0"
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/NazariyRomanyuk/AutomatizationJava")
            credentials {
                username = System.getenv("USERNAME")
                password = System.getenv("TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("jarPackage") {
            from(components["java"])
            groupId = group.toString()
            artifactId = "jar-package"
            version = System.getenv("VERSION")
        }
    }
}