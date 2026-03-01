package engine.Packages.Editor.EditorMainUI;

import java.io.File;
import java.util.List;

import engine.Behaviors.Component;
import engine.Behaviors.Transform;
import engine.Packages.Editor.EditorGUITool.EditorGUI;
import engine.Packages.Editor.EditorGUITool.Classes.EditorGUIWindow;
import engine.Packages.Editor.EditorGUITool.Classes.EditorGUIWindow.*;
import engine.Packages.Editor.EditorMainUI.EditorGUIElement.EditorGUIRenderer;

public class EditorUIPrimary extends Component {
    
    EditorGUIWindow EditorPrimaryWindow;
    EditorGUIWindow Hiarachy;

    SameLineConstraint TopBar;
    
    //TopBarButtons

    WindowButton fileB;
    WindowButton CreateB;
    WindowButton importB;
    WindowButton transformB;

    List<EditorGUIRenderer> GUIElement;

    // MenuBlocks
    // file

    WindowBox fileBox;

    // Create

    WindowBox createBox;

    // import

    WindowBox importBox;

    // transform

    WindowBox TransformBox;

    @Override
    public void start() {
        EditorPrimaryWindow = new EditorGUIWindow("Scene Modification Manager");

        Hiarachy = new EditorGUIWindow("Hiarachy");

        Hiarachy.addRawNode(HiarachyWindow::Render);

        //Topbar
        TopBar = EditorPrimaryWindow.addSameLineConstraint();

        fileB = EditorPrimaryWindow.createButton("File");
        CreateB = EditorPrimaryWindow.createButton("Create");
        importB = EditorPrimaryWindow.createButton("Import");
        transformB = EditorPrimaryWindow.createButton("Transform");

        TopBar.AddObject(fileB);
        TopBar.AddObject(CreateB);
        TopBar.AddObject(importB);
        TopBar.AddObject(transformB);

        // File
        
        fileBox = EditorPrimaryWindow.addWindowBox();
        fileBox.AddObject(EditorPrimaryWindow.createTextLabel("File"));

        fileBox.setEnabled(false);

        // Create

        createBox = EditorPrimaryWindow.addWindowBox();
        createBox.AddObject(EditorPrimaryWindow.createTextLabel("Create"));

        


        createBox.setEnabled(false);

        // Import

        importBox = EditorPrimaryWindow.addWindowBox();
        importBox.AddObject(EditorPrimaryWindow.createTextLabel("Import"));

        importBox.setEnabled(false);

        // Transform

        TransformBox = EditorPrimaryWindow.addWindowBox();
        TransformBox.AddObject(EditorPrimaryWindow.createTextLabel("Transform"));

        TransformBox.setEnabled(false);
    }

    private void HideBoxes() {
        fileBox.setEnabled(false);
        createBox.setEnabled(false);
        importBox.setEnabled(false);
        TransformBox.setEnabled(false);
    }

    @Override
    public void update() {
        //Menu Selection
        if (fileB.buttonPressed()) {
            HideBoxes();
            fileBox.setEnabled(true);
        }
        if (CreateB.buttonPressed()) {
            HideBoxes();
            createBox.setEnabled(true);
        }
        if (importB.buttonPressed()) {
            HideBoxes();
            importBox.setEnabled(true);
        }
        if (transformB.buttonPressed()) {
            HideBoxes();
            TransformBox.setEnabled(true);
        }

        //Scene Modifycation Manager
    }
}
