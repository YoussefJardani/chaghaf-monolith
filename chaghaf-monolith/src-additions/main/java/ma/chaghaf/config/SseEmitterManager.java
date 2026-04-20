package ma.chaghaf.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SseEmitterManager {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ObjectMapper mapper;

    public SseEmitterManager() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /** Called by ERP to subscribe to real-time events. */
    public SseEmitter subscribe() {
        String id = UUID.randomUUID().toString();
        SseEmitter emitter = new SseEmitter(0L); // never time out

        emitters.put(id, emitter);
        emitter.onCompletion(() -> remove(id));
        emitter.onTimeout(() -> remove(id));
        emitter.onError(e -> remove(id));

        try {
            emitter.send(SseEmitter.event()
                .name("connected")
                .data("{\"clientId\":\"" + id + "\",\"totalClients\":" + emitters.size() + "}"));
        } catch (Exception e) {
            remove(id);
        }

        log.info("SSE client connected: {} (total={})", id, emitters.size());
        return emitter;
    }

    private void remove(String id) {
        emitters.remove(id);
        log.debug("SSE client removed: {} (remaining={})", id, emitters.size());
    }

    /**
     * Broadcast an event to ALL connected ERP clients.
     * @param eventName  The SSE event name (e.g. "qr-scan", "new-reservation")
     * @param data       Any serialisable object
     */
    public void broadcast(String eventName, Object data) {
        if (emitters.isEmpty()) return;
        String json;
        try {
            json = mapper.writeValueAsString(data);
        } catch (Exception e) {
            log.warn("SSE serialisation error for event {}: {}", eventName, e.getMessage());
            json = "{\"error\":\"serialization_failed\"}";
        }
        String payload = json;
        emitters.forEach((id, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(payload));
            } catch (Exception ex) {
                emitters.remove(id);
            }
        });
    }

    public int getConnectedCount() {
        return emitters.size();
    }
}
