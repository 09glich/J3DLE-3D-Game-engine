package engine.Hiarachy;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import engine.Behaviors.Component;

public final class ComponentRegistry {
    private static final Map<String, Class<? extends Component>> TYPES = new LinkedHashMap<>();

    public static void register(String label, Class<? extends Component> type) {
        TYPES.put(label, type);
    }

    public static Set<Map.Entry<String, Class<? extends Component>>> entries() {
        return TYPES.entrySet();
    }

    public static Component create(Class<? extends Component> type) {
        try {
            return type.getDeclaredConstructor().newInstance(); // needs no-arg ctor
        } catch (Exception e) {
            throw new RuntimeException("Failed to create component: " + type.getName(), e);
        }
    }
}