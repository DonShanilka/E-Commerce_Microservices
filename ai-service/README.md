# ai-service

Backend-only AI service scaffold for `E-Commerce_Microservices`.

## Includes
- `POST /api/v1/ai/chat` — AI shopping assistant grounded in Product Service catalog
- `POST /api/v1/ai/compare` — compare existing product IDs
- `POST /api/v1/ai/recommendations` — initial category/brand/history-based recommendations
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
```
Never commit the API key. Set it in your IDE run configuration or shell environment. `AI_API_BASE_URL` is the provider base URL; `/chat/completions` is appended automatically.

## Run
The Gradle wrapper is included in this service. From PowerShell, set the required environment variables and run:
```powershell
.\gradlew.bat bootRun
```
Default port: 8087. Product Service is expected at `http://localhost:8082` and catalog endpoint `GET /api/v1/products`. If your actual API response differs, update `catalog()` in `AiController.java`.

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

## Important limitations
- This first version fetches the catalog and sends a bounded candidate list to the AI; for a large catalog, add pagination/filtering to Product Service.
- Recommendations accept purchase-history summaries from the caller. They are not yet connected to User Service or Order Service.
- Semantic search here is AI-assisted relevance selection, not embedding/vector similarity.
- Review sentiment analysis accepts review text directly; integrating it automatically with Review Service is a follow-up step.
- Demand forecasting accepts historical sales observations from the caller; integrate Order Service and validate sufficient real history before using forecasts for inventory decisions.
- Add authentication, authorization, rate limiting, tests, and provider-specific timeout/retry handling before production.



