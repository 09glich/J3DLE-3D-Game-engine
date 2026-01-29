package engine.GFX;

import engine.RenderingPipeline.Engine_Graphics;
import engine.structs.*;

import java.nio.IntBuffer;
import java.nio.file.Path;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

public class Mesh {
    public class Vertex {
        public Vector3 position;
        public Color color;
        public Vector3 normal;
        public Vector2 uvCoordinate;

        Vertex() {
            this.position = new Vector3(0,0,0);
            this.normal = new Vector3(0,0,0);
            this.color = new Color(0,0,0);
            this.uvCoordinate = new Vector2(0,0);
        }
    }

    public class Face{
        public int vertexIndex0;
        public int vertexIndex1;
        public int vertexIndex2;
    }

    public Path filepath;

    public int Version = 0;
    public boolean Dynamic = false;

    public Vertex[] Verticies;
    public Face[] Triangles; 

    private int MeshIndex;

    public int getMeshIndex() {
        return MeshIndex;
    }

    public Mesh() {
        MeshIndex = Engine_Graphics.PushMesh(this);
    }

    public void BUILD() {
        MeshIndex = Engine_Graphics.PushMesh(this);
    }

    //public Vector3[] Verticies;
    //public Vector3[] VertexNormals;
    //public Color[] VertexColors;
    //public Vector2[] VertexUV;

    public Vector3[] getpositionList() 
    {
        Vector3[] positions = new Vector3[this.Verticies.length];
        int indx = 0;
        for (Vertex vertex : Verticies) {
            positions[indx] = vertex.position;
            indx++;
        }
        return positions;
    }
    public Vector3[] getNormalList() 
    {
        Vector3[] normals = new Vector3[this.Verticies.length];
        int indx = 0;
        for (Vertex vertex : Verticies) {
            normals[indx] = vertex.normal;
            indx++;
        }
        return normals;
    }
    public Color[] getColorList() 
    {
        Color[] colors = new Color[this.Verticies.length];
        int indx = 0;
        for (Vertex vertex : this.Verticies) {
            colors[indx] = vertex.color;
            indx++;
        }
        return colors;

    }
    public Vector2[] getTexCoordsList() 
    {
        Vector2[] texCoords = new Vector2[this.Verticies.length];
        int indx = 0;
        for (Vertex vertex : this.Verticies) {
            texCoords[indx] = vertex.uvCoordinate;
            indx++;
        }
        return texCoords;
    }


    public static Mesh[] loadMeshFromFile(Path currentpath) {
        String currentStringPath = currentpath.toString();

        AIScene scene = Assimp.aiImportFile(currentStringPath,
            Assimp.aiProcess_Triangulate |
            Assimp.aiProcess_GenNormals |
            Assimp.aiProcess_JoinIdenticalVertices |
            Assimp.aiProcess_FlipUVs
        );

        if (scene == null) {
            throw new RuntimeException("Faild to load model:" + Assimp.aiGetErrorString());
        }
        
        int meshcount = scene.mNumMeshes();
        PointerBuffer meshes = scene.mMeshes();

        Mesh[] meshOutput = new Mesh[meshcount];

        for(int meshindx = 0; (meshindx < meshcount); meshindx++) {
            AIMesh mesh = AIMesh.create(meshes.get(meshindx));

            Mesh CMesh = new Mesh();
            

            // Vertex Array Construction
            AIVector3D.Buffer VertexBuffer = mesh.mVertices();

            int vertexCount = VertexBuffer.limit();
            
            Vertex[] Verts = new Vertex[vertexCount];

            for(int vertexIndex = 0; vertexIndex < VertexBuffer.remaining(); vertexIndex++) {
               AIVector3D vertex = VertexBuffer.get(vertexIndex);
               Verts[vertexIndex] = CMesh.new Vertex();
               Verts[vertexIndex].position = new Vector3(vertex.x(), vertex.y(), vertex.z());
            }
            CMesh.Verticies = Verts;

            // Triangle Array Construction

            AIFace.Buffer FaceBuffer = mesh.mFaces();
            int FaceBufferLength = FaceBuffer.limit();
            Face[] FaceOutput = new Face[FaceBufferLength];

            for (int FaceIndex = 0; FaceIndex < FaceBufferLength; FaceIndex++) {
                AIFace currentFace = FaceBuffer.get(FaceIndex);
                IntBuffer FaceIndicies = currentFace.mIndices();
                Face WrittenFace = CMesh.new Face();
                
                
                WrittenFace.vertexIndex0 = FaceIndicies.get(0);
                WrittenFace.vertexIndex1 = FaceIndicies.get(1);
                WrittenFace.vertexIndex2 = FaceIndicies.get(2);

                FaceOutput[FaceIndex] = WrittenFace;
            }

            CMesh.Triangles = FaceOutput;
            
            // Normal Array Construction

            AIVector3D.Buffer NormalBuffer = mesh.mNormals();
            if (NormalBuffer != null) {
                int VertexNormalCount = NormalBuffer.limit();
                
                for (int _VertexIndex = 0; _VertexIndex < VertexNormalCount; _VertexIndex++) {
                    Vertex currentVertex = CMesh.Verticies[_VertexIndex];
                    AIVector3D VertNormal = NormalBuffer.get(_VertexIndex);
                    
                    currentVertex.normal = new Vector3(
                        VertNormal.x(),
                        VertNormal.y(),
                        VertNormal.z()
                    );  
                }
            }

            // UV Array Construction

            AIVector3D.Buffer UVBuffer = mesh.mTextureCoords(0);
            //int UVBL = UVBuffer.limit();

            if (UVBuffer != null) {
                for(int VertexIndex = 0; VertexIndex < vertexCount; VertexIndex++) {
                    Vertex vert = CMesh.Verticies[VertexIndex];
                    AIVector3D uvCoord = UVBuffer.get(VertexIndex);

                    vert.uvCoordinate = new Vector2(
                        uvCoord.x(), 
                        uvCoord.y()
                    );
                }
            }
            

            // Color array construction

            // Tangant Array construction

            meshOutput[meshindx] = CMesh;
        }
        Assimp.aiReleaseImport(scene);
        return meshOutput;
    }

}
