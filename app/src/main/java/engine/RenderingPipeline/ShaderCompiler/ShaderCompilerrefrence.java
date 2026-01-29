package engine.RenderingPipeline.ShaderCompiler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.GFX.Material.MaterialProperty;

public class ShaderCompilerrefrence {
    public class ShaderFileOutput {
        public String Vertex;
        public String Fragment;
        public Map<String, MaterialProperty> Propertys;
    }


    public interface ShaderCompilerRefrenceinterface {
        public static final List<ShaderFileOutput> Programs = null;
        static String getVertexShader(int program) { return new String(); }
        static String getFragmentShader() { return new String(); }
        static Map<String, MaterialProperty> getShaderPropertys() { return new HashMap<String, MaterialProperty>(); }
    }
}