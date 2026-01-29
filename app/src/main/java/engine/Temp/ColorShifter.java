package engine.Temp;

import engine.Behaviors.Component;
import engine.Engine_Classes.Time;
import engine.GFX.Material;
import engine.structs.*;

public class ColorShifter extends Component {
    public Material mat;

    @Override
    public void update() {
        mat.setProperty("MeshColor", Color.FromHSV(Time.time * 90, 1, 1));
    }
}
