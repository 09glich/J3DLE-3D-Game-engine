package engine.Packages.Editor.EditorGUITool.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import imgui.ImGui;

import engine.GFX.Image;
import engine.Packages.Editor.EditorGUITool.BaseEditorGUI;
import engine.structs.Vector2;

public class EditorGUIWindow extends BaseEditorGUI{
    List<WindowObject> objects;
    private int childContainerId = 0;
    

    // Classes
    public class WindowObject {
        private boolean enabled = true;

        public void update() {} 
        WindowObject() {
            this(true);
        }
        WindowObject(boolean autoAdd) {
            if (autoAdd) {
                objects.add(this);
            }
        }
        public void setEnabled(boolean value) {
            enabled = value;
        }
        public boolean isEnabled() {
            return enabled;
        }
    }

    public class WindowContainer extends WindowObject {
        protected List<WindowObject> objects;

        WindowContainer(boolean autoAdd) {
            super(autoAdd);
            objects = new ArrayList<>();
        }

        WindowContainer() {
            this(true);
        }

        @Override
        public void update() {
            for (WindowObject windowObject : this.objects) {
                if (!windowObject.isEnabled()) {
                    continue;
                }
                windowObject.update();
            }
        }

        public void AddObject(WindowObject object) {
            objects.add(object);
        }

        public void RemoveObject(int indx) {
            objects.remove(indx);
        }

        public void RemoveObject(WindowObject object) {
            objects.remove(object);
        }
    }


    public class SameLineConstraint extends WindowContainer {
        public float offset = 0f;
        public float padding = 0f;

        SameLineConstraint(boolean autoAdd) {
            super(autoAdd);
        }
        SameLineConstraint() {
            this(true);
        }

        @Override
        public void update() {
            for (int indx = 0; indx < this.objects.size(); indx++) {
                WindowObject windowObject = this.objects.get(indx);
                if (!windowObject.isEnabled()) {
                    continue;
                }
                windowObject.update();
                if (indx < this.objects.size() - 1) {
                    WindowObject nextWindowObject = this.objects.get(indx + 1);
                    if (!nextWindowObject.isEnabled()) {
                        continue;
                    }
                    ImGui.sameLine(offset, padding);
                }
            }
        }

        public void setOffset(float value) {offset = value;}
        public void setPadding(float value) {padding = value;}
    }
    public SameLineConstraint addSameLineConstraint() {
        return new SameLineConstraint(true);
    }
    public SameLineConstraint createSameLineConstraint() {
        return new SameLineConstraint(false);
    }

    public class WindowBox extends WindowContainer {
        private String boxId = "WindowBox";
        public float width = 0f;
        public float height = 0f;
        public boolean border = true;

        WindowBox(boolean autoAdd) {
            super(autoAdd);
            boxId = "WindowBox_" + childContainerId++;
        }

        WindowBox() {
            this(true);
        }

        @Override
        public void update() {
            ImGui.beginChild(boxId, width, height, border);
            super.update();
            ImGui.endChild();
        }

        public void setBoxId(String id) { boxId = id; }
        public void setWidth(float value) { width = value; }
        public void setHeight(float value) { height = value; }
        public void setSize(Vector2 size) { this.width = size.x; this.height = size.y; }
        public void setBorder(boolean value) { border = value; }
    }
    public WindowBox addWindowBox() {
        return new WindowBox(true);
    }
    public WindowBox createWindowBox() {
        return new WindowBox(false);
    }

    public class DropDown extends WindowContainer {

    }

    // Text Label
    public class WindowLabel extends WindowObject {
        String text = "Text Label";

        @Override
        public void update() {
            ImGui.text(text);
        }
        public void setText(String t) { text = t; }

        WindowLabel(String t, boolean autoAdd) { super(autoAdd); text = t; }
        WindowLabel(String t) { this(t, true); }
        public WindowLabel(boolean autoAdd) { super(autoAdd); }
        public WindowLabel() { this(true); }
    }
    public WindowLabel addTextLabel(String text) {
        return new WindowLabel(text, true);
    }
    public WindowLabel createTextLabel(String text) {
        return new WindowLabel(text, false);
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
        WindowButton (boolean autoAdd) { super(autoAdd); }
        WindowButton (String txt, boolean autoAdd) { super(autoAdd); Text = txt; }
        WindowButton () { this(true); }
        WindowButton (String txt) { this(txt, true); }
    }
    public WindowButton addButton(String Text) {
        return new WindowButton(Text, true);
    }
    public WindowButton createButton(String Text) {
        return new WindowButton(Text, false);
    }
    public WindowButton addButton() {
        return new WindowButton(true);
    }
    public WindowButton createButton() {
        return new WindowButton(false);
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

        WindowToggleButton (boolean autoAdd) { super(autoAdd); }
        WindowToggleButton (String Text1, String Text2, boolean autoAdd) { super(autoAdd); setText(Text1, Text2);}
        WindowToggleButton () { this(true); }
        WindowToggleButton (String Text1, String Text2) { this(Text1, Text2, true);}
    }
    public WindowToggleButton addToggleButton(String Text1, String Text2) {
        return new WindowToggleButton(Text1, Text2, true);
    }
    public WindowToggleButton createToggleButton(String Text1, String Text2) {
        return new WindowToggleButton(Text1, Text2, false);
    }
    public WindowToggleButton addToggleButton() {
        return new WindowToggleButton(true);
    }
    public WindowToggleButton createToggleButton() {
        return new WindowToggleButton(false);
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
        WindowImageButton (Image Image, Vector2 size, boolean autoAdd) { super(autoAdd); if (!(Image == null)) Img = Image; Size = size; }
        WindowImageButton (Image Image, Vector2 size) { this(Image, size, true); }
    }
    public WindowImageButton AddImageButton(Image Image, Vector2 Size) {
        return new WindowImageButton(Image, Size, true);
    }
    public WindowImageButton createImageButton(Image Image, Vector2 Size) {
        return new WindowImageButton(Image, Size, false);
    }

    //IMGUINode
    public class EditorRawNode extends WindowObject {
        Runnable lambda;

        EditorRawNode(boolean autoAdd, Runnable lambda) {
            super(autoAdd);
            this.lambda = lambda;
        }

        @Override
        public void update() {
            if (lambda != null) {
                lambda.run();
            }
        }

        public void setLambda(Runnable lambda) {
            this.lambda = lambda;
        }
    }
    public EditorRawNode addRawNode(Runnable lambda) {
        return new EditorRawNode(true, lambda);
    }
    public EditorRawNode createRawNode(Runnable lambda) {
        return new EditorRawNode(false, lambda);
    }

    public void AddObject(WindowObject object) {
        if (object != null) {
            this.objects.add(object);
        }
    }

    // Window Actual

    String Title = "Window";

    @Override
    public void update() {
        ImGui.begin(Title);
        for (WindowObject object : objects) {
            if (!object.isEnabled()) {
                continue;
            }
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
