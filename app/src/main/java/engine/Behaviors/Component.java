package engine.Behaviors;

import engine.Hiarachy.GameObject;

public class Component {
    
    private GameObject gameObject;
    private Transform transform;
    private boolean started = false;
    private boolean enabled = true;

    // Lifecycle (override in subclasses)
    public void start() {}
    public void update() {}
    public void afterRender() {}

    public final void engineStart() {
        if (!started) {
            started = true;
            start();
        }
    }

    public final void engineUpdate() {
        if (enabled) {
            update();
        }
    }

    public final void engineAfterRender() {
        if (enabled) {
            afterRender();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    //Getters
    public Transform transform() {
        return this.transform;
    }
    public GameObject gameObject () {
        return this.gameObject;
    }
    
    // Setters
    public void setParent(Transform transform) {
        this.gameObject = transform.gameObject();
        this.transform = transform;

    }
    public void setParent(GameObject gameObject) {
        this.gameObject = gameObject;
        this.transform = gameObject.transform;
    }
}
