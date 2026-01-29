package engine.RenderingPipeline.ShaderCompiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import  engine.ClassHelpers.StringHelper;

import engine.GFX.Material.ShaderPropertyType;


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
        public Map<String, ShaderPropertyType> ShaderPropertys;

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
            ShaderPropertys = new HashMap<String,ShaderPropertyType>();
        }
    }

    public static class GLSLShaderPass {
        public String Vertex;
        public String Fragment;
    }


    public static GLSurfaceShader DecompileFromString(String string) {
        GLSurfaceShader shaderOutput = new GLSurfaceShader();
        GLSLShaderPass currentPass = null;

        shaderApplyLayer CurrentLayer = shaderApplyLayer.NONE;


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
                    } else if (program[0].equals("#properties")) { // Propertys Key Switch
                    } else if (program[0].equals("#pass")) {
                        currentPass = shaderOutput.GetNewpass();
                    } else if (program[0].equals("#glversion")) {
                        shaderOutput.SetVersion(Integer.parseInt(program[1]));
                    } else if (program[0].equals("#shadername")) {
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
                    
                if (CurrentLayer == shaderApplyLayer.PROPERTYS) {
                    if (!line.isEmpty()) {
                        List<String> data = StringHelper.SplitBySpaceExcludingQuotes(line);
                        
                        
                    }
                } 
            }
        } catch (IOException e) {
            System.err.println("IO Exeption thrown by shader decompiler. Line failed to read, Corrupt file or invalid file");
        }

        shaderOutput.ApplyVersion();

        return shaderOutput;
    }

    public enum shaderApplyLayer {VERTEX, FRAGMENT, PROPERTYS, NONE}
    
}
