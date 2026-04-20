/*
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  SECURITY CONFIG — Changes for chaghaf-monolith              ║
 * ╚══════════════════════════════════════════════════════════════╝
 *
 * In:  chaghaf-monolith/src/main/java/ma/chaghaf/config/SecurityConfig.java
 *
 * 1) Add "/api/catalog" and "/api/catalog/**" to permitAll (public catalog for mobile)
 * 2) Keep "/api/admin/**" behind authentication (already handled by anyRequest().authenticated())
 * 3) Add an AsyncSupportedDispatcherServlet config so SSE works properly (optional)
 *
 * ─── Find this block in SecurityConfig.filterChain() ───────────
 *
 *   .requestMatchers(
 *       "/api/auth/login",
 *       "/api/auth/register",
 *       "/api/auth/refresh",
 *       "/api/auth/health",
 *       "/actuator/**"
 *   ).permitAll()
 *
 * ─── Replace with ───────────────────────────────────────────────
 *
 *   .requestMatchers(
 *       "/api/auth/login",
 *       "/api/auth/register",
 *       "/api/auth/refresh",
 *       "/api/auth/health",
 *       "/api/catalog",          // public catalog (mobile app)
 *       "/api/catalog/**",
 *       "/actuator/**"
 *   ).permitAll()
 *
 * ─── application.yml addition ───────────────────────────────────
 *
 * Add under spring: mvc: to allow SSE (async) requests:
 *
 *   spring:
 *     mvc:
 *       async:
 *         request-timeout: -1   # disable async timeout for SSE
 *
 * ─── No other changes needed ────────────────────────────────────
 * The JwtAuthenticationFilter already sets request attributes for admin routes.
 */
