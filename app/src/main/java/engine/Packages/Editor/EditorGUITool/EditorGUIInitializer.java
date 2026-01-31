package engine.Packages.Editor.EditorGUITool;

import java.util.ArrayList;
import java.util.List;

import engine.App;
import engine.Behaviors.Component;
import imgui.ImGui;
import imgui.glfw.ImGuiImplGlfw;
import imgui.gl3.ImGuiImplGl3;

public class EditorGUIInitializer extends Component{
    private static ImGuiImplGlfw imGuiGLFW;
    private static ImGuiImplGl3 glImpl;

    public static void GUIInit() {
        imGuiGLFW = new ImGuiImplGlfw();
        glImpl = new ImGuiImplGl3();

        ImGui.createContext();

        imGuiGLFW.init(App.getWindow().getWindowHandel(), true);
        glImpl.init("#version 330 core");
    }

    @Override
    public void start() {
        System.out.println("Initializing ImGUI");
        GUIInit();
        EditorGUI.Init();
    }

    @Override
    public void afterRender() {
        imGuiGLFW.newFrame();
        ImGui.newFrame();
        EditorGUI.UpdateElements();
        ImGui.render();
        glImpl.renderDrawData(ImGui.getDrawData());
    }
}