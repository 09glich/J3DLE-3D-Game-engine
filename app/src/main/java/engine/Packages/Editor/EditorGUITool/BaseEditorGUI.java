package engine.Packages.Editor.EditorGUITool;

public class BaseEditorGUI {
    public void update() {}
    public BaseEditorGUI () {
        EditorGUI.AddEditorElement(this);
    }
}
