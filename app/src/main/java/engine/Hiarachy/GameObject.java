package engine.Hiarachy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import engine.Behaviors.Component;
import engine.Behaviors.Transform;

public class GameObject {
    public String name;

    public Transform transform;
    public GameObject parent;
    private final List<Component> components = new ArrayList<>();
    private final List<GameObject> children = new ArrayList<>();

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

    
    public void UpdateCall() {
        for (Component component : components) {
            component.engineStart();
            component.engineUpdate();
        }

        for (GameObject child : children) {
            child.UpdateCall();
        }
    }

    public void AfterRenderCall() {
        for (Component component : components) {
            component.engineAfterRender();
        }
        for (GameObject child : children) {
            child.AfterRenderCall();
        }
    }

    public <T extends Component> T addComponent(T component) {
        if (component == null) return null;
        if (component instanceof Transform t) {
            t.attachTo(this);
        } else {
            component.setParent(this);
        }
        component.engineAwake();
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

    public List<Component> getAllComponents() { return Collections.unmodifiableList(components); }
    public List<GameObject> getChildren() { return Collections.unmodifiableList(children); }

    //Parent Child Relationships

    public GameObject addGameObject() {
        GameObject object = new GameObject("GameObject");
        object.setParent(this);
        return object;
    }
    public GameObject addGameObject(String Name) {
        GameObject object = new GameObject(Name);
        object.setParent(this);
        return object;
    }

    private boolean isDescendantOf(GameObject potentialAncestor) {
        GameObject current = this.parent;
        while (current != null) {
            if (current == potentialAncestor) {
                return true;
            }
            current = current.parent;
        }
        return false;
    }

    public void setParent(GameObject newParent) {
        if (newParent == this) {
            throw new RuntimeException("Cannot parent gameobject to itself");
        }
        if (newParent != null && newParent.isDescendantOf(this)) {
            throw new RuntimeException("Cannot parent gameobject to one of its descendants");
        }
        if (parent == newParent) {
            return;
        }

        if (parent != null) {
            parent.children.remove(this);
        }

        parent = newParent;

        if (newParent != null && !newParent.children.contains(this)) {
            newParent.children.add(this);
        }
    }
    public GameObject getParent() {
        return parent;
    }
    public void addChild(GameObject obj) {
        if (obj == null) {
            return;
        }
        obj.setParent(this);
    }
}
