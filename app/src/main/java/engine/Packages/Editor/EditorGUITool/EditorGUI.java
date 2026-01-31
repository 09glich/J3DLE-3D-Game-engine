package engine.Packages.Editor.EditorGUITool;

import java.util.ArrayList;
import java.util.List;

public class EditorGUI {
    private static List<BaseEditorGUI> EditorGUI;

    public static void AddEditorElement(BaseEditorGUI gui) {
        if (EditorGUI == null) {
            Init();
        }
        EditorGUI.add(gui);
    }

    public static void RemoveEditorElement (BaseEditorGUI eGui) {
        EditorGUI.remove(eGui);
    }

    public static void UpdateElements() {
        for (BaseEditorGUI egui : EditorGUI) {

            egui.update();
        }
    }

    public static void Init () {
        EditorGUI = new ArrayList<>();
    }
}
