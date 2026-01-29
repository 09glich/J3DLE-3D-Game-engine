package engine.Hiarachy;

public final class SceneManager {
    private static Scene activeScene;

    private SceneManager() {}

    public static void setActiveScene(Scene scene) { activeScene = scene; }
    public static Scene getActiveScene() { return activeScene; }
    public static Scene newScene() {Scene currentScene = new Scene(); activeScene = currentScene; return currentScene;}

    public static void update() {
        if (activeScene != null) activeScene.update();
    }
}