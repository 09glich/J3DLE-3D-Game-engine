package engine;

import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

import org.joml.Matrix4f;

import engine.Behaviors.Camera;
import engine.Engine_Classes.Time;
import engine.GFX.Material;
import engine.GFX.Material.ShaderPropertyType;
import engine.GFX.Mesh;
import engine.GFX.SurfaceShader;
import engine.Hiarachy.GameObject;
import engine.Io.TextAsset;
import engine.RenderingPipeline.Engine_Graphics;
import engine.debugging.Debug;
import engine.structs.Color;
import engine.structs.Quaternion;
import engine.structs.Vector2;
import engine.structs.Vector3;

public class StarterApp {
    GameObject CameraObject;
    public Camera currentCamera;
    Material mat;
    Material mat2;

    Mesh[] currentMeshes;
    Mesh[] WeirdCubeMesh;

    int c = 0;
    float Ltime = 0;

    Color[] cs;

    public void Update() {
        //currentCamera.setVeiwSize(CurrentWindow.getSize());

        if (Time.time - Ltime > 1.0) {
            Ltime = Time.time;
            c++;
            c=c%3;
        }

        //CameraObject.transform.Position = new Vector3((float)Math.sin(Math.toRadians(t.time * 180)), 0, (float)Math.cos(Math.toRadians(t.time * 180))).Scale(2.5f);
        CameraObject.transform.Rotation = Quaternion.Euler(new Vector3(0,Time.time * 5,0));

        Float SinTime = (float)Math.abs(Math.sin(Time.time + 3.14159620));
        mat.setProperty("MeshColor", cs[c]); // CHANGED PROPERTY EVERY FRAME

        for (int i = 0; i < 20; i++) 
        {
            Engine_Graphics.drawOnce(new Matrix4f().translate(Quaternion.Euler(0,(Time.time * 90) + ((360/20f) * i),0).transform(new Vector3(0, 5, -20)) ).rotate(Quaternion.Euler(0, Time.time * 360, 0)).scale((float)Math.sin(Time.time)), WeirdCubeMesh[0], mat);
        }
    }

    public void Start() {
        cs = new Color[3];
        cs[0] = Color.red;
        cs[1] = Color.green;
        cs[2] = Color.blue;

        Debug.log("Loading mesh assets");
        currentMeshes = Mesh.loadMeshFromFile(Path.of("C:\\Users\\16314\\Documents\\Java_Stuffs\\3DEngine\\3D_Game_Engine\\app\\src\\main\\java\\engine\\Temp\\RoundedCube.glb"));
        WeirdCubeMesh = Mesh.loadMeshFromFile(Path.of("C:\\Users\\16314\\Documents\\Java_Stuffs\\3DEngine\\3D_Game_Engine\\app\\src\\main\\java\\engine\\Temp\\WeirdCube.fbx"));

        Debug.log("Loading Material and Shaders");
        TextAsset ShaderFile = new TextAsset(Path.of("C:\\Users\\16314\\Documents\\Java_Stuffs\\3DEngine\\3D_Game_Engine\\app\\src\\main\\java\\engine\\Temp\\StandardSurface.csgl"));

        mat = new Material();
        mat.CreateProperty("MeshColor", "Color", ShaderPropertyType.Color , new Color(0, 0, 0));

        mat2 = new Material();
        mat2.CreateProperty("MeshColor", "Color", ShaderPropertyType.Color , new Color(1, 0, 1));

        SurfaceShader shader = new SurfaceShader();

        shader.setShaderSourceFilePath(ShaderFile);
        mat.setShader(shader) ;
        mat2.setShader(shader);
        

        Debug.log("Setting up game objects");

        CameraObject = new GameObject();
        currentCamera = new Camera();
        currentCamera.setParent(CameraObject);
        currentCamera.FeildOfVeiw = 70f;
        currentCamera.CameraMode = currentCamera.CameraMode.PERSPECTIVE;
        currentCamera.setVeiwSize(new Vector2(1920, 1080));
        

        Debug.log("Setting up mesh rendering");

        Quaternion rotQuat = new Quaternion();
        rotQuat.rotationXYZ(
            (float)Math.toRadians(20),
            (float)Math.toRadians(45),
            (float)Math.toRadians(50)
        );

        //Engine_Graphics.submitPresistant(new Matrix4f().translate(new Vector3(0,2,-5)).rotate(rotQuat), currentMeshes[0], mat, 0l);
        //Engine_Graphics.submitPresistant(new Matrix4f().translate(new Vector3(2,0,-5)), WeirdCubeMesh[0], mat, 0l);

        float BoxDistance = 500f;
        for (int i = 0; i < 500; i++) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    Engine_Graphics.submitPresistant(new Matrix4f().translate(new Vector3((ThreadLocalRandom.current().nextFloat()*BoxDistance) - BoxDistance/2 ,(ThreadLocalRandom.current().nextFloat()*BoxDistance) - BoxDistance/2,(ThreadLocalRandom.current().nextFloat()*BoxDistance) - BoxDistance/2)), WeirdCubeMesh[0], mat, 0l);
                } else {
                    Engine_Graphics.submitPresistant(new Matrix4f().translate(new Vector3((ThreadLocalRandom.current().nextFloat()*BoxDistance) - BoxDistance/2 ,(ThreadLocalRandom.current().nextFloat()*BoxDistance) - BoxDistance/2,(ThreadLocalRandom.current().nextFloat()*BoxDistance) - BoxDistance/2)), WeirdCubeMesh[0], mat2, 0l);
                }
                
            } else {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    Engine_Graphics.submitPresistant(new Matrix4f().translate(new Vector3((ThreadLocalRandom.current().nextFloat()*BoxDistance) - BoxDistance/2 ,(ThreadLocalRandom.current().nextFloat()*BoxDistance) - BoxDistance/2,(ThreadLocalRandom.current().nextFloat()*BoxDistance) - BoxDistance/2)), currentMeshes[0], mat, 0l);
                } else {
                    Engine_Graphics.submitPresistant(new Matrix4f().translate(new Vector3((ThreadLocalRandom.current().nextFloat()*BoxDistance) - BoxDistance/2 ,(ThreadLocalRandom.current().nextFloat()*BoxDistance) - BoxDistance/2,(ThreadLocalRandom.current().nextFloat()*BoxDistance) - BoxDistance/2)), currentMeshes[0], mat2, 0l);
                }            }
        }

        Debug.log("Everything is loaded");

        Engine_Graphics.setClearColor(new Color(.1f, .1f, .1f));
    }
}
