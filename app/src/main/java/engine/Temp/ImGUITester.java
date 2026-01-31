package engine.Temp;

import engine.Behaviors.Component;
import engine.Engine_Classes.Window;
import engine.Packages.Editor.EditorGUITool.Classes.EditorGUIWindow;
import engine.Packages.Editor.EditorGUITool.Classes.EditorGUIWindow.WindowButton;
import engine.Packages.Editor.EditorGUITool.Classes.EditorGUIWindow.WindowLabel;
import engine.Packages.Editor.EditorGUITool.Classes.EditorGUIWindow.WindowToggleButton;

public class ImGUITester extends Component{
    WindowButton button;
    WindowToggleButton toggleButton;

    @Override
    public void update() {
        if (button.buttonPressed()) System.out.println("Button Pressed");
        
    }

    @Override
    public void start() {
        System.out.println("ImGUI Tester Starting");

        EditorGUIWindow guiwindow = new EditorGUIWindow("Window");
        guiwindow.addTextLabel("Hello world");
        button = guiwindow.addButton("Press me");
        toggleButton = guiwindow.addToggleButton("String one", "String two");


    }
}
