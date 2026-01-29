package engine;

import engine.Engine_Classes.Time;
import engine.Engine_Classes.Window;

import java.util.concurrent.locks.LockSupport;

import engine.RenderingPipeline.Engine_Graphics;
import engine.RenderingPipeline.RenderFactory;
import engine.RenderingPipeline.RenderFactory.Backend;
import engine.debugging.Debug;


public class App {

    static int Framerate = 144;

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
