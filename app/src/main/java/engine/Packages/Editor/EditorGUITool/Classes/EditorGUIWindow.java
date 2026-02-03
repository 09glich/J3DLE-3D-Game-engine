package engine.Packages.Editor.EditorGUITool.Classes;

import java.util.ArrayList;
import java.util.List;

import imgui.ImGui;

import engine.GFX.Image;
import engine.Packages.Editor.EditorGUITool.BaseEditorGUI;
import engine.Packages.Editor.EditorGUITool.EditorGUI;
import engine.structs.Vector2;

public class EditorGUIWindow extends BaseEditorGUI{
    List<WindowObject> objects;

    // Classes
    public class WindowObject {
        public void update() {} 
        WindowObject() {
            objects.add(this);
        }
    }

    // Text Label
    public class WindowLabel extends WindowObject {
        String text = "Text Label";

        @Override
        public void update() {
            ImGui.text(text);
        }
        public void setText(String t) { text = t; }

        WindowLabel(String t) { super(); text = t; }
        public WindowLabel() { super(); }
    }
    public WindowLabel addTextLabel(String text) {
        return new WindowLabel(text);
    }

    //Button
    public class WindowButton extends WindowObject {
        String Text = "Button";
        boolean Pressed = false;


        @Override
        public void update() {
            if (ImGui.button(Text)) {
                Pressed = true;
            }
        }

        public boolean buttonPressed() {
            boolean result = Pressed;
            Pressed = false;
            return result;
        }
        public void setText(String t) { Text = t; }
        WindowButton () { super(); }
        WindowButton (String txt) { super(); Text = txt; }
    }
    public WindowButton addButton(String Text) {
        return new WindowButton(Text);
    }
    public WindowButton addButton() {
        return new WindowButton();
    }

    //Toggle Button

    public class WindowToggleButton extends WindowObject {
        String[] Text = {"Text1", "Text2"};
        int State = 0;

        @Override
        public void update() {
            if (ImGui.button(Text[State])) {
                State ++;
                State = State % 2;
            }
        }

        public boolean buttonPressed() {
            return (State == 0 ? true : false);
        }
        public void setText(String Text1, String Text2) { Text = new String[2]; Text[0] = Text1; Text[1] = Text2;}

        WindowToggleButton () { super(); }
        WindowToggleButton (String Text1, String Text2) { super(); setText(Text1, Text2);}
    }
    public WindowToggleButton addToggleButton(String Text1, String Text2) {
        WindowToggleButton Toggle = new WindowToggleButton(Text1, Text2);
        return Toggle;
    }
    public WindowToggleButton addToggleButton() {
        return new WindowToggleButton();
    }

    //Image Button
    public class WindowImageButton extends WindowObject {
        Image Img;
        Vector2 Size = new Vector2(2,2);
        boolean Pressed = false;

        @Override
        public void update() {
            if (Img == null) {
                if (ImGui.button("Image Unavailible")) {
                    Pressed = true;
                }
            } else {
                if (ImGui.imageButton(Img.getImageIndex(), Size.x, Size.y)) {
                    Pressed = true;
                }
            }   
        }

        public boolean buttonPressed() {
            boolean result = Pressed;
            Pressed = false;
            return result;
        }
        public void setImage(Image Image) { if (!(Image == null)) Img = Image; }
        WindowImageButton (Image Image, Vector2 size) { super(); if (!(Image == null)) Img = Image; Size = size; }
    }
    public WindowImageButton AddImageButton(Image Image, Vector2 Size) {
        return new WindowImageButton(Image, Size);
    }

    

    // Window Actual

    String Title = "Window";

    @Override
    public void update() {
        ImGui.begin(Title);
        for (WindowObject object : objects) {
            object.update();
        }
        ImGui.end();
    }
    
    public void setTitle(String window) {
        Title = window;
    }

    
    public EditorGUIWindow() {
        super();
        objects = new ArrayList<>();
    }

    public EditorGUIWindow(String Title) {
        super();
        objects = new ArrayList<>();
        this.Title = Title;
    }
    
}
