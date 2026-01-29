package engine.RenderingPipeline.Backend;

import engine.Behaviors.Camera;
import engine.GFX.*;
import engine.structs.Color;



import org.joml.Matrix4f;

public interface BackendGraphics {

    // Helpers
    default void setClearColor(Color color) {}

    // Draw Functions
    default void drawOnce(Matrix4f meshMatrix, Mesh currentMesh, Material material) {}
    default void submitPresistant(Matrix4f meshMatrix, Mesh currentMesh, Material material, Long Layer) {}
    default void removePresistant(int indx) {}
    default void ClearAllPresistant() {}

    //Asset Loading
    default int PushMesh(Mesh currentMesh) {return -1;}
    default int PushShader(SurfaceShader shader) {return -1;}
    default int PushMaterial(Material shader) {return -1;}
    default int PushImage(Image img) {return -1;}

    // Render 
    default void renderCamera(Camera camera) {}

    // Engine Level
    default void init() {}
    default void beginFrame() {}
    default void endFrame() {}
    


}