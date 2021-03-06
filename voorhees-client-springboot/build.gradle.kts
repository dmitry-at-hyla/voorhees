dependencies {
    api(project(":voorhees-client"))

    compileOnly("org.springframework.boot:spring-boot:2.1.8.RELEASE")
    compileOnly("org.springframework:spring-webmvc:5.1.9.RELEASE")

    testApi(project(":voorhees-client-fuel"))
    testImplementation("junit:junit:4.12")
    testImplementation("org.mock-server:mockserver-netty:5.3.0")
    testImplementation("com.fasterxml.jackson.core:jackson-core:2.9.9")
    testImplementation("com.fasterxml.jackson.core:jackson-annotations:2.9.9")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.9.9")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.1.8.RELEASE")
    testImplementation("org.springframework.boot:spring-boot-starter-web:2.1.8.RELEASE")
}
