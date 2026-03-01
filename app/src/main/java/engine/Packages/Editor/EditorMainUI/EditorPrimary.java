package engine.Packages.Editor.EditorMainUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import engine.Hiarachy.GameObject;

public class EditorPrimary {
    List<GameObject> Selected = new ArrayList<GameObject>();

    public void setSelection(List<GameObject> selection) {
        Selected = selection;
    }

    public void addToSelection(GameObject object) {
        Selected.add(object);
    }

    public List<GameObject> getSelection() {
        return Collections.unmodifiableList(Selected); 
    }
}
