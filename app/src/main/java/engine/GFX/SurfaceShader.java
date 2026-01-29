package engine.GFX;

import java.nio.file.Path;
import java.util.Map;

import engine.GFX.Material.MaterialProperty;
import engine.Io.TextAsset;
import engine.RenderingPipeline.Engine_Graphics;

public class SurfaceShader {
    TextAsset ShaderFile;
    private int ShaderIndex;
    
    Map<String, MaterialProperty> ShaderPropertys;

    public void SetMaterialPropertys(Map<String, MaterialProperty> ShaderPropertys) {
        this.ShaderPropertys = ShaderPropertys;
    }

    public Map<String, MaterialProperty> getMaterialPropertys() {
        return this.ShaderPropertys;
    }

    public String GetShaderSource() {

        return this.ShaderFile.ReadString();
    }

    public void setShaderSourceFilePath(TextAsset ShaderFile) {
        this.ShaderFile = ShaderFile;
    }

    public int getShaderIndex() {
        return ShaderIndex;
    }

    public SurfaceShader() {
        //ShaderIndex = Engine_Graphics.PushShader(this);
    }

    public void BUILD() {
        ShaderIndex = Engine_Graphics.PushShader(this);
    }

    public static SurfaceShader LoadShaderFromFile(String file) {
        SurfaceShader newshader = new SurfaceShader();

        newshader.ShaderFile = new TextAsset(Path.of(file));

        newshader.BUILD();
        return newshader;
    }
}
