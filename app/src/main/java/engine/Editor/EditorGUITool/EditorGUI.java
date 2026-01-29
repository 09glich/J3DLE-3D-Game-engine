package engine.Editor.EditorGUITool;

import engine.App;
import engine.Behaviors.Component;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.glfw.ImGuiImplGlfw;
import imgui.gl3.ImGuiImplGl3;

public class EditorGUI extends Component{
    private static ImGuiImplGlfw imGuiGLFW;
    private static ImGuiImplGl3 glImpl;

    public static void GUIInit() {
        imGuiGLFW = new ImGuiImplGlfw();
        glImpl = new ImGuiImplGl3();

        ImGui.createContext();

        imGuiGLFW.init(App.getWindow().getWindowHandel(), true);
        glImpl.init("#version 330 core");
    }

    public static void Update() {

    }
}