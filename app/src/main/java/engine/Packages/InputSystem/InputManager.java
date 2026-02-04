package engine.Packages.InputSystem;

import engine.EventSystem.EventSystem.*;
import engine.structs.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import engine.App;
import engine.Behaviors.Component;
import engine.Engine_Classes.Window;
import engine.Engine_Classes.Window.keyCode;
import engine.Engine_Classes.Window.Cursor.CursorMode;
import engine.Engine_Classes.Window.Cursor;
import engine.Engine_Classes.Window.MouseButton;

public class InputManager extends Component {
    private static Window currentWindow;

    private static Map<keyCode, Event<keyCode>> keyDownEvents;
    private static Map<keyCode, Event<keyCode>> keyUpEvents;

    static {
        keyDownEvents = new HashMap<>();
        keyUpEvents = new HashMap<>();
    }



    @Override
    public void start() {
        currentWindow = App.getWindow();

        currentWindow.bindKeyDown((KeyCode) -> {
            if (keyDownEvents.containsKey(KeyCode)) keyDownEvents.get(KeyCode).fire(KeyCode);
        });

        currentWindow.bindKeyUp((KeyCode) -> {
            if (keyUpEvents.containsKey(KeyCode)) keyUpEvents.get(KeyCode).fire(KeyCode);
        });
    }

    public static void BindKeyDown(keyCode code, Consumer<keyCode> function) {
        if (!keyDownEvents.containsKey(code)) keyDownEvents.put(code, new Event<keyCode>());
        keyDownEvents.get(code).fire(code);
    }
    public static void BindKeyUp(keyCode code, Consumer<keyCode> function) {
        if (!keyUpEvents.containsKey(code)) keyUpEvents.put(code, new Event<keyCode>());
        keyUpEvents.get(code).fire(code);
    }
    
    public static boolean getKeyDown(keyCode keyCode) {
        return currentWindow.getKeyState(keyCode);
    }

    public static boolean getMouseButtonDown(MouseButton button) {
        return currentWindow.getMouseKeyState(button);
    }
    
    public static Vector2 getMouseDelta() {
        return currentWindow.getMouseDelta(); 
    }

    
}
