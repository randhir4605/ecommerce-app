# User Management Entities — Maven Library

A reusable **JAR library** containing the JPA entities for the supplied PostgreSQL user/authentication schema. It is not a runnable Spring Boot application: it has no `main` class, datasource configuration, controllers, or repositories.

## Included entities

- `User`
- `UserCredentials`
- `UserProfile`
- `UserIdentity`
- `UserAddress`
- `Role`
- `UserRole`
- `Permission`
- `RolePermission`
- `EmailVerificationToken`
- `PasswordResetToken`
- `RefreshToken`
- `IdentityProvider` enum

Java package: `com.example.app.entity`

## 1. Build and install the library locally

Requirements: JDK 25 and Maven.

From this directory, run:

```bash
mvn clean install
```

Maven installs the JAR and its POM into your local Maven repository (usually `~/.m2/repository`). The artifact coordinates are:

```text
com.example:user-management-entities:1.0.0
```

## 2. Import it in another Maven project

Add this dependency inside the consuming project's `pom.xml`:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>user-management-entities</artifactId>
    <version>1.0.0</version>
</dependency>
```

Because the JAR is installed locally, no remote repository is required for local development. For team/CI use, publish the artifact to a Maven repository or install it in each build environment.

## 3. Register entity scanning in the consuming Spring Boot application

Spring Boot scans entities under the package of its main application class by default. If that does not include `com.example.app.entity`, add entity scanning explicitly:

```java
import com.example.app.entity.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackageClasses = User.class)
@SpringBootApplication
public class YourApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(YourApplication.class, args);
    }
}
```

If your project already scans this package, the extra `@EntityScan` may not be needed.

## Important integration notes

- The library uses Java 25 and Spring Boot 4.1.2 dependency management. Keep Hibernate/Jakarta Persistence versions compatible with your consuming application. If your application uses a different Spring Boot/Hibernate line, verify compatibility before adopting the library.
- It declares JPA, Hibernate Core, Hibernate Spatial, and JTS dependencies because the entities use persistence annotations and `UserAddress.location` uses `org.locationtech.jts.geom.Point` with `GEOGRAPHY(POINT,4326)`.
- Add the PostgreSQL JDBC driver and `spring-boot-starter-data-jpa` in the consuming application if it uses Spring Boot with PostgreSQL and Spring Data JPA; those are application infrastructure, not part of the entity library.
- The database schema and PostGIS extension must already exist. This library does not create or migrate database tables.
- Keep PostgreSQL's `LOWER(username)` unique index, partial unique indexes for default addresses, foreign keys, and CHECK constraints. Ordinary JPA annotations do not express all of them.
- `account_status` remains mapped as a `String` because the referenced `account_status` table definition was not supplied.
- The package `com.example.app.entity` is retained from the original entity project. You can rename it before first use if you have an organization-specific namespace.
