import de.aaschmid.gradle.plugins.cpd.Cpd
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("java")
    id("jacoco")
    id("application")
    id("eclipse")
    checkstyle
    pmd
    id("de.aaschmid.cpd")
    id("com.github.spotbugs")
    `build-dashboard`
}

/*
sourceSets {
    main {
        java {
            srcDirs("src")
        }
        resources {
            srcDirs("res")
        }
    }
    test {
        java {
            srcDirs("src")
        }
    }
}
*/

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://plugins.gradle.org/m2/")
    flatDir {
        dirs("lib")
    }
}


version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

application {
    mainClassName = "reega.main.Launcher"
}

jacoco {
    toolVersion = "0.8.6"
    reportsDir = file("$buildDir/jacoco")
}

val javaFXModules = listOf("base", "controls", "fxml", "graphics", "swing")
val supportedPlatforms = listOf("linux", "mac", "win")
val javaFxVersion = "17-ea+3"

dependencies {
    for (platform in supportedPlatforms) {
        for (module in javaFXModules) {
            implementation("org.openjfx:javafx-$module:$javaFxVersion:$platform")
        }
    }

    // Tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    implementation("junit:junit:_")
    runtimeOnly("org.junit.vintage:junit-vintage-engine:_")

    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.0")

    // Implementation to make MockWebServer work with JUnit 5 in Eclipse
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    // HTTP client library - stick to 2.7.0 to avoid illegal reflections (should upgrade to Java 14)
    implementation("com.squareup.retrofit2:retrofit:2.7.0")
    implementation("com.squareup.retrofit2:converter-gson:2.7.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.1.0")

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

    File("lib")
        .takeIf { it.exists() }
        ?.takeIf { it.isDirectory }
        ?.listFiles()
        ?.filter { it.extension == "jar" }
        ?.forEach { implementation(files("lib/${it.name}")) }
}

defaultTasks("clean", "shadowJar")
tasks {
    test {
        useJUnitPlatform()
        setForkEvery(2)
        maxParallelForks = 1
        finalizedBy("jacocoTestReport")
    }
    jacocoTestReport {
        dependsOn("test")
    }
    jar {
        manifest {
            attributes["Main-Class"] = "reega.main.Launcher"
        }
    }
    withType<ShadowJar> {
        dependsOn("test")
        archiveFileName.set("reega.jar")
        mergeServiceFiles()
    }
}

/*
tasks.withType<Test>() {
    ignoreFailures = true
    useJUnitPlatform()
}
*/

spotbugs {
    setEffort("max")
    setReportLevel("low")
    showProgress.set(true)
    val excludeFile = File("${project.rootProject.projectDir}/config/spotbugs/excludes.xml")
    if (excludeFile.exists()) {
        excludeFilter.set(excludeFile)
    }
}

tasks.withType<com.github.spotbugs.snom.SpotBugsTask> {
    ignoreFailures = true
    reports {
        create("html") {
            enabled = true
        }
    }
}

pmd {
    ruleSets = listOf()
    ruleSetConfig = resources.text.fromFile("${project.rootProject.projectDir}/config/pmd/pmd.xml")
    isIgnoreFailures = true
}

cpd {
    isIgnoreFailures = true
}

tasks.withType<Cpd> {
    reports {
        xml.setEnabled(false)
        text.setEnabled(true)
    }
    language = "java"
    minimumTokenCount = 50
    ignoreFailures = true
    source = sourceSets["main"].allJava
}

checkstyle {
    isIgnoreFailures = true
}
