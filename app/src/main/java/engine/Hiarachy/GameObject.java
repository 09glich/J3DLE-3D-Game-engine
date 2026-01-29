package engine.Hiarachy;

import engine.Behaviors.Transform;

public class GameObject {
    public String name;

    public Transform transform;

    public GameObject(String name) {
        this.name = name;
        this.transform = new Transform();
    }

    public GameObject() { this.transform = new Transform(); }
}
