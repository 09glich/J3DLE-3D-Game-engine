package engine.Packages.Editor.EditorMainUI.EditorGUIElement;

import engine.Packages.Editor.EditorGUITool.Classes.EditorGUIWindow;
import engine.Packages.Editor.EditorGUITool.Classes.EditorGUIWindow.WindowObject;

public class EditorGUIRenderer {
    boolean enabled;

    public void Render(WindowObject container) {
        if (enabled) {
            OnGUI(container);
        }
    }

    public void Start(EditorGUIWindow Container) {}
    public void OnGUI(WindowObject container) {}

    public void SetActive(boolean enabled) {this.enabled = enabled;}
}
