package engine.Behaviors;

import java.nio.file.Path;

import engine.GFX.Mesh;

public class MeshFilter extends Component {
    public Mesh[] currentMesh;

    public MeshFilter(String meshpath) {
        currentMesh = Mesh.loadMeshFromFile(Path.of(meshpath));
    }
}
