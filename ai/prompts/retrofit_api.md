# Retrofit API (Network Layer)

This prompt defines how to define Retrofit interfaces, configure the client, handle errors, and test. It applies to projects using MVVM, Repository, and coroutines. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Adding or changing API interfaces, DTOs, error handling, interceptors, and how Repository calls the network layer. For projects using Retrofit + OkHttp with suspend and Kotlin coroutines.

---

## API interface

- **Retrofit** — Use **suspend** methods; return DTO (data class) or Response&lt;T&gt;. Avoid Call&lt;T&gt; or RxJava unless the project standardizes on them.
- **HTTP** — @GET, @POST, @PUT, @DELETE, @PATCH; path and query/body via annotations; @Path for dynamic path, @Query or @QueryMap for query params.
- **Body** — @Body with data class; for form/multipart use @FieldMap, @Part, etc.
- **Headers** — Fixed with @Headers; dynamic with @Header or OkHttp Interceptor (e.g. token).

---

## DTO and boundaries

- **DTO** — Request/response as **data class** (e.g. XxxRequest, XxxResponse); field names match JSON or use @SerializedName; nullable and defaults per API.
- **Boundary** — DTO stays in data layer; Repository maps DTO to **domain model** before return; do not expose DTO in domain or UI. Put mapping in data (e.g. data.mapper or inside Repository).

---

## Error handling

- **Exceptions** — Network (IOException), HTTP (HttpException), parse errors: catch in Repository or dedicated mapper; convert to **Result&lt;T&gt;** or **sealed class** (e.g. LoadResult.Error). Do not throw raw exceptions to ViewModel.
- **HTTP** — 4xx/5xx via Retrofit Response&lt;T&gt; or HttpException (code and body); parse error body message/code per product.
- **Timeout and retry** — Configure connect/read/write timeout in OkHttpClient; optional RetryInterceptor or retry in app (e.g. exponential backoff).
- **Non-blocking** — All network in coroutines (suspend); do not call on main thread; Repository methods are suspend or return Flow.

---

## OkHttp and Retrofit config

- **Client** — Provide one **OkHttpClient** via DI (Hilt or Koin); add LoggingInterceptor (debug only), auth Interceptor, timeouts; add CertificatePinner if the project requires pinning.
- **Retrofit** — Provide **Retrofit** via DI; baseUrl, client, ConverterFactory (Gson/Moshi). Provide API interface via @Provides or Koin single.
- **Multiple baseUrl** — Use @Url or OkHttp Interceptor to change host, or provide different Retrofit per environment (Qualifier).

---

## Caching (optional)

- **OkHttp Cache** — For GET configure Cache and CacheControl; control in Interceptor by request/response headers.
- **App cache** — For “cache then network” or “network failed use cache” do it in Repository (Flow or suspend), not mixed into Retrofit.

---

## Testing

- **MockWebServer** — In unit or integration tests use **MockWebServer** to simulate API; enqueue responses; assert path/header/body. Inject real Retrofit (baseUrl to MockWebServer) or Fake in Repository/ViewModel tests.
- **Contract** — Keep request/response samples or contract tests for critical APIs so server changes are caught.

---

## Example request (for AI)

```
Add GET /user/profile returning UserProfileDto and POST /order with body CreateOrderRequest.
- In data layer define UserProfileDto, CreateOrderRequest, OrderResponse.
- In Retrofit interface: suspend getProfile(), suspend createOrder(@Body req).
- In Repository implementation call API, map DTO to domain model, map network/parse errors to Result or sealed class.
- Add tests for Repository or ViewModel using MockWebServer.
```
