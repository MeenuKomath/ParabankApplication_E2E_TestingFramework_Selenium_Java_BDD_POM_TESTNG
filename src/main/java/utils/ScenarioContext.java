package utils;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {

    private static final ThreadLocal<Map<String, Object>> context = ThreadLocal.withInitial(HashMap::new);

    public static void set(String key, Object value) {

        context.get().put(key, value);
    }

    public static <T> T get(String key, Class<T> type) {

        return type.cast(context.get().get(key));
    }

    public static void clear() {

        context.remove();
    }
}
