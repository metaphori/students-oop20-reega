import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("java")
    id("application")
    id("eclipse")
}

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://plugins.gradle.org/m2/")
}

version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

application {
    mainClassName = "reega.main.Launcher"
}

val javaFXModules = listOf("base", "controls", "fxml", "graphics", "swing")
val supportedPlatforms = listOf("linux", "mac", "win")

dependencies {
    for (platform in supportedPlatforms) {
        for (module in javaFXModules) {
            implementation("org.openjfx:javafx-$module:17-ea+3:$platform")
        }
    }

    // Tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.0")
    
    // Implementation to make MockWebServer work with JUnit 5 in Eclipse 
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    // DB driver (local development purpose only)
    implementation("org.postgresql:postgresql:42.2.18")

    // HTTP client library - stick to 2.7.0 to avoid illegal reflections (should upgrade to Java 14)
    implementation("com.squareup.retrofit2:retrofit:2.7.0")
    implementation("com.squareup.retrofit2:converter-gson:2.7.0")

    // Gson for JSON parsing
    implementation("com.google.code.gson:gson:2.8.6")

    // Bcrypt library
    implementation("org.springframework.security:spring-security-crypto:5.4.2")

    // Utilities
    implementation("commons-io:commons-io:2.8.0")
    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("com.google.guava:guava:30.1-jre")
    implementation("org.apache.commons:commons-collections4:4.4")

    // logging
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.13.0")

    // Dependency injection
    implementation("javax.inject:javax.inject:1")

    // Email validation
    implementation("commons-validator:commons-validator:1.7")
}

defaultTasks("clean", "test", "shadowJar")
tasks {
    test {
        useJUnitPlatform()
    }
    jar {
        manifest {
            attributes["Main-Class"] = "reega.main.Launcher"
        }
    }
    withType<ShadowJar> {
        archiveFileName.set("reega.jar")
        mergeServiceFiles()
    }
}