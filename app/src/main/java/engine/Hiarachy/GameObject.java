package engine.Hiarachy;

import java.util.ArrayList;
import java.util.List;

import engine.Behaviors.Component;
import engine.Behaviors.Transform;

public class GameObject {
    public String name;

    public Transform transform;
    private final List<Component> components = new ArrayList<>();

    public GameObject(String name) {
        this.name = name;
        this.transform = new Transform();
        this.transform.attachTo(this);
        components.add(this.transform);
    }

    public GameObject() { 
        this.name = "GameObject";
        this.transform = new Transform();
        this.transform.attachTo(this);
        components.add(this.transform);
    }

    public <T extends Component> T addComponent(T component) {
        if (component == null) return null;
        if (component instanceof Transform t) {
            t.attachTo(this);
        } else {
            component.setParent(this);
        }
        components.add(component);
        return component;
    }

    public <T extends Component> T getComponent(Class<T> type) {
        for (Component c : components) {
            if (type.isInstance(c)) {
                return type.cast(c);
            }
        }
        return null;
    }

    public <T extends Component> List<T> getComponents(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (Component c : components) {
            if (type.isInstance(c)) {
                result.add(type.cast(c));
            }
        }
        return result;
    }

    public List<Component> getAllComponents() {
        return components;
    }
}
