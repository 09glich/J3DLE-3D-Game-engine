package engine.Behaviors;

import engine.Hiarachy.GameObject;

public class Component {
    
    private GameObject gameObject;
    private Transform transform;

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
