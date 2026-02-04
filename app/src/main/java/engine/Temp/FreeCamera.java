package engine.Temp;

import engine.Engine_Classes.Time;

import engine.Behaviors.Component;
import engine.Engine_Classes.Window.Cursor;
import engine.Engine_Classes.Window.MouseButton;
import engine.Engine_Classes.Window.keyCode;
import engine.Engine_Classes.Window.Cursor.CursorMode;
import engine.Engine_Classes.Window.Cursor.CursorVisibility;
import engine.Packages.Editor.EditorGUITool.Classes.EditorGUIWindow;
import engine.Packages.Editor.EditorGUITool.Classes.EditorGUIWindow.*;
import engine.Packages.InputSystem.InputManager;
import engine.debugging.Debug;
import engine.structs.Quaternion;
import engine.structs.Vector2;
import engine.structs.Vector3;

public class FreeCamera extends Component {
    
    WindowLabel lable;

    float Pan = 0f;
    float Yaw = 0f;
    float MoveSpeed = 5f;

    @Override
    public void start() {

    }

    @Override
    public void update() {
        
        if (InputManager.getMouseButtonDown(MouseButton.RIGHT)) {
            Cursor.setCursorMode(CursorMode.LOCKED);
            Cursor.setCursorVisibility(CursorVisibility.HIDDEN);

            Vector2 MouseDelta = InputManager.getMouseDelta();

            Pan -= MouseDelta.x;
            Yaw -= MouseDelta.y;
            Yaw = Math.clamp(Yaw, -89.9f, 89.9f);

            Quaternion YawQuat = Quaternion.Euler(new Vector3(0, Pan,0));
            Quaternion PanQuat = Quaternion.Euler(new Vector3(Yaw, 0,0));

            Quaternion MixedRotation = (Quaternion)YawQuat.mul(PanQuat);
            transform().Rotation = MixedRotation;

            Vector3 MovementVector = new Vector3(0,0,0);

            if (InputManager.getKeyDown(keyCode.W)) { MovementVector.add(Vector3.Forward);}
            if (InputManager.getKeyDown(keyCode.S)) { MovementVector.add(Vector3.Back); }
            if (InputManager.getKeyDown(keyCode.A)) { MovementVector.add(Vector3.Left); }
            if (InputManager.getKeyDown(keyCode.D)) { MovementVector.add(Vector3.Right); }
            if (InputManager.getKeyDown(keyCode.E)) { MovementVector.add(Vector3.Up); }
            if (InputManager.getKeyDown(keyCode.Q)) { MovementVector.add(Vector3.Down); }

            if (InputManager.getKeyDown(keyCode.LEFT_SHIFT)) { MovementVector.Scale(5f); }

            MixedRotation.transform(MovementVector).mul(MoveSpeed).mul(Time.deltaTime);

            transform().Position.add(MovementVector);
        } else {

            Cursor.setCursorMode(CursorMode.UNLOCKED);
            Cursor.setCursorVisibility(CursorVisibility.VISIBLE);
        }
    }
}
