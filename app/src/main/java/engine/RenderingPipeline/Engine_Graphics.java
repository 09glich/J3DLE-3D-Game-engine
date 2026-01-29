package engine.RenderingPipeline;

import engine.Behaviors.Camera;
import engine.GFX.*;
import engine.RenderingPipeline.Backend.BackendGraphics;
import engine.structs.Color;

import org.joml.Matrix4f;

public final class Engine_Graphics {
    private static BackendGraphics backend;

    // Helpers
    public static void setClearColor(Color ClearColor) {backend.setClearColor(ClearColor);}

    // Upload pushing
    public static int PushMesh(Mesh currentMesh) {return backend.PushMesh(currentMesh);}
    public static int PushShader(SurfaceShader shader) {return backend.PushShader(shader);}
    public static int PushMaterial(Material material) {return backend.PushMaterial(material);}
    public static int PushImage(Image img) {return backend.PushImage(img);}

    //Draw
    public static void drawOnce(Matrix4f meshMatrix, Mesh currentMesh, Material material) {backend.drawOnce(meshMatrix, currentMesh, material);}
    public static void submitPresistant(Matrix4f meshMatrix, Mesh currentMesh, Material material, Long Layer) {backend.submitPresistant(meshMatrix, currentMesh, material, Layer);}
    public static void removePresistant(int indx) {}
    public static void ClearAllPresistant() {}


    // Render Buffer
    public static void renderCamera(Camera camera) {backend.renderCamera(camera);}


    // Engine Level
    public static void beginFrame() {backend.beginFrame();}
    public static void endFrame() {backend.endFrame();}
    public static void init() {if (backend == null) {backend = RenderFactory.GetPipeline(); backend.init();}}
}