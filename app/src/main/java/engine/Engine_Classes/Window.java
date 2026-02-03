package engine.Engine_Classes;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import engine.EventSystem.EventSystem.Event;
import engine.EventSystem.EventSystem.EventAction;
import engine.structs.Vector2;


public class Window {
    public enum windowMode{WINDOWED, BORDERLESSWINDOW, FULLSCREEN}
    public enum VSyncMode {ENABLED, DISABLED}

    private long window;
    private int width;
    private int height;
    private long PrimaryMonitor;
    private windowMode Mode = windowMode.WINDOWED;
    private VSyncMode vsync = VSyncMode.ENABLED;
    // Input
    private Vector2 mousePosition;
    private Vector2 previousMousePosition = Vector2.zero;

    private double scrollX;
    private double scrollY;

    private Event<keyCode> KeyDown;
    private Event<keyCode> KeyUp;
    
    
    //Core Functions
    private void WindowCheck() {
        if (window == 0) throw new RuntimeException("Window not initialized. cannot start window");
    }
    public void Start(Vector2 Size, windowMode Mode, VSyncMode mode) {
        // Opening Window
        GLFWErrorCallback.createPrint(System.err).set();
        if (!GLFW.glfwInit()) throw new IllegalStateException("GLFW Initialization Failure");
        PrimaryMonitor = GLFW.glfwGetPrimaryMonitor();

        //GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        window = GLFW.glfwCreateWindow(1920, 1080, "Window", 0,0);
        setSize(Size);
        setWindowedMode(Mode);
        setVsyncMode(mode);
        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwShowWindow(window); 

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            GLFW.glfwGetWindowSize(window, w, h);
            width = w.get(0);
            height = h.get(0);
        }
        

        //Callback hell
        // Resizeing
        GLFW.glfwSetWindowSizeCallback(window, (win, w, h) -> {
            width = w;
            height = h;
        });
        
        // Key State
        KeyUp = new Event<>();
        KeyDown = new Event<>();
        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (action == GLFW.GLFW_RELEASE) KeyUp.fire(keyCode.FromGLFW(key));
            if (action == GLFW.GLFW_PRESS) KeyDown.fire(keyCode.FromGLFW(key));
        });

        // Scroll wheel
        GLFW.glfwSetScrollCallback(window, (win, xoffset, yoffset) -> {
            scrollX += xoffset;
            scrollY += yoffset;
        });
    } 
    public void Start () {Start(new Vector2(1920,1080), windowMode.FULLSCREEN, VSyncMode.ENABLED);}
    public void WindowUpdate() {
        WindowCheck();

        scrollX = 0;
        scrollY = 0;

        previousMousePosition = mousePosition;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            DoubleBuffer x = stack.mallocDouble(1);
            DoubleBuffer y = stack.mallocDouble(1);
            GLFW.glfwGetCursorPos(window, x, y);

            mousePosition = new Vector2((float)x.get(0), (float)y.get(0));
        }

        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }
    public boolean WindowRequestClose() {return GLFW.glfwWindowShouldClose(window);}
    public void CloseWindow() {
        WindowCheck();

        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
    public VSyncMode getVsyncMode() {return vsync;}
    public void setVsyncMode(VSyncMode mode) 
    {
        WindowCheck();
        vsync = mode; if (mode == VSyncMode.ENABLED) 
        {
            GLFW.glfwSwapInterval(1);
        }
        else if (mode == VSyncMode.DISABLED) 
        {
            GLFW.glfwSwapInterval(0);
        }

    }

    // Customization
    //Size
    public Vector2 getSize() {
        return new Vector2(width, height);
    }
    public void setSize(Vector2 Size) {
        GLFW.glfwSetWindowSize(window, (int)Size.x, (int)Size.y); 
        width = (int)Size.x;
        height = (int)Size.y;
    }
    
    //Window Mode
    public void updateWindowDockMode() {
        if (Mode == windowMode.WINDOWED) {
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
        } else if (Mode == windowMode.FULLSCREEN) {
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
            GLFWVidMode mode = GLFW.glfwGetVideoMode(PrimaryMonitor);
            GLFW.glfwSetWindowMonitor(window, PrimaryMonitor, 0, 0, mode.width(), mode.height(), mode.refreshRate());
        } else if (Mode == windowMode.BORDERLESSWINDOW) {
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
            setSize(getMonitorSize(PrimaryMonitor));
        }
    }
    public void setWindowedMode(windowMode Mode) {
        this.Mode = Mode;
        updateWindowDockMode();
    }
    public windowMode getWindowMode() { return this.Mode; }


    public void SetFullscreenMonitor(long Monitor) {
        PrimaryMonitor = Monitor;
        updateWindowDockMode();
    }
    public void getMonitors() { // Not Implimented
        PointerBuffer Monitors = GLFW.glfwGetMonitors();
        //Monitors.
    }
    public Vector2 getMonitorSize(Long Monitor) { 
        GLFWVidMode Mode = GLFW.glfwGetVideoMode(PrimaryMonitor); 
        return new Vector2(Mode.height(), Mode.width()); 
    }

    // User Input Hooks
    public boolean getKeyState(keyCode key) {
        return GLFW.glfwGetKey(window, key.keyint()) == GLFW.GLFW_PRESS;
    }
    public Vector2 getMousePosition() {
        return mousePosition;
    }
    public Vector2 getMouseDelta() {
        return mousePosition.sub(previousMousePosition);
    }
    
    public Vector2 getScrollWeelDelta() {
        return new Vector2((float)scrollX, (float)scrollY);
    }
    public EventAction<keyCode> bindKeyDown(Consumer<keyCode> OnKeyDown) {
        return KeyDown.connect(OnKeyDown);
    }
    public EventAction<keyCode> bindKeyUp(Consumer<keyCode> OnKeyDown) {
        return KeyUp.connect(OnKeyDown);
    }

    // Info
    public long getWindowHandel() {
        return window;
    }

    public static class Cursor {}

    public static enum keyCode {
        //Auto generated by Codex (aint nobody got the time to do this by hand)
        A(GLFW.GLFW_KEY_A),
        B(GLFW.GLFW_KEY_B),
        C(GLFW.GLFW_KEY_C),
        D(GLFW.GLFW_KEY_D),
        E(GLFW.GLFW_KEY_E),
        F(GLFW.GLFW_KEY_F),
        G(GLFW.GLFW_KEY_G),
        H(GLFW.GLFW_KEY_H),
        I(GLFW.GLFW_KEY_I),
        J(GLFW.GLFW_KEY_J),
        K(GLFW.GLFW_KEY_K),
        L(GLFW.GLFW_KEY_L),
        M(GLFW.GLFW_KEY_M),
        N(GLFW.GLFW_KEY_N),
        O(GLFW.GLFW_KEY_O),
        P(GLFW.GLFW_KEY_P),
        Q(GLFW.GLFW_KEY_Q),
        R(GLFW.GLFW_KEY_R),
        S(GLFW.GLFW_KEY_S),
        T(GLFW.GLFW_KEY_T),
        U(GLFW.GLFW_KEY_U),
        V(GLFW.GLFW_KEY_V),
        W(GLFW.GLFW_KEY_W),
        X(GLFW.GLFW_KEY_X),
        Y(GLFW.GLFW_KEY_Y),
        Z(GLFW.GLFW_KEY_Z),

        DIGIT_0(GLFW.GLFW_KEY_0),
        DIGIT_1(GLFW.GLFW_KEY_1),
        DIGIT_2(GLFW.GLFW_KEY_2),
        DIGIT_3(GLFW.GLFW_KEY_3),
        DIGIT_4(GLFW.GLFW_KEY_4),
        DIGIT_5(GLFW.GLFW_KEY_5),
        DIGIT_6(GLFW.GLFW_KEY_6),
        DIGIT_7(GLFW.GLFW_KEY_7),
        DIGIT_8(GLFW.GLFW_KEY_8),
        DIGIT_9(GLFW.GLFW_KEY_9),

        KP_0(GLFW.GLFW_KEY_KP_0),
        KP_1(GLFW.GLFW_KEY_KP_1),
        KP_2(GLFW.GLFW_KEY_KP_2),
        KP_3(GLFW.GLFW_KEY_KP_3),
        KP_4(GLFW.GLFW_KEY_KP_4),
        KP_5(GLFW.GLFW_KEY_KP_5),
        KP_6(GLFW.GLFW_KEY_KP_6),
        KP_7(GLFW.GLFW_KEY_KP_7),
        KP_8(GLFW.GLFW_KEY_KP_8),
        KP_9(GLFW.GLFW_KEY_KP_9),
        KP_DECIMAL(GLFW.GLFW_KEY_KP_DECIMAL),
        KP_DIVIDE(GLFW.GLFW_KEY_KP_DIVIDE),
        KP_MULTIPLY(GLFW.GLFW_KEY_KP_MULTIPLY),
        KP_SUBTRACT(GLFW.GLFW_KEY_KP_SUBTRACT),
        KP_ADD(GLFW.GLFW_KEY_KP_ADD),
        KP_ENTER(GLFW.GLFW_KEY_KP_ENTER),
        KP_EQUAL(GLFW.GLFW_KEY_KP_EQUAL),

        SPACE(GLFW.GLFW_KEY_SPACE),
        ENTER(GLFW.GLFW_KEY_ENTER),
        TAB(GLFW.GLFW_KEY_TAB),
        ESCAPE(GLFW.GLFW_KEY_ESCAPE),
        BACKSPACE(GLFW.GLFW_KEY_BACKSPACE),
        INSERT(GLFW.GLFW_KEY_INSERT),
        DELETE(GLFW.GLFW_KEY_DELETE),
        HOME(GLFW.GLFW_KEY_HOME),
        END(GLFW.GLFW_KEY_END),
        PAGE_UP(GLFW.GLFW_KEY_PAGE_UP),
        PAGE_DOWN(GLFW.GLFW_KEY_PAGE_DOWN),
        LEFT(GLFW.GLFW_KEY_LEFT),
        RIGHT(GLFW.GLFW_KEY_RIGHT),
        UP(GLFW.GLFW_KEY_UP),
        DOWN(GLFW.GLFW_KEY_DOWN),

        LEFT_SHIFT(GLFW.GLFW_KEY_LEFT_SHIFT),
        RIGHT_SHIFT(GLFW.GLFW_KEY_RIGHT_SHIFT),
        LEFT_CONTROL(GLFW.GLFW_KEY_LEFT_CONTROL),
        RIGHT_CONTROL(GLFW.GLFW_KEY_RIGHT_CONTROL),
        LEFT_ALT(GLFW.GLFW_KEY_LEFT_ALT),
        RIGHT_ALT(GLFW.GLFW_KEY_RIGHT_ALT),
        LEFT_SUPER(GLFW.GLFW_KEY_LEFT_SUPER),
        RIGHT_SUPER(GLFW.GLFW_KEY_RIGHT_SUPER),
        MENU(GLFW.GLFW_KEY_MENU),

        CAPS_LOCK(GLFW.GLFW_KEY_CAPS_LOCK),
        NUM_LOCK(GLFW.GLFW_KEY_NUM_LOCK),
        SCROLL_LOCK(GLFW.GLFW_KEY_SCROLL_LOCK),
        PRINT_SCREEN(GLFW.GLFW_KEY_PRINT_SCREEN),
        PAUSE(GLFW.GLFW_KEY_PAUSE),

        MINUS(GLFW.GLFW_KEY_MINUS),
        EQUAL(GLFW.GLFW_KEY_EQUAL),
        LEFT_BRACKET(GLFW.GLFW_KEY_LEFT_BRACKET),
        RIGHT_BRACKET(GLFW.GLFW_KEY_RIGHT_BRACKET),
        BACKSLASH(GLFW.GLFW_KEY_BACKSLASH),
        SEMICOLON(GLFW.GLFW_KEY_SEMICOLON),
        APOSTROPHE(GLFW.GLFW_KEY_APOSTROPHE),
        COMMA(GLFW.GLFW_KEY_COMMA),
        PERIOD(GLFW.GLFW_KEY_PERIOD),
        SLASH(GLFW.GLFW_KEY_SLASH),
        GRAVE_ACCENT(GLFW.GLFW_KEY_GRAVE_ACCENT),

        F1(GLFW.GLFW_KEY_F1),
        F2(GLFW.GLFW_KEY_F2),
        F3(GLFW.GLFW_KEY_F3),
        F4(GLFW.GLFW_KEY_F4),
        F5(GLFW.GLFW_KEY_F5),
        F6(GLFW.GLFW_KEY_F6),
        F7(GLFW.GLFW_KEY_F7),
        F8(GLFW.GLFW_KEY_F8),
        F9(GLFW.GLFW_KEY_F9),
        F10(GLFW.GLFW_KEY_F10),
        F11(GLFW.GLFW_KEY_F11),
        F12(GLFW.GLFW_KEY_F12),
        F13(GLFW.GLFW_KEY_F13),
        F14(GLFW.GLFW_KEY_F14),
        F15(GLFW.GLFW_KEY_F15),
        F16(GLFW.GLFW_KEY_F16),
        F17(GLFW.GLFW_KEY_F17),
        F18(GLFW.GLFW_KEY_F18),
        F19(GLFW.GLFW_KEY_F19),
        F20(GLFW.GLFW_KEY_F20),
        F21(GLFW.GLFW_KEY_F21),
        F22(GLFW.GLFW_KEY_F22),
        F23(GLFW.GLFW_KEY_F23),
        F24(GLFW.GLFW_KEY_F24);

        static final Map <Integer, keyCode> BY_CODE = new HashMap<>();

        static {
            for (keyCode Code : keyCode.values()) {
                BY_CODE.put(Code.keyint(), Code);
            }
        }

        private final int key;
        private keyCode(int key) {
            this.key = key;
        }
        public int keyint() {
            return key;
        }
        public static keyCode FromGLFW(int GLFWKeyCode) {
           return BY_CODE.get(GLFWKeyCode);
        }
    }
    public static enum MouseButton {
        // GLFW exposes buttons 1..8, with aliases for left/right/middle.
        BUTTON_1(GLFW.GLFW_MOUSE_BUTTON_1),
        BUTTON_2(GLFW.GLFW_MOUSE_BUTTON_2),
        BUTTON_3(GLFW.GLFW_MOUSE_BUTTON_3),
        BUTTON_4(GLFW.GLFW_MOUSE_BUTTON_4),
        BUTTON_5(GLFW.GLFW_MOUSE_BUTTON_5),
        BUTTON_6(GLFW.GLFW_MOUSE_BUTTON_6),
        BUTTON_7(GLFW.GLFW_MOUSE_BUTTON_7),
        BUTTON_8(GLFW.GLFW_MOUSE_BUTTON_8),

        LEFT(GLFW.GLFW_MOUSE_BUTTON_LEFT),
        RIGHT(GLFW.GLFW_MOUSE_BUTTON_RIGHT),
        MIDDLE(GLFW.GLFW_MOUSE_BUTTON_MIDDLE);

        private final int button;

        private MouseButton(int button) {
            this.button = button;
        }

        public int keyint() {
            return button;
        }
    }
}