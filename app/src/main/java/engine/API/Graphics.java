package engine.API;

import org.joml.Matrix4f;

import engine.GFX.*;
import engine.RenderingPipeline.Engine_Graphics;

// This is all to be completed.

public interface Graphics {

    static void DrawOnce(Matrix4f Matrix, Mesh currentMesh, Material material ) {Engine_Graphics.drawOnce(Matrix, currentMesh, material);}
}