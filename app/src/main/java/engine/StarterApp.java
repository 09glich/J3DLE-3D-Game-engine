package engine;

import java.util.concurrent.ThreadLocalRandom;

import engine.Behaviors.*;
import engine.Hiarachy.*;
import engine.Packages.Editor.EditorGUITool.EditorGUIInitializer;
import engine.RenderingPipeline.Engine_Graphics;
import engine.structs.*;
import engine.GFX.*;
import engine.GFX.Image.ImageFilterMode;
import engine.GFX.Material.MaterialProperty;
import engine.GFX.Material.ShaderPropertyType;
import engine.Temp.ColorShifter;
import engine.Temp.ImGUITester;
import engine.Temp.MovementWave;
import engine.Temp.Spinner;
import engine.debugging.Debug;


public class StarterApp {

    public void Start() {
        String AssetPath = "C:\\Users\\16314\\Documents\\Java_Stuffs\\3DEngine\\3D_Game_Engine\\app\\\\src\\main\\\\java\\engine";

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
        EditorGUIInitializer gui = new EditorGUIInitializer();
        imguiHandler.addComponent(gui);

        Debug.log("Setting up mesh renderer");
        // Creating a block
        GameObject object = currentScene.createGameObject("Block");
        object.addComponent(new MeshFilter(AssetPath + "\\Temp\\roundedCube.glb")); 
        
        SurfaceShader currentShader = SurfaceShader.LoadShaderFromFile(AssetPath + "\\Temp\\StandardSurfaceColor.csgl");
        Material currentMat = new Material(currentShader);
        Material[] mat = new Material[1];
        currentMat.CreateProperty(new MaterialProperty("MeshColor", ShaderPropertyType.Color, new Color(255, 0, 255)));
        mat[0] = currentMat;

        object.addComponent(new MeshRenderer(mat));

        object.transform.Position = new Vector3(0f,0f,-5f);

        //object.addComponent(new MovementWave());

        Material BatchMaterial = new Material(currentShader);
        Material[] BMA = new Material[1];
        BMA[0] = BatchMaterial;
        BatchMaterial.CreateProperty(new MaterialProperty("MeshColor", ShaderPropertyType.Color, new Color(255, 0, 255)));


        float RandRange = 100;

        for (int i = 0; i <= 500; i++) {
            GameObject go = currentScene.createGameObject("Block");
            go.addComponent(new MeshFilter(AssetPath + "\\Temp\\roundedCube.glb")); 
            
            if (ThreadLocalRandom.current().nextFloat() >= .5f) {
                go.addComponent(new MeshFilter(AssetPath + "\\Temp\\roundedCube.glb"));
            }else {
                go.addComponent(new MeshFilter(AssetPath + "\\Temp\\WeirdCube.fbx"));
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

        // Companion Cube
        GameObject CompanionCube = currentScene.createGameObject("CompanionCube");
        CompanionCube.addComponent(new MeshFilter(AssetPath + "\\Temp\\CompanionCube.fbx"));

        SurfaceShader textureShader = SurfaceShader.LoadShaderFromFile(AssetPath + "\\Temp\\StandardSurfaceTexture.csgl");
        Image companionTexture = Image.LoadImageFromFile(AssetPath + "\\Temp\\CompanionCube.png");
        companionTexture.setFilterMode(ImageFilterMode.NEAREST);

        Material companionMaterial = new Material(textureShader);
        Material[] compMatBatch = new Material[1];
        compMatBatch[0] = companionMaterial;

        companionMaterial.CreateProperty("_Texture", ShaderPropertyType.Sampler2D, companionTexture);

        CompanionCube.addComponent(new MeshRenderer(compMatBatch));

        CompanionCube.transform.Position = new Vector3(-2,0, -2);
        
        GameObject IGUITest = currentScene.createGameObject("IMGUITester");
        IGUITest.addComponent(new ImGUITester());
        
        

    }
}
