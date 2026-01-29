package engine;

import java.util.concurrent.ThreadLocalRandom;

import engine.Behaviors.*;
import engine.Hiarachy.*;
import engine.RenderingPipeline.Engine_Graphics;
import engine.structs.*;
import engine.GFX.*;

import engine.GFX.Material.MaterialProperty;
import engine.GFX.Material.ShaderPropertyType;

import engine.Editor.EditorGUITool.EditorGUI;
import engine.Temp.ColorShifter;
import engine.Temp.MovementWave;
import engine.Temp.Spinner;
import engine.debugging.Debug;


public class StarterApp {

    public void Start() {
        Engine_Graphics.setClearColor(new Color(.25f, .25f, .25f));

        Debug.log("Starting Scene");
        Scene currentScene = SceneManager.newScene();

        Debug.log("Setting up camera");
        //Remember if you want to try and run the system currently you must change the directorys. I am working on making it local to the project, this will not always be the case
        
        GameObject cameraObject = currentScene.createGameObject("Main Camera");
        Camera currentCamera = cameraObject.addComponent(new Camera());
        currentScene.setScenePrimaryCamera(currentCamera);

        cameraObject.addComponent(new Spinner());

        Debug.log("Init imgui");

        GameObject imguiHandler = currentScene.createGameObject("ImGUIHandeler");
        EditorGUI gui = new EditorGUI();
        gui.setParent(imguiHandler);

        Debug.log("Setting up mesh renderer");
        // Creating a block
        GameObject object = currentScene.createGameObject("Block");
        object.addComponent(new MeshFilter("C:\\Users\\16314\\Documents\\Java_Stuffs\\3DEngine\\3D_Game_Engine\\app\\src\\main\\java\\engine\\Temp\\roundedCube.glb")); 
        
        SurfaceShader currentShader = SurfaceShader.LoadShaderFromFile("C:\\Users\\16314\\Documents\\Java_Stuffs\\3DEngine\\3D_Game_Engine\\app\\src\\main\\java\\engine\\Temp\\StandardSurface.csgl");
        Material currentMat = new Material(currentShader);
        Material[] mat = new Material[1];
        currentMat.CreateProperty(new MaterialProperty("MeshColor", null, ShaderPropertyType.Color, new Color(255, 0, 255)));
        mat[0] = currentMat;

        object.addComponent(new MeshRenderer(mat));

        object.transform.Position = new Vector3(0f,0f,-5f);

        //object.addComponent(new MovementWave());

        Material BatchMaterial = new Material(currentShader);
        Material[] BMA = new Material[1];
        BMA[0] = BatchMaterial;
        BatchMaterial.CreateProperty(new MaterialProperty("MeshColor", null, ShaderPropertyType.Color, new Color(255, 0, 255)));


        float RandRange = 100;

        for (int i = 0; i <= 500; i++) {
            GameObject go = currentScene.createGameObject("Block");
            go.addComponent(new MeshFilter("C:\\Users\\16314\\Documents\\Java_Stuffs\\3DEngine\\3D_Game_Engine\\app\\src\\main\\java\\engine\\Temp\\roundedCube.glb")); 
            
            if (ThreadLocalRandom.current().nextBoolean()) {
                go.addComponent(new MeshFilter("C:\\Users\\16314\\Documents\\Java_Stuffs\\3DEngine\\3D_Game_Engine\\app\\src\\main\\java\\engine\\Temp\\roundedCube.glb"));
            }else {
                go.addComponent(new MeshFilter("C:\\Users\\16314\\Documents\\Java_Stuffs\\3DEngine\\3D_Game_Engine\\app\\src\\main\\java\\engine\\Temp\\WeirdCube.fbx"));
            }

            go.addComponent(new MeshRenderer(BMA));

            go.transform.Position = new Vector3(
                (ThreadLocalRandom.current().nextFloat() * (RandRange * 2)) - RandRange,
                (ThreadLocalRandom.current().nextFloat() * (RandRange * 2)) - RandRange,
                (ThreadLocalRandom.current().nextFloat() * (RandRange * 2)) - RandRange
            );

            go.addComponent(new MovementWave());
        }

        GameObject ShifterGameObject = currentScene.createGameObject("ColorShifter");
        
        ColorShifter shifter = new ColorShifter();
        shifter.mat = BatchMaterial;

        ShifterGameObject.addComponent(shifter);

        
        

    }
}
