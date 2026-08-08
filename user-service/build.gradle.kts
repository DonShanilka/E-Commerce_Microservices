plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "lk.ijse"
version = "0.0.1-SNAPSHOT"
description = "user-service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2025.1.2"


dependencies {

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")


    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")


    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")


    // Monitoring
    implementation("org.springframework.boot:spring-boot-starter-actuator")


    // Spring Cloud Eureka Client
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")


    // OpenFeign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")


    // Config Client
    implementation("org.springframework.cloud:spring-cloud-starter-config")


    // RabbitMQ
    implementation("org.springframework.boot:spring-boot-starter-amqp")


    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")


    // Development
    developmentOnly("org.springframework.boot:spring-boot-devtools")


    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind")


    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


dependencyManagement {

    imports {
        mavenBom(
            "org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}"
        )
    }

}


tasks.withType<Test> {
    useJUnitPlatform()
}