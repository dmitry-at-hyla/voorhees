import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.withType<KotlinCompile> {
    kotlinOptions.javaParameters = true
}

dependencies {
    api(project(":voorhees-core"))

    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
    compileOnly("com.fasterxml.jackson.core:jackson-core:2.9.9")
    compileOnly("com.fasterxml.jackson.core:jackson-annotations:2.9.9")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.9.9")
    compileOnly("org.springframework:spring-webmvc:5.1.9.RELEASE")

    testImplementation("junit:junit:4.12")
    testImplementation("com.fasterxml.jackson.core:jackson-core:2.9.9")
    testImplementation("com.fasterxml.jackson.core:jackson-annotations:2.9.9")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.9.9")
    testImplementation("org.springframework:spring-webmvc:5.1.9.RELEASE")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.1.8.RELEASE")
    testImplementation("org.springframework.boot:spring-boot-starter-web:2.1.8.RELEASE")
    testImplementation("org.springframework.security:spring-security-config:5.1.6.RELEASE")
    testImplementation("org.springframework.security:spring-security-test:5.1.6.RELEASE")
}
