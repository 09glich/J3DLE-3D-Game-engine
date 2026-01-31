package engine.Hiarachy;

import java.util.ArrayList;
import java.util.List;

import engine.Behaviors.Camera;
import engine.Behaviors.Component;

public class Scene {
    private final List<GameObject> gameObjects = new ArrayList<>();
    private Camera currentCamera;

    public GameObject createGameObject(String name) {
        GameObject go = new GameObject(name);
        addGameObject(go);
        return go;
    }

    public void addGameObject(GameObject go) {
        if (go != null) {
            gameObjects.add(go);
        }
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void update() {
        for (GameObject go : gameObjects) {
            for (Component c : go.getAllComponents()) {
                c.engineStart();
                c.engineUpdate();
            }
        }
    }


    public void AfterRender () {
        for (GameObject go : gameObjects) {
            for (Component c : go.getAllComponents()) {
                c.afterRender();
            }
        }
    }

    public void setScenePrimaryCamera(Camera camera) {
        currentCamera = camera;
    }

    public Camera getScenePrimaryCamera() {
        return currentCamera;
    }
}
