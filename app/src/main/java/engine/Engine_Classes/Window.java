package engine.Engine_Classes;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryStack;

import engine.structs.Vector2;


public class Window {

    private long window;
    private int width;
    private int height;
    
    private void WindowCheck() throws Exception {
        if (window == 0) throw new Exception("Window not initialized. cannot start window");
    }

    public void Start() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!GLFW.glfwInit()) throw new IllegalStateException("GLFW Initialization Failure");

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        window = GLFW.glfwCreateWindow(1920, 1080, "Window", 0,0);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            GLFW.glfwGetWindowSize(window, w, h);
            width = w.get(0);
            height = h.get(0);
        }

        GLFW.glfwSetWindowSizeCallback(window, (win, w, h) -> {
            width = w;
            height = h;
        });

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(window);

        
    }
    
    public void WindowUpdate() throws Exception {
        WindowCheck();

        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    public boolean WindowRequestClose() {return GLFW.glfwWindowShouldClose(window);}

    public void CloseWindow() throws Exception {
        WindowCheck();

        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }

    public Vector2 getSize() {
        return new Vector2(width, height);
    }

    public long getWindowHandel() {
        return window;
    }
}
