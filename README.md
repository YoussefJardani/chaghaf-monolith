# Chaghaf Monolith 🚀

Tous les microservices fusionnés en **un seul JAR**.

## Structure

```
chaghaf-monolith/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── src/main/java/ma/chaghaf/
    ├── ChaghafApplication.java       ← Point d'entrée unique
    ├── config/
    │   ├── SecurityConfig.java       ← Sécurité unifiée (remplace tous les SecurityConfig)
    │   └── GlobalExceptionHandler.java
    ├── auth/                         ← AuthController, AuthService, User, JwtService...
    ├── subscription/
    ├── reservation/
    ├── boisson/
    ├── snack/
    ├── social/
    └── notification/
```

## Ce qui a changé vs microservices

| Avant (microservices)         | Après (monolithe)                        |
|-------------------------------|------------------------------------------|
| `@RequestHeader("X-User-Id")` | `request.getAttribute("X-User-Id")`      |
| Eureka / Discovery            | Supprimé                                 |
| Feign Clients                 | Appels directs entre services            |
| 9 JARs séparés                | 1 seul JAR                               |
| 9 ports différents            | Un seul port 8080                        |
| SecurityConfig × 9            | Un seul SecurityConfig                   |

## Lancer localement

```bash
cd chaghaf-monolith
mvn clean package -DskipTests
java -jar target/chaghaf-monolith-1.0.0.jar
```

## Lancer avec Docker

```bash
docker-compose up --build
```

## Déployer sur Render

1. Créer un **Web Service** sur Render
2. Relier votre repo GitHub
3. **Build Command**: `cd chaghaf-monolith && mvn clean package -DskipTests`
4. **Start Command**: `java -jar chaghaf-monolith/target/chaghaf-monolith-1.0.0.jar`
5. Variables d'environnement:

```
DB_HOST=dpg-d7f7dcdckfvc73dopob0-a
DB_PORT=5432
DB_NAME=chaghaf_bd
DB_USER=chaghaf_bd_user
DB_PASS=votre_mot_de_passe
JWT_SECRET=chaghaf-super-secret-key-2025-agadir-morocco-production
```

## Endpoints disponibles

| Service        | Endpoint                         |
|----------------|----------------------------------|
| Auth           | POST /api/auth/login             |
| Auth           | POST /api/auth/register          |
| Auth           | GET  /api/auth/me                |
| Subscription   | GET  /api/subscriptions/active   |
| Subscription   | GET  /api/subscriptions/packs    |
| Reservation    | GET  /api/reservations           |
| Reservation    | GET  /api/reservations/salles    |
| Boisson        | GET  /api/boissons               |
| Snack          | GET  /api/snacks/catalog         |
| Social         | GET  /api/posts                  |
| Notification   | GET  /api/notifications          |
| Health         | GET  /actuator/health            |
