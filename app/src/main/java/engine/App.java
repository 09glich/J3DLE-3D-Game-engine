package engine;

import engine.API.Graphics;
import engine.Behaviors.Camera;
import engine.Engine_Classes.Time;
import engine.Engine_Classes.Window;
import engine.GFX.Material;
//import engine.debugging.*;
import engine.GFX.Mesh;
import engine.GFX.SurfaceShader;
import engine.GFX.Material.ShaderPropertyType;
import engine.Hiarachy.GameObject;
import engine.Io.TextAsset;

import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.LockSupport;

import org.joml.Matrix4f;

import engine.RenderingPipeline.Engine_Graphics;
import engine.RenderingPipeline.RenderFactory;
import engine.RenderingPipeline.RenderFactory.Backend;
import engine.debugging.Debug;
import engine.structs.Color;
import engine.structs.Quaternion;
import engine.structs.Vector2;
import engine.structs.Vector3;


public class App {

    static int Framerate = 240;

    public static void main(String[] args) throws Exception {

        long TimeBetweenFrameCall = 1_000_000_000l/Framerate;

        Window CurrentWindow = new Window();

        CurrentWindow.Start(); 

        Debug.log("Init Graphics");
        RenderFactory.init(Backend.OPENGL);
        Engine_Graphics.init();

        StarterApp app = new StarterApp();

        app.Start();

        

        Time t = new Time();
        long last = System.nanoTime();

        while (!CurrentWindow.WindowRequestClose()) {
            Long fs = System.nanoTime();

            Engine_Graphics.beginFrame();// Begining the  Frame
            
            app.Update();

            Engine_Graphics.renderCamera(app.currentCamera);
            
            CurrentWindow.WindowUpdate();
            Engine_Graphics.endFrame();

            long elapsed = System.nanoTime() - fs;
            long remaining = TimeBetweenFrameCall - elapsed;

            if (remaining > 0) {
                LockSupport.parkNanos(remaining);
            }
            
            long Nlast = System.nanoTime();
            long LastFrameElapsed =  Nlast - last; 
            last = Nlast;
            
            //Time Update
            
            float ElapsedMiliseconds = (LastFrameElapsed / 1_000_000_000.0f);

            t.TimeScale = Math.clamp(t.TimeScale, 0, 1000);

            t.time = t.time + (1 * ElapsedMiliseconds) * t.TimeScale;
            t.deltaTime = ElapsedMiliseconds * t.TimeScale;

            t.unscaledtime = t.unscaledtime + (1 * ElapsedMiliseconds);
            t.unscaledDeltaTime = ElapsedMiliseconds;


        }

        CurrentWindow.CloseWindow();
    }
}
