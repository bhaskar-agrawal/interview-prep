# Notification Service - SDE2 Interview Feedback

## What's Done Well

- **Layered architecture**: Clean Controller -> Service -> Repository separation. Shows understanding of Spring conventions.
- **Strategy + Factory pattern**: `AbstractNotifier` with concrete `EmailNotifier`, `SlackNotifier`, `WhatsappNotifier` and a `NotifierFactory` is the right pattern for this problem. Easy to extend for new channels.
- **Concurrency awareness**: Use of `ConcurrentHashMap`, `synchronized` on `FileProcessor`, and even acknowledging the race condition in comments — this is good. Shows you're thinking about thread safety.
- **Lombok usage**: `@Builder`, `@Data` on models and DTOs is clean.
- **Design doc in readme**: Showing your thinking (entities, class design, concurrency discussion) before writing code is exactly what an SDE2 should do.

---

## Issues to Fix / Discuss

### 1. Compile Error in `AbstractNotifier`
```java
public  add abstract void notify(...);  // stray "add" keyword
```
This won't compile. Clean it up.

---

### 2. Race Condition in `subscribe()` (Critical)
You acknowledged this in a comment, but here's the precise issue:

```java
notfnTypeUsersMapping.computeIfAbsent(type, key -> { ... });
notfnTypeUsersMapping.computeIfPresent(type, (key, value) -> { ... });
```

These two calls are NOT atomic together — there is a window between them. Also the inner `HashSet` is not thread-safe; concurrent `add()` calls on it are unsafe even with `ConcurrentHashMap`.

The correct pattern:
```java
notfnTypeUsersMapping.computeIfAbsent(type, k -> ConcurrentHashMap.newKeySet()).add(userId);
```
`ConcurrentHashMap.newKeySet()` gives you a thread-safe Set, and `computeIfAbsent` is atomic — it returns the existing set if present or creates a new one. No need for `computeIfPresent` after.

Same issue applies to `userIdChannelsMapping`.

---

### 3. NPE in `channelsForUser()`
```java
return this.userIdChannelsMapping.get(userId).stream().toList();  // NPE if userId not in map
```
If a user is in `notfnTypeUsersMapping` but not in `userIdChannelsMapping` (partial failure during subscribe), this throws NPE. Add a null/empty check.

---

### 4. `NotifierFactory` Bypasses Spring DI
```java
this.emailNotifier = new EmailNotifier(processor);  // created with "new", not a Spring bean
```
The notifiers are manually instantiated, so they can't be injected, mocked in tests, or managed by Spring. Either inject them via `@Autowired` constructor injection, or register them as `@Component` beans.

---

### 5. Controller Mapping Issue
```java
@PostMapping
@RequestMapping("/subscribe")
```
Using `@PostMapping` and `@RequestMapping` together on the same method causes ambiguous/redundant mapping. The correct approach is just:
```java
@PostMapping("/subscribe")
```

---

### 6. Wrong HTTP Status Codes
- `subscribe` returns `200 OK` but your own readme specifies `201 Created`.
- All exceptions return `500 Internal Server Error` regardless of the cause. A bad request (missing field, invalid enum) should return `400 Bad Request`. This shows awareness of REST semantics.

---

### 7. Partial Atomicity in `subscribe()` (Design Issue)
The two maps (`notfnTypeUsersMapping` and `userIdChannelsMapping`) are updated independently. If the second update fails, the user is subscribed to the type but has no channels recorded — an inconsistent state. Worth mentioning in the interview that a proper solution would need either an atomic operation, a compensation step, or both updates wrapped in a lock.

---

### 8. Channel is Global Across Notification Types
A user's channels are stored per-user, not per-subscription. So if user A subscribes to `SERVICE_DOWN` on Slack and later to `HIGH_LOAD` on Email, both channels accumulate — A gets `SERVICE_DOWN` on both Slack and Email.

The design should ideally be:
```
Map<String, Map<NotificationType, Set<NotificationChannels>>>
// userId -> notifType -> channels
```
This is worth raising as a design trade-off discussion even if you don't implement it.

---

### 9. `FileProcessor` - Minor Issues
- Filepath is hardcoded (`./data.txt`) — should be configurable via `application.properties`.
- `BufferedWriter` is created inside try-with-resources for `FileWriter` but is itself not in the try-with-resources block. If `bufferedWriter.write()` throws, `bufferedWriter.close()` is never called. Wrap both:
```java
try (FileWriter fw = new FileWriter(filepath, true);
     BufferedWriter bw = new BufferedWriter(fw)) { ... }
```

---

### 10. Redundant Object Creation in `notify()`
`SingleNotification` and `Notification` both hold `data` and `serviceName`. `Notification` is built but never stored — only passed to the notifier. `SingleNotification` is used in the response. These two can be merged or one can be eliminated. Minor, but shows clean thinking.

---

## SDE2 Checklist Summary

| Area | Status |
|---|---|
| Design patterns (Strategy, Factory) | Good |
| Layered architecture | Good |
| Concurrency awareness | Partial — right instinct, wrong implementation |
| Thread-safe data structures | Partial — ConcurrentHashMap used but inner sets unsafe |
| Error handling | Weak — NPE risk, generic 500s |
| Spring best practices | Partial — DI bypassed in factory, mapping annotations wrong |
| REST semantics | Weak — status codes not matching |
| Extensibility of design | Good — adding a new channel is straightforward |
| Atomicity / consistency | Not addressed |

---

## Key Talking Points for the Interview

1. **Concurrency**: Explain why `computeIfAbsent` alone is sufficient and why the inner Set must also be thread-safe.
2. **Channel-per-subscription vs channel-per-user**: Proactively raise this as a design trade-off.
3. **Scalability**: If this was a real system, file-based notification would be replaced by an async queue (Kafka/SQS). The `AbstractNotifier` abstraction already supports this swap cleanly.
4. **Consistency**: Two-map updates not being atomic — how would you solve this at scale?