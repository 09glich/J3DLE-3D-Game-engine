package engine.Packages.Editor.EditorMainUI;

import java.util.List;

import engine.Hiarachy.GameObject;
import engine.Hiarachy.Scene;
import engine.Hiarachy.SceneManager;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

public class HiarachyWindow {
    
    public static void RecursiveSearch(GameObject obj) {
        int flags = 0;
        if (obj.getChildren().isEmpty()) flags = ImGuiTreeNodeFlags.Leaf;
        //if (go == selected)
        flags |= ImGuiTreeNodeFlags.OpenOnArrow;

        ImGui.pushID(System.identityHashCode(obj));
        boolean Open = ImGui.treeNodeEx("| " + obj.name, flags);

        if (ImGui.isItemClicked()) {

        }

        if (Open) {
            for (GameObject go : obj.getChildren()) {
                RecursiveSearch(go);
            }
            ImGui.treePop();
        }

        ImGui.popID();
    }

    public static void Render() {
        Scene currentScene = SceneManager.getActiveScene();
        if (currentScene == null) {
            return;
        }
        List<GameObject> objects = currentScene.getGameObjects();

        for (GameObject object : objects) {
            if (object.getParent() == null) {
                RecursiveSearch(object);
            }
        }
    }
}
