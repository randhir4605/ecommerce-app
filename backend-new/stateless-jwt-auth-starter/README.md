# Stateless JWT Auth Spring Boot Starter

A reusable Spring Boot 4 starter that auto-configures stateless JWT bearer authentication. Add the starter dependency to a compatible servlet-based Spring Boot application, configure the signing secret and issuer, and the starter supplies a JWT security filter chain automatically unless the application already defines its own `SecurityFilterChain`.

This project targets **Spring Boot 4.1.2 and Java 25**. It uses the Spring Boot 4 starter artifact names.

## What it configures

- `SecurityFilterChain` with `SessionCreationPolicy.STATELESS`.
- Bearer-token JWT decoding and validation using HS256, the configured signing key, expiration/standard timestamp validation, and the configured issuer.
- Every request requires a valid JWT except the configured public paths and (when enabled) the login endpoint.
- Form login, HTTP Basic, CSRF, and the framework logout endpoint are disabled for this bearer-token-only setup.
- A BCrypt `PasswordEncoder`, unless the consuming application defines its own.
- A `POST /api/auth/login` endpoint (enabled by default) only when the consuming application provides a `UserDetailsService`. It authenticates username/password through Spring Security and returns a signed JWT.
- JWT `authorities` are included in tokens and restored as Spring Security authorities on authenticated requests. The standard `scope` / `scp` claim handling is preserved too.

## What remains application-specific

The starter intentionally does **not** ship a repository or a `UserDetailsService`. Each application owns its repository queries, account lookup, database model, and any account-status rules. Provide a `UserDetailsService` bean to enable the starter's login endpoint. If the application has its own `SecurityFilterChain`, the starter backs off and that application must configure stateless JWT security itself.

This is a shared-secret HS256 setup: any application that signs or validates the same tokens must securely share the same secret. For deployments where separate services should only validate tokens and not be able to create them, prefer asymmetric signing (for example, RS256) or a dedicated identity provider.

## 1. Install the starter locally

Requirements: JDK 25 and Maven.

From the extracted project root (the directory containing the parent `pom.xml`), run:

```bash
mvn clean install
```

The build installs the auto-configuration module and the starter into your local Maven repository.

## 2. Add one dependency to the consuming application's `pom.xml`

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>stateless-jwt-auth-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

The `com.example` group ID is a placeholder for this local sample. Change it before publishing your own library under a real organization/group ID.

## 3. Configure the JWT key and issuer

Generate a strong random secret, then put its Base64 value in an environment variable. For example, on macOS/Linux:

```bash
export JWT_SECRET_BASE64="$(openssl rand -base64 32)"
```

Do not commit the generated secret to source control. Each environment should supply its secret through environment configuration or a secret manager.

Example `application.yml`:

```yaml
app:
  jwt:
    enabled: true
    secret-base64: ${JWT_SECRET_BASE64}
    issuer: c-store
    access-token-ttl: 15m
    login-endpoint-enabled: true
    login-path: /api/auth/login
    public-paths:
      - /api/public/**
      - /actuator/health
```

The decoded secret must contain at least 32 random bytes. `issuer` must match the JWT `iss` claim. The login path is permitted automatically while the starter login endpoint is enabled.

## 4. Provide the application's user lookup

For the starter's built-in login endpoint, provide a `UserDetailsService` in the consuming application. The starter does not decide how your application's database stores users or credentials.

For example, if your application has a `UserCredentialsRepository` returning your existing `UserCredentials` entity:

```java
@Configuration
public class AuthenticationUserConfiguration {

    @Bean
    UserDetailsService userDetailsService(UserCredentialsRepository repository) {
        return username -> repository.findByUsernameIgnoreCase(username)
                .map(credentials ->
                        org.springframework.security.core.userdetails.User
                                .withUsername(credentials.getUsername())
                                .password(credentials.getPasswordHash())
                                // Replace with the authorities loaded for this user.
                                .authorities(List.of())
                                .build())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
    }
}
```

Required imports for this example include `java.util.List`, `org.springframework.context.annotation.Bean`, `org.springframework.context.annotation.Configuration`, `org.springframework.security.core.userdetails.UserDetailsService`, and `org.springframework.security.core.userdetails.UsernameNotFoundException`. Import `UserCredentialsRepository` from your application and `UserCredentials` from your entity library. The password stored in the database must be a BCrypt hash if you use the default `BCryptPasswordEncoder`.

The example deliberately uses `List.of()` for authorities to match a minimal authentication setup; load real roles/permissions if the consuming application needs authorization checks such as `hasAuthority("ORDER_CREATE")`.

## 5. Try it

Login (replace the example username/password with a user already stored in your application's database):

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"demo","password":"your-password"}'
```

The response has `access_token`, `token_type`, and `expires_in`. Use the token on protected endpoints:

```bash
curl http://localhost:8080/api/orders \
  -H 'Authorization: Bearer <access_token>'
```

A request to a protected path without a valid token receives an unauthorized response. An expired token, a bad signature, or a token with a different issuer is rejected.

## Settings

| Property | Default | Purpose |
|---|---|---|
| `app.jwt.enabled` | `true` | Set `false` to disable this starter's auto-configuration. |
| `app.jwt.secret-base64` | Required | Base64-encoded HS256 secret; at least 32 bytes after decoding. |
| `app.jwt.issuer` | `user-management-auth` | JWT issuer to put into and validate from the `iss` claim. Set explicitly in each application. |
| `app.jwt.access-token-ttl` | `15m` | Lifetime of tokens issued by the built-in login endpoint. |
| `app.jwt.login-endpoint-enabled` | `true` | Registers the built-in login endpoint if a `UserDetailsService` bean is present. |
| `app.jwt.login-path` | `/api/auth/login` | Full request path for the login endpoint. |
| `app.jwt.public-paths` | `/api/public/**` | Additional paths permitted without a JWT. |

If you disable the built-in login endpoint but have your own login route, add that route to `app.jwt.public-paths` so clients can obtain a token without already having one.

## Important integration notes

- Remove your old `SecurityFilterChain` bean if you want the starter's chain to be created automatically. If an application defines its own chain, the starter backs off rather than replacing custom security rules.
- The starter is for servlet-based Spring MVC applications, not WebFlux.
- Keep JWTs in the `Authorization: Bearer ...` header. CSRF is disabled because this configuration assumes bearer tokens are not automatically attached by the browser as cookies.
- The decoded JWT principal is a Spring Security `Jwt`, not your JPA `User` entity. Use the token subject (`sub`) as the principal name or look up the application user when needed.
- This generated project has been structurally reviewed, but a full Maven compile could not be run in the generation environment because Maven is not installed and the available JDK is Java 21 rather than the configured Java 25.
