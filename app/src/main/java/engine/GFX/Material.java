package engine.GFX;



import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;

import engine.RenderingPipeline.Engine_Graphics;
import engine.structs.*;

public class Material {
    // Classes
    public enum ShaderPropertyType {
        Color,
        Bool,
        Float,
        Int,
        //Vector4,
        Vector3,
        Vector2,
        Matrix4f,
        Matrix3f,
        Sampler2D,
        SamplerCubed,
        Sampler3D,
    }
    public static class MaterialProperty {
        public String CodeName;
        public String EditorName;
        public ShaderPropertyType Type;
        public Object value;

        public MaterialProperty(String CodeName, String Name, ShaderPropertyType PropertyType, Object Value) {
            this.CodeName = CodeName;
            this.EditorName = Name;
            this.Type = PropertyType;
            this.value = Value;
        }
    }

    // Values
    private int MaterialIndex;
    private boolean Dirty = true;
    private SurfaceShader CurrentShader;
    private Map<String,MaterialProperty> properties;


    //
    public Material(Map<String,MaterialProperty> properties) {
        this.properties = properties;
        this.Dirty = true;
        
        MaterialIndex = Engine_Graphics.PushMaterial(this);
    }

    public Material(SurfaceShader starterShader) {
        this.CurrentShader = starterShader;
        MaterialIndex = Engine_Graphics.PushMaterial(this);
    }

    public Material() { 
        properties = new HashMap<String, MaterialProperty>();
        MaterialIndex = Engine_Graphics.PushMaterial(this);
    }
    
    // Shader Setter
    public SurfaceShader getShader() {
        return CurrentShader;
    }

    public void setShader(SurfaceShader shader) {
        this.Dirty = true;
        this.CurrentShader = shader;
    }

    // Dirty

    public boolean getMatState() {
        return Dirty;
    }

    public void setClean() {
        this.Dirty = false;
    }

    public int getMaterialIndex() {
        return MaterialIndex;
    }


    // Material Propertys

    public Map<String, MaterialProperty> getPropertys() {
        return this.properties;
    }

    public Boolean setProperty(String Name, Object value) {
        MaterialProperty property = this.properties.get(Name);

        if (property == null) {
            return false;
        }
        if (!isCompatible(property.Type, value)) {
            return false;
        }

        property.value = value;
        this.Dirty = true;
        return true;
    }

    public void CreateProperty(String CodeName, String Name, ShaderPropertyType PropertyType, Object Value) {
        this.properties.put(CodeName, new MaterialProperty(CodeName, Name, PropertyType, Value));
    }

    public void CreateProperty(MaterialProperty property) {
        this.properties.put(property.CodeName, property);
    }

    private boolean isCompatible(ShaderPropertyType type, Object value) {
        return switch (type) {
            case Float -> value instanceof Float;
            case Int -> value instanceof Integer;
            case Bool -> value instanceof Boolean;
            case Vector2 -> value instanceof Vector2;
            case Vector3 -> value instanceof Vector3;
            case Color -> value instanceof Color;
            case Matrix4f -> value instanceof Matrix4f;
            case Sampler2D -> value instanceof Image; // whatever your texture class is
            default -> false;
        };
    }
}

