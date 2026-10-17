package lk.ijse.aiservice;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class AiRateLimitFilter extends OncePerRequestFilter {
    private static final class Bucket {
        private double tokens;
        private long updatedAt;
        private volatile long lastSeen;
        private Bucket(double capacity, long now) { tokens = capacity; updatedAt = now; lastSeen = now; }
    }

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final AtomicLong requests = new AtomicLong();
    private final int capacity;
    private final double refillPerSecond;

    public AiRateLimitFilter(@Value("${ai.rate-limit.requests-per-minute:60}") int requestsPerMinute) {
        capacity = Math.max(1, requestsPerMinute);
        refillPerSecond = capacity / 60.0;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (!request.getRequestURI().startsWith("/api/v1/ai/")) {
            chain.doFilter(request, response);
            return;
        }
        if (request.getContentLengthLong() > 65536) {
            response.setStatus(413);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":413,\"error\":\"payload_too_large\",\"message\":\"Request body exceeds 64 KiB.\"}");
            return;
        }
        String key = request.getRemoteAddr();
        long now = System.currentTimeMillis();
        Bucket bucket = buckets.computeIfAbsent(key, ignored -> new Bucket(capacity, now));
        boolean allowed;
        synchronized (bucket) {
            bucket.tokens = Math.min(capacity, bucket.tokens + (now - bucket.updatedAt) * refillPerSecond / 1000.0);
            bucket.updatedAt = now;
            bucket.lastSeen = now;
            allowed = bucket.tokens >= 1;
            if (allowed) bucket.tokens -= 1;
        }
        if (requests.incrementAndGet() % 1000 == 0 && buckets.size() > 10000) {
            buckets.entrySet().removeIf(entry -> now - entry.getValue().lastSeen > 120000);
        }
        if (!allowed) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":429,\"error\":\"rate_limit_exceeded\",\"message\":\"Too many requests. Retry later.\"}");
            return;
        }
        chain.doFilter(request, response);
    }
}
