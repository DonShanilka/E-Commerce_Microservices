# ai-service

Backend-only AI service scaffold for `E-Commerce_Microservices`.

## Includes
- `POST /api/v1/ai/chat` — AI shopping assistant grounded in Product Service catalog
- `POST /api/v1/ai/compare` — compare existing product IDs
- `POST /api/v1/ai/recommendations` — recommendations from caller-supplied category/brand/history
- `GET /api/v1/ai/recommendations/users/{userId}` — recommendations grounded in that user’s Order Service history (requires `X-AI-Internal-Key`)
- `POST /api/v1/ai/search` — AI-assisted natural-language product search (not vector embeddings yet)
- `POST /api/v1/ai/reviews/analyze` — sentiment analysis for a review
- `POST /api/v1/ai/forecast` — cautious estimate from supplied historical unit sales
- `GET /actuator/health`

## Requirements
Java 25 and an OpenAI-compatible Chat Completions API.

## Environment variables
```powershell
$env:PRODUCT_SERVICE_BASE_URL = "http://localhost:8082"
$env:AI_API_BASE_URL = "https://api.groq.com/openai/v1"
$env:AI_API_KEY = "your-api-key"
$env:AI_MODEL = "openai/gpt-oss-20b"
$env:ORDER_SERVICE_BASE_URL = "http://localhost:8085"
$env:AI_INTERNAL_API_KEY = "a-long-random-secret-shared-only-by-internal-backend-services"
```
Never commit the API key. Set it in your IDE run configuration or shell environment. `AI_API_BASE_URL` is the provider base URL; `/chat/completions` is appended automatically.

## Operations

Configure `DB_PASSWORD` in each database-backed service before restarting it; database passwords were removed from checked-in configuration. Order and Review Service also require the shared `AI_INTERNAL_API_KEY` to enable their AI-only endpoints. AI requests are limited by client IP (default 60/minute; set `AI_RATE_LIMIT_PER_MINUTE`) and the product catalog is cached briefly (default 30 seconds). Prometheus metrics are available at `/actuator/prometheus`; provider request, failure, token-usage (when supplied by the provider), and character-volume counters are exported. Cost should be calculated from your provider’s current price sheet and actual usage data.

## Run
The Gradle wrapper is included in this service. From PowerShell, set the required environment variables and run:
```powershell
.\gradlew.bat bootRun
```
Default port: 8087. Product Service is expected at `http://localhost:8082` and catalog endpoint `GET /api/v1/products`. If your actual API response differs, update `catalog()` in `AiController.java`.


## Other service security and catalog setup

- User Service exposes `POST /api/v1/users/authenticate` with JSON `{"email":"...","password":"..."}` and returns a 15-minute bearer JWT signed with HS256. New passwords use BCrypt; legacy plaintext passwords are upgraded after a successful login. Public registration always creates a `CUSTOMER`; assign the first `ADMIN` through a controlled database migration/administrative process.
- User profile reads/updates require a bearer token and are limited to the owner or an administrator. User listing/deletion and product create/update/delete require the `ADMIN` role.
- Product `GET /api/v1/products` now accepts `page` (zero-based, default 0), `size` (default 20, maximum 100), `query`, `category`, and `brand`. It returns a page of summaries without image data; `GET /{id}` still returns the full product.
- Set the same `JWT_SECRET_BASE64` value in User Service, Product Service, and the server-side frontend configuration. Generate a random secret with `openssl rand -base64 32`; keep it out of source control and never expose it with a `NEXT_PUBLIC_` prefix. Set `DB_PASSWORD` for every database-backed service and `RABBITMQ_USERNAME`/`RABBITMQ_PASSWORD` for Product, Order, and Inventory services. The RabbitMQ guest fallback was removed.
- Existing plain-text user passwords migrate to BCrypt when that account logs in. The previously exposed PostgreSQL password is not changed by these code changes; rotate it in PostgreSQL and then update `DB_PASSWORD`.

## Database-backed endpoints

The AI service reads product, order, and review information through their owning service APIs, so each service remains the owner of its database. Set the same long random `AI_INTERNAL_API_KEY` in AI, Order, and Review Service. Send it in `X-AI-Internal-Key` for internal data routes. The user-history recommendation route also requires `X-Authenticated-User`, `X-User-Assertion-Expiry` (Unix seconds, at most five minutes ahead), and `X-User-Assertion`: Base64 URL-safe HMAC-SHA256 without padding over `userId + "\n" + expiry`, signed with `AI_INTERNAL_API_KEY`. A trusted backend must authenticate the caller, ensure the requested user ID is authorized, then sign that identity assertion. Never expose the shared key to browser clients. Internal data routes fail closed when the key is unset.

```bash
curl -H "X-AI-Internal-Key: $AI_INTERNAL_API_KEY" "http://localhost:8087/api/v1/ai/forecast/products/17?periodsAhead=2"

# Only a trusted backend that has already authorized user 42 should create this assertion.
USER_ID=42
EXPIRY=$(($(date +%s) + 300))
ASSERTION=$(python3 -c 'import base64,hashlib,hmac,sys; print(base64.urlsafe_b64encode(hmac.new(sys.argv[1].encode(), (sys.argv[2]+"\n"+sys.argv[3]).encode(), hashlib.sha256).digest()).decode().rstrip("="))' "$AI_INTERNAL_API_KEY" "$USER_ID" "$EXPIRY")
curl -H "X-AI-Internal-Key: $AI_INTERNAL_API_KEY" \
  -H "X-Authenticated-User: $USER_ID" \
  -H "X-User-Assertion-Expiry: $EXPIRY" \
  -H "X-User-Assertion: $ASSERTION" \
  "http://localhost:8087/api/v1/ai/recommendations/users/$USER_ID?limit=5"
```

## Example requests
```bash
curl -X POST http://localhost:8087/api/v1/ai/chat -H 'Content-Type: application/json' -d '{"message":"I need a laptop for programming under Rs. 250,000"}'
curl -X POST http://localhost:8087/api/v1/ai/compare -H 'Content-Type: application/json' -d '{"productIds":[1,2],"purpose":"software engineering"}'
curl -X POST http://localhost:8087/api/v1/ai/recommendations -H 'Content-Type: application/json' -d '{"preferredCategories":["Laptop"],"preferredBrands":["Lenovo"],"budget":250000,"limit":5,"purchaseHistory":[{"category":"Laptop","brand":"Lenovo"}]}'
curl -X POST http://localhost:8087/api/v1/ai/search -H 'Content-Type: application/json' -d '{"query":"comfortable chair for long working hours","limit":5}'
```

### 5. Review sentiment analysis
```bash
curl -X POST http://localhost:8087/api/v1/ai/reviews/analyze -H 'Content-Type: application/json' -d '{"review":"The product is good, but the battery life is poor."}'
```

### 6. Demand forecasting
Pass historical sales points from your Order Service; do not send fabricated sample data in production.
```bash
curl -X POST http://localhost:8087/api/v1/ai/forecast -H 'Content-Type: application/json' -d '{"history":[{"date":"2026-09-01","unitsSold":12},{"date":"2026-09-08","unitsSold":15},{"date":"2026-09-15","unitsSold":14},{"date":"2026-09-22","unitsSold":18}],"periodsAhead":2}'
```

Generate the shared JWT secret locally without saving it in the repository:

```bash
openssl rand -base64 32
```

Use the generated value for `JWT_SECRET_BASE64` in User Service, Product Service, and the server-side frontend. Any service holding this HMAC secret can issue JWTs, so only trusted services should receive it.

## Current limitations

- User Service issues end-user HS256 JWTs. A trusted backend must still validate the caller and authorize access before signing the separate short-lived HMAC assertion described above for AI user-history routes. The shared JWT secret must remain server-side.
- Product Service supports pagination and filtering. AI Service follows catalog pages, caches the result briefly, and caps traversal at 10,000 products; very large catalogs should use a dedicated server-side search integration.
- Forecasts use up to 180 days of completed sales, zero-fill dates without orders, and return a bounded baseline with uncertainty. Promotions, stockouts, holidays, and marketing activity are not available in current data.
- Provider token counts are reported when the provider returns them. Cost is not estimated because model pricing is provider- and date-dependent.
- The checked-in database passwords were removed from service configuration. Set `DB_PASSWORD` before restarting services and rotate the exposed database credential in PostgreSQL; configuration changes do not rotate the database account itself.
