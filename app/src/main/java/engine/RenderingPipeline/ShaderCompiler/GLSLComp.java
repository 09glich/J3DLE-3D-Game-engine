package engine.RenderingPipeline.ShaderCompiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import  engine.ClassHelpers.StringHelper;
import engine.GFX.Material.MaterialProperty;
import engine.GFX.Material.ShaderPropertyType;
import engine.structs.Color;
import engine.structs.Vector2;
import engine.structs.Vector3;


public class GLSLComp {

    private static class ShaderParseExeption extends RuntimeException {
        ShaderParseExeption(String ExeptionText) {
            super(ExeptionText);
        }
    }

    public static class GLSurfaceShader {
        public String ShaderName;
        private int GLVersion = 330;
        public List <GLSLShaderPass> ShaderPasses;
        public Map<String, MaterialProperty> ShaderPropertys;

        public void Addpass(GLSLShaderPass pass) {
            ShaderPasses.add(pass);
        }
        public GLSLShaderPass GetNewpass() {
            GLSLShaderPass pass = new GLSLShaderPass();
            pass.Vertex = "";
            pass.Fragment = "";
            ShaderPasses.add(pass);
            return pass;
        }
        public void ApplyVersion() {
            for (GLSLShaderPass ShaderPass : ShaderPasses) {
                ShaderPass.Vertex = ReplaceVersionLine(ShaderPass.Vertex);
                ShaderPass.Fragment = ReplaceVersionLine(ShaderPass.Fragment);
            }
        }
        public void SetVersion(int GLSLVersion) {
            this.GLVersion = GLSLVersion;
            this.ApplyVersion();
        }
        private String ReplaceVersionLine(String source) {
            if (source == null || source.isEmpty()) {
                return "#version " + GLVersion;
            }

            int nlIndex = source.indexOf('\n');
            String newline = "\n";
            int restStart = -1;

            if (nlIndex >= 0) {
                if (nlIndex > 0 && source.charAt(nlIndex - 1) == '\r') {
                    newline = "\r\n";
                }
                restStart = nlIndex + 1;
            } else {
                int crIndex = source.indexOf('\r');
                if (crIndex >= 0) {
                    newline = "\r";
                    restStart = crIndex + 1;
                }
            }

            String firstLine = (restStart == -1) ? source : source.substring(0, restStart);
            String trimmed = firstLine.trim();

            if (trimmed.startsWith("#version")) {
                if (restStart == -1) {
                    return "#version " + GLVersion;
                }
                return "#version " + GLVersion + newline + source.substring(restStart);
            }

            if (restStart == -1) {
                return "#version " + GLVersion + newline + source;
            }

            return "#version " + GLVersion + newline + source;
        }

        public GLSurfaceShader() {
            ShaderPasses = new ArrayList<GLSLShaderPass>();
            ShaderPropertys = new HashMap<String,MaterialProperty>();
        }
    }

    public static class GLSLShaderPass {
        public String Vertex;
        public String Fragment;
    }

    
    public static Map<String, MaterialProperty> getMaterialPropertys(String Input) {
        Map<String,MaterialProperty> Props = new HashMap<>();

        BufferedReader stringLines = new BufferedReader(new StringReader(Input));

        try {
            int Line = 0;
            for (String line; (line = stringLines.readLine()) != null;) {
                Line++;
                if (line.startsWith("#P")) {
                    String[] Propertys = line.split(" ");
                    if (Propertys[0].equals("#P")) {
                        ShaderPropertyType ptype = ShaderPropertyType.Bool;
                        Object Property = null;

                        if (Propertys.length <= 3) {throw new RuntimeException("Property present but data not found. Line " + Line ); }

                        if (Propertys[2] == "Float") {
                            ptype = ShaderPropertyType.Float;

                            try {
                                Property = Float.parseFloat(Propertys[3]);
                            } catch(NumberFormatException error) {
                                throw new RuntimeException("Failed to parse float. Reason: Malformed at line :" + Line + "with this error " + error);
                            }
                        } else if (Propertys[2] == "Int") {
                            ptype = ShaderPropertyType.Int;

                            try {
                                Property = Integer.parseInt(Propertys[3]);
                            } catch(NumberFormatException error) {
                                throw new RuntimeException("Failed to parse int. Reason: Malformed at line :" + Line + "with this error " + error);
                            }
                            
                        } else if (Propertys[2] == "Boolean") {
                            ptype = ShaderPropertyType.Bool;

                            if (Propertys[3].toLowerCase().equals("true")) {
                                Property = true;
                            } else if (Propertys[3].toLowerCase().equals("false")) {
                                Property = false;
                            } else {
                                throw new RuntimeException("Failed to parse boolean. expected true or false got " + Propertys[3]+ " Line : " + Line);
                            }

                        } else if (Propertys[2] == "Color") {
                            ptype = ShaderPropertyType.Color;

                            Color c = new Color();

                            try {
                                c.setR(Float.parseFloat(Propertys[4]));
                                c.setG(Float.parseFloat(Propertys[5]));
                                c.setB(Float.parseFloat(Propertys[6]));
                                c.setA(Float.parseFloat(Propertys[7]));
                            } catch (NumberFormatException error) {
                                throw new RuntimeException("Failed to parse Color. Malformed Input. Line : " + Line);
                            }

                            Property = c;

                        } else if (Propertys[2] == "Vector3") {
                            ptype = ShaderPropertyType.Vector3;

                            Vector3 vec = new Vector3(0,0,0);

                            try {
                                vec.x = Float.parseFloat(Propertys[4]);
                                vec.y = Float.parseFloat(Propertys[5]);
                                vec.z = Float.parseFloat(Propertys[6]);
                            } catch (NumberFormatException error) {
                                throw new RuntimeException("Failed to parse Color. Malformed Input. Line : " + Line);
                            }

                            Property = vec;
                        } else if (Propertys[2] == "Vector2") {
                            ptype = ShaderPropertyType.Vector2;

                            Vector2 vec = new Vector2(0,0);
                            try {
                                vec.x = Float.parseFloat(Propertys[4]);
                                vec.y = Float.parseFloat(Propertys[5]);
                                
                            } catch (NumberFormatException error) {
                                throw new RuntimeException("Failed to parse Color. Malformed Input. Line : " + Line);
                            }

                            Property = vec;
                        } else if (Propertys[2] == "Image") {
                            ptype = ShaderPropertyType.Sampler2D;

                            Property = null;

                        } else if (Propertys[2] == "ImageCube") {
                            ptype = ShaderPropertyType.SamplerCubed;

                            Property = null;
                        }

                        MaterialProperty property1 = new MaterialProperty(Propertys[1], ptype, Propertys);
                        Props.put(Propertys[1], property1);
                    }
                }
            }
        } catch ( IOException e ) {

        }

        return Props;
    }

    public static GLSurfaceShader DecompileFromString(String string) {
        GLSurfaceShader shaderOutput = new GLSurfaceShader();
        GLSLShaderPass currentPass = null;

        shaderApplyLayer CurrentLayer = shaderApplyLayer.NONE;

        shaderOutput.ShaderPropertys = getMaterialPropertys(string);

        BufferedReader br = new BufferedReader(new StringReader(string));
        try {
            int LineNumber = -1;
            for (String line; (line = br.readLine()) != null;) {
                line = line.trim();
                LineNumber ++;

                // Tags
                if (line.startsWith("#")) {
                    String[] program = line.split(" ");
                    program[0] = program[0].toLowerCase();
                    if (program[0].equals("#vertex")) { // Vertex Key switch
                        if (program.length > 1 && program[1].equals("end")) {
                            CurrentLayer = shaderApplyLayer.NONE;
                        } else {
                            CurrentLayer = shaderApplyLayer.VERTEX;
                        }
                    } else if (program[0].equals("#fragment")) { // Fragment Key Switch
                        if (program.length > 1 && program[1].equals("end")) {
                            CurrentLayer = shaderApplyLayer.NONE;
                        } else {
                            CurrentLayer = shaderApplyLayer.FRAGMENT;
                        }

                    } else if (program[0].equals("#p")) {
                    } else if (program[0].equals("#pass")) {
                        currentPass = shaderOutput.GetNewpass();
                    } else if (program[0].equals("#glversion")) {
                        shaderOutput.SetVersion(Integer.parseInt(program[1]));
                    } else if (program[0].equals("#shadername")) {
                        System.out.println(program[1]);
                        shaderOutput.ShaderName = program[1];
                    } else  {
                        throw new ShaderParseExeption("Cannot parse shader because tag not recognized. Key:" + program[0] + " Line:" + LineNumber);
                    }

                    continue;
                }
                
                //Code Split

                if (currentPass != null) {
                    if (CurrentLayer == shaderApplyLayer.VERTEX) {
                        currentPass.Vertex = currentPass.Vertex + line + "\n";
                    } else if (CurrentLayer == shaderApplyLayer.FRAGMENT) {
                        currentPass.Fragment = currentPass.Fragment + line + "\n";
                    }
                }
                    
                
            }
        } catch (IOException e) {
            System.err.println("IO Exeption thrown by shader decompiler. Line failed to read, Corrupt file or invalid file");
        }

        shaderOutput.ApplyVersion();

        return shaderOutput;
    }

    public enum shaderApplyLayer {VERTEX, FRAGMENT, NONE}
    
}
