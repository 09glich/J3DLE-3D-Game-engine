package engine.RenderingPipeline.Backend;


import engine.RenderingPipeline.ShaderCompiler.GLSLComp;
import engine.RenderingPipeline.ShaderCompiler.GLSLComp.GLSurfaceShader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import engine.Behaviors.Camera;
import engine.GFX.*;
import engine.GFX.Material.MaterialProperty;
import engine.GFX.Image.*;
import engine.structs.*;


import org.joml.Matrix4f;
import  org.lwjgl.BufferUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL30.*;

public class GLBackend implements BackendGraphics {
    public enum RenderingPass {Depth(0), Opaque(1), Transparent(2), Shadow(3);
        private int id = 0;
        RenderingPass(int id) { this.id = id; }
        public int getID() {return id;}
    }

    private Color ClearColor = Color.black;

    private List<GPUMesh> UploadedMeshes;
    private List<GPUShader> UploadedShaders;
    private List<Material> UploadedMaterials;
    private List<GPUImage> UploadedImages;

    private List<RenderSubmission> PresistantSubmissions;
    private List<RenderSubmission> PerFrameSubmissions;
    
    // Convert face Class into 3
    private IntBuffer convertToFaceIntArray(Mesh.Face[] faceArray) {

        int[] Buffer = new int[faceArray.length*3];
        
        int indx = 0;
        for(Mesh.Face f : faceArray) {
            Buffer[indx+0] =  f.vertexIndex0;
            Buffer[indx+1] =  f.vertexIndex1;
            Buffer[indx+2] =  f.vertexIndex2;

            indx+=3;
        }

        IntBuffer outputBuffer = BufferUtils.createIntBuffer(faceArray.length*3);
        outputBuffer.put(Buffer).flip();
        return outputBuffer;
    }

    private void SortSubList(List<RenderSubmission> list) {
        list.sort(Comparator.comparingLong(rs -> rs.StaticKey));
    }
  
    // Per Frame
    // Setts up Uniforms and draws Meshes
    boolean presistantDirty = false;
    private void DrawPresistant(Camera currentCamera) {
        int CurrentBoundShader = -1;

        if (presistantDirty) {
            SortSubList(PresistantSubmissions);
        }

        for (int meshIndx = 0; meshIndx < PresistantSubmissions.size(); meshIndx++) {
            RenderSubmission Submission = PresistantSubmissions.get(meshIndx);
            
            GPUMesh mesh = UploadedMeshes.get(Submission.currentMesh);
            GPUShader shader = UploadedShaders.get(Submission.shader);

            if (CurrentBoundShader != shader.FullProgram) {
                CurrentBoundShader = shader.FullProgram;
                glUseProgram(shader.FullProgram);
            }
            
            UniformUploader.UploadUniform("ModelView", shader.FullProgram, Submission.Matrix);
            UniformUploader.UploadUniform("ModelViewInverse", shader.FullProgram, new Matrix4f(Submission.Matrix).invert());
            UniformUploader.UploadUniform("ViewMat", shader.FullProgram, currentCamera.getVeiwMatrix4f());
            UniformUploader.UploadUniform("Projection", shader.FullProgram, currentCamera.getProjectionMatrix4f());

            //Uploads the Materials data
            Map <String, MaterialProperty> propertys = UploadedMaterials.get(Submission.meshmaterial).getPropertys();
            List<String> keys = new ArrayList<>(propertys.keySet());

            for (String MapElement : keys) {
                MaterialProperty currentProperty = propertys.get(MapElement);
                UniformUploader.UploadUniform(currentProperty.CodeName, shader.FullProgram, currentProperty.value);
            }
            
            glBindVertexArray(mesh.VAO);
            glDrawElements(GL_TRIANGLES, mesh.IndecieCount, GL_UNSIGNED_INT, 0);

            UniformUploader.ResetIncro();

            
        } 
    }
    // Sets up uniforms and draws dynamic
    private void DrawDynamic(Camera currentCamera) {
        int CurrentBoundShader = -1;

        SortSubList(PerFrameSubmissions);

        for (int meshIndx = 0; meshIndx < PerFrameSubmissions.size(); meshIndx++) {
            RenderSubmission Submission = PerFrameSubmissions.get(meshIndx);
            
            GPUMesh mesh = UploadedMeshes.get(Submission.currentMesh);
            GPUShader shader = UploadedShaders.get(Submission.shader);

            if (CurrentBoundShader != shader.FullProgram) {
                CurrentBoundShader = shader.FullProgram;
                glUseProgram(shader.FullProgram);
            }

            UniformUploader.UploadUniform("ModelView", shader.FullProgram, Submission.Matrix);
            UniformUploader.UploadUniform("ModelViewInverse", shader.FullProgram, Submission.Matrix.invert());
            UniformUploader.UploadUniform("ViewMat", shader.FullProgram, currentCamera.getVeiwMatrix4f());
            UniformUploader.UploadUniform("Projection", shader.FullProgram, currentCamera.getProjectionMatrix4f());

            Map <String, MaterialProperty> propertys = UploadedMaterials.get(Submission.meshmaterial).getPropertys();
            List<String> keys = new ArrayList<>(propertys.keySet());

            for (String MapElement : keys) {
                MaterialProperty currentProperty = propertys.get(MapElement);
                UniformUploader.UploadUniform(currentProperty.CodeName, shader.FullProgram, currentProperty.value);
            }

            glBindVertexArray(mesh.VAO);
            glDrawElements(GL_TRIANGLES, mesh.IndecieCount, GL_UNSIGNED_INT, 0);

            UniformUploader.ResetIncro();
        }
    }

    //Public Methods
    // Sets clear color
    public void setClearColor(Color color) {
        ClearColor = color;
    }

    // Initializes OpenGL to make it work
    public void init() {
        System.out.println("Initiating Program with OPENGL");
        createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        PresistantSubmissions = new ArrayList<> ();
        PerFrameSubmissions = new ArrayList<> ();

        UploadedShaders = new ArrayList<>();
        UploadedMeshes = new ArrayList<>();
        UploadedMaterials = new ArrayList<>();
        UploadedImages = new ArrayList<>();
    }

    // Upload Mesh
    public int PushMesh(Mesh currentMesh) {
        // Removing Duplicate meshes
        boolean meshAssetChanged = false;
        int MeshIndx = 0;
        GPUMesh meshAsset = null;
        for (GPUMesh mesh : UploadedMeshes) {
            if (mesh.MeshAsset == currentMesh) {
                meshAssetChanged = true;
                meshAsset = mesh;
                break;
            }
            MeshIndx ++;
        }
        
        if (meshAssetChanged && meshAsset != null) {
            // DELETE VBO VAO VEO ENTRYS HERE TO PREVENT GPU MEMORY LEAKS
            for (VBOEntry VBO : meshAsset.VBO) {
                glDeleteBuffers(VBO.VBO);
            }
            glDeleteVertexArrays(meshAsset.VAO);
            UploadedMeshes.remove(MeshIndx);
        }

        // Adding new meshes
        // GPU Uploading Stuff
        int VAO = glGenVertexArrays();
        int EBO = glGenBuffers();
        glBindVertexArray(VAO);

        // Faces
        IntBuffer FaceIndiciesBuffer = convertToFaceIntArray(currentMesh.Triangles);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, FaceIndiciesBuffer, GL_STATIC_DRAW);
        glBindVertexArray(VAO);
        glDrawElements(GL_TRIANGLES, FaceIndiciesBuffer.limit(), GL_UNSIGNED_INT, 0);

        // GPU Mesh Cachein
        GPUMesh newMesh = new GPUMesh();
        newMesh.MeshAsset = currentMesh;
        newMesh.VAO = VAO;
        newMesh.EBO = EBO;
        newMesh.IndecieCount = currentMesh.Triangles.length * 3;
        
        int MeshIndex = UploadedMeshes.size();
        UploadedMeshes.add(newMesh);

        // Positions
        newMesh.UploadAttributes(0, currentMesh.getpositionList(), GL_STATIC_DRAW);
        // Normals
        newMesh.UploadAttributes(1, currentMesh.getNormalList(), GL_STATIC_DRAW);
        // UV1
        newMesh.UploadAttributes(2, currentMesh.getTexCoordsList() , GL_STATIC_DRAW);
        // Colors
        newMesh.UploadAttributes(5, currentMesh.getColorList() , GL_STATIC_DRAW);

        return MeshIndex;
    }

    // Upload Shaders
    public int PushShader(SurfaceShader shader) {
        String ContentString = shader.GetShaderSource();
            GLSurfaceShader shaderCompiled = GLSLComp.DecompileFromString(ContentString);

            String Pass0VertexString = shaderCompiled.ShaderPasses.get(0).Vertex;
            String Pass0FragmentString = shaderCompiled.ShaderPasses.get(0).Fragment;

            int GLVERTEX = glCreateShader(GL_VERTEX_SHADER);
            glShaderSource(GLVERTEX, Pass0VertexString);
            glCompileShader(GLVERTEX);
            logShaderCompile(GLVERTEX, "Vertex");
            
            int GLFRAGMENT = glCreateShader(GL_FRAGMENT_SHADER);
            glShaderSource(GLFRAGMENT, Pass0FragmentString);
            glCompileShader(GLFRAGMENT);
            logShaderCompile(GLFRAGMENT, "Fragment");

            int PROGRAMPRIMARY = glCreateProgram();
            glAttachShader(PROGRAMPRIMARY, GLVERTEX);
            glAttachShader(PROGRAMPRIMARY, GLFRAGMENT);
            glLinkProgram(PROGRAMPRIMARY);
            logProgramLink(PROGRAMPRIMARY);

            glDeleteShader(GLVERTEX);
            glDeleteShader(GLFRAGMENT);

            GPUShader shaderSubmission = new GPUShader();
            shaderSubmission.FullProgram = PROGRAMPRIMARY;
            int ShaderIndex = UploadedShaders.size();
            UploadedShaders.add(shaderSubmission);

            return ShaderIndex;
    }

    class ShaderCompileError extends RuntimeException {
        ShaderCompileError(String mesage) {
            super(mesage);
        }
    }
    private void logShaderCompile(int shader, String label) {
        int status = glGetShaderi(shader, GL_COMPILE_STATUS);
        if (status == GL11.GL_FALSE) {
            String log = glGetShaderInfoLog(shader);
            throw new ShaderCompileError(label + " - " + log);
        }
    }

    class ShaderLinkError extends RuntimeException {
        ShaderLinkError(String mesage) {
            super(mesage);
        }
    }
    private void logProgramLink(int program) {
        int status = glGetProgrami(program, GL_LINK_STATUS);
        if (status == GL11.GL_FALSE) {
            String log = glGetProgramInfoLog(program);
            throw new ShaderLinkError("Program link error:\n" + log);
        }
    }

    //Cache Material
    public int PushMaterial(Material Material) {
        int Index = UploadedMaterials.size();
        UploadedMaterials.add(Material);
        return Index;
    }

    // Upload Image
    public int PushImage(Image img) {

        boolean replace = false;
        int indx = 0;
        for (GPUImage imgs : UploadedImages) {
            if (img == imgs.original) {
                glDeleteTextures(imgs.ImageBuffer);
                replace = true;
                break;
            }
            indx ++;
        }

        int TexId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, TexId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        // Image Binding

        int ColorMode = GL_RGB;

        if (img.getChannels() == 4) {
            ColorMode = GL_RGBA;
        }else if (img.getChannels() == 2) {
            ColorMode = GL_RG;
        }else if (img.getChannels() == 1) {
            ColorMode = GL_R;
        }
        glTexImage2D(GL_TEXTURE_2D, 0, ColorMode, img.getWidth(), img.getHeight(), 0, ColorMode, GL_UNSIGNED_BYTE, img.getBuffer());

        // Filter Mode
        int Mode = ((img.getFilterMode() == ImageFilterMode.LINEAR) ? GL_LINEAR : GL_NEAREST);
        int MinMode = ((img.getMipMode() == MipmapMode.ENABLED) ? ((img.getFilterMode() == ImageFilterMode.LINEAR) ? GL_LINEAR_MIPMAP_LINEAR : GL_NEAREST_MIPMAP_NEAREST) : Mode);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, MinMode);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, Mode); 

        // Wrap

        ImageWrapMode WM = img.getWrapMode();
        int WrapModeX = ((WM == ImageWrapMode.REPEAT || WM == ImageWrapMode.CLAMPY) ? GL_REPEAT : GL_CLAMP);
        int WrapModeY = ((WM == ImageWrapMode.REPEAT || WM == ImageWrapMode.CLAMPX) ? GL_REPEAT : GL_CLAMP);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, WrapModeX);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, WrapModeY);

        // Generate Mips
        if (img.getMipMode() == MipmapMode.ENABLED) {
            glGenerateMipmap(GL_TEXTURE_2D);
        }

        GPUImage imgLog = new GPUImage();
        imgLog.ImageBuffer = TexId;
        imgLog.original = img;

        if (replace) {
            UploadedImages.set(indx, imgLog);
        } else {
            UploadedImages.add(imgLog);
        }

        return TexId;
    }


    // MeshDrawing
    //Draw for one frame
    public void drawOnce(Matrix4f meshMatrix, Mesh currentMesh, Material CurrentMaterial) 
    {
        RenderSubmission submission = new RenderSubmission();
        submission.renderPass = RenderingPass.Opaque.getID();
        submission.Matrix = meshMatrix;
        submission.currentMesh = currentMesh.getMeshIndex();
        submission.meshmaterial = CurrentMaterial.getMaterialIndex();
        submission.shader = CurrentMaterial.getShader().getShaderIndex();
        submission.Build();


        PerFrameSubmissions.add(submission);
    }

    //Draw every frame
    public void submitPresistant(Matrix4f meshMatrix, Mesh currentMesh, Material CurrentMaterial, Long Layer) 
    {

        RenderSubmission submission = new RenderSubmission();
        submission.Matrix = meshMatrix;
        submission.renderPass = RenderingPass.Opaque.getID();
        submission.currentMesh = currentMesh.getMeshIndex();
        submission.meshmaterial = CurrentMaterial.getMaterialIndex();
        submission.shader = CurrentMaterial.getShader().getShaderIndex();
        submission.Build();

        PresistantSubmissions.add(submission);
    }

    // Remove from presistant que
    public void RemovePresisitant() {

    }

    //Remove all presistant
    public void ClearAllPresistant() {

    }

    public void renderCamera(Camera camera) {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        Vector2 viewSize = camera.getViewSize();
        glViewport(0, 0, (int)viewSize.x, (int)viewSize.y);
        glClear(GL_COLOR_BUFFER_BIT);

        DrawPresistant(camera);
        DrawDynamic(camera);
    }

    // Frame Organization
    public void beginFrame() {
        glClearColor(ClearColor.rf(), ClearColor.gf(), ClearColor.bf(), ClearColor.af());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    //Clears the dynamic Cue
    public void endFrame() {
        PerFrameSubmissions.clear();
    }
}

// Other Classes

class RenderSubmission {
    public Matrix4f Matrix;
    public int renderPass;
    public int currentMesh;
    public int meshmaterial;
    public int shader;

    public long StaticKey;

    public void Build() {
        this.StaticKey =  (
            ((long)this.renderPass << 60) |
            ((long)this.shader << 48) |
            ((long)this.meshmaterial << 32) |
            ((long)this.currentMesh << 16)
        );
    }
}


class GPUMesh {
    public Mesh MeshAsset;
    public int Version;

    public int IndecieCount;

    public int VAO;
    public List<VBOEntry> VBO;
    public int EBO;

    public Vector3[] Verticies;
    public int[] Triangles; 

    GPUMesh() {
        VBO = new ArrayList<VBOEntry>();
    }

    // Attribute submission
    //Vector3 Implimentation
    public void UploadAttributes(int Index, Vector3[] V3List, int usage) {
        float[] array = new float[V3List.length*3];
        int idx = 0;

        for (Vector3 v3 : V3List) {
            array[idx++] = v3.x;
            array[idx++] = v3.y;
            array[idx++] = v3.z;
        }

        FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
        buffer.put(array).flip();

        int buff = glGenBuffers();
        
        VBOEntry currentVBO = new VBOEntry();
        currentVBO.Index = Index;
        currentVBO.VBO = buff;
        this.VBO.add(currentVBO);

        glBindVertexArray(this.VAO);

        glBindBuffer(GL_ARRAY_BUFFER, currentVBO.VBO);
        glBufferData(GL_ARRAY_BUFFER, buffer, usage);

        glVertexAttribPointer(Index, 3, GL_FLOAT, false, 3*Float.BYTES, 0);
        glEnableVertexAttribArray(Index);
    }
    //Vector2 Implimentation
    public void UploadAttributes(int Index, Vector2[] V2List, int usage) {
        float[] array = new float[V2List.length*2];
        int idx = 0;

        for (Vector2 v3 : V2List) {
            array[idx++] = v3.x;
            array[idx++] = v3.y;
        }

        FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
        buffer.put(array).flip();

        int buff = glGenBuffers();

        VBOEntry currentVBO = new VBOEntry();
        currentVBO.Index = Index;
        currentVBO.VBO = buff;
        this.VBO.add(currentVBO);
        glBindVertexArray(this.VAO);

        glBindBuffer(GL_ARRAY_BUFFER, currentVBO.VBO);
        glBufferData(GL_ARRAY_BUFFER, buffer, usage);

        glVertexAttribPointer(Index, 2, GL_FLOAT, false, 2*Float.BYTES, 0);
        glEnableVertexAttribArray(Index);
    }
    //Color Implimentation
    public void UploadAttributes(int Index, Color[] ColorList, int usage) {
        float[] array = new float[ColorList.length*3];
        int idx = 0;

        for (Color v3 : ColorList) {
            array[idx++] = v3.r;
            array[idx++] = v3.g;
            array[idx++] = v3.b;
        }

        FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
        buffer.put(array).flip();

        int buff = glGenBuffers();

        VBOEntry currentVBO = new VBOEntry();
        currentVBO.Index = Index;
        currentVBO.VBO = buff;
        this.VBO.add(currentVBO);
        glBindVertexArray(this.VAO);

        glBindBuffer(GL_ARRAY_BUFFER, currentVBO.VBO);
        glBufferData(GL_ARRAY_BUFFER, buffer, usage);

        glVertexAttribPointer(Index, 3, GL_FLOAT, false, 3*Float.BYTES, 0);
        glEnableVertexAttribArray(Index);
    }
}

class UniformUploader {

    //Single Inputs
    public static void UploadUniform(String name, int program, Float Number) {
        int location = glGetUniformLocation(program, name);
        if (location != -1) {
            glUniform1f(location, Number);
        }
    }

    public static void UploadUniform(String name, int program, int Number) {
        int location = glGetUniformLocation(program, name);
        if (location != -1) {
            glUniform1i(location, Number);
        }
    }

    // Vectors
    public static void UploadUniform(String name, int program, Vector3 Position) {
        int location = glGetUniformLocation(program, name);
        if (location != -1) {
            glUniform3f(location, Position.x, Position.y, Position.z);
        }
    }

    public static void UploadUniform(String name, int program, Vector2 Position) {
        int location = glGetUniformLocation(program, name);
        if (location != -1) {
            glUniform2f(location, Position.x, Position.y);
        }
    }

    // Color
    public static void UploadUniform(String name, int program, Color Color) {
        
        int location = glGetUniformLocation(program, name);
        if (location != -1) {
            
            glUniform4f(location, Color.r/255f, Color.g/255f, Color.b/255f, Color.a/255f); // Switched from Intager space to float space
        }
    }

    // Matrixies
    public static void UploadUniform(String name, int program, Matrix4f matrix) {
        int location = glGetUniformLocation(program, name);
        if (location != -1) {
            try (MemoryStack stack = MemoryStack.stackPush()) { 
                FloatBuffer fb = stack.mallocFloat(16);
                matrix.get(fb);
                glUniformMatrix4fv(location, false, fb);
            }
        }
    } 

    static int textureUint = 0;
    public static void UploadUniform(String name, int program, Image img) {
        int location = glGetUniformLocation(program, name);
        
        if (location == -1) {
            glActiveTexture(GL_TEXTURE0 + textureUint);
            glBindTexture(GL_TEXTURE_2D, img.getImageIndex());
            glUniform1i(location, textureUint);
            textureUint ++;
        }
    }

    public static void UploadUniform(String name, int program, Object value) {
        if (value instanceof Vector3 v) { UploadUniform(name, program, v); }
        else if (value instanceof Vector2 v) { UploadUniform(name, program, v); }
        else if (value instanceof Color c) { UploadUniform(name, program, c); }
        else if (value instanceof Integer i) { UploadUniform(name, program, i); }
        else if (value instanceof Float f) { UploadUniform(name, program, f); }
        else if (value instanceof Image i) { UploadUniform(name, program, i);}
        else { throw new IllegalArgumentException("Unsupported uniform type"); }
    }

    public static void ResetIncro () {
        textureUint = 0;
    }

}

class GPUShader {
    int FullProgram;

}

class GPUImage {
    Image original;
    int ImageBuffer;
}

class VBOEntry {
    public int Index;
    public int VBO;
}

