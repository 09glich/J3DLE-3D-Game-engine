package engine.Behaviors;

import engine.GFX.Material;
import engine.GFX.Mesh;
import engine.Hiarachy.GameObject;
import engine.RenderingPipeline.Engine_Graphics;

public class MeshRenderer extends Component {
    public Material material[];
    private MeshFilter filter;

    public MeshRenderer(Material[] material) {
        this.material = material;
    }
    
    @Override
    public void start() {
        GameObject currentObject = gameObject();
        filter = currentObject.getComponent(MeshFilter.class);
    }

    @Override
    public void update() {
        for(Mesh m : filter.currentMesh) {
            Engine_Graphics.drawOnce(transform().getTRXMatrix(), m, material[0]);
        }
    }
}
