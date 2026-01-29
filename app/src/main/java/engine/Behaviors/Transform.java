package engine.Behaviors;

import org.joml.Matrix4f;

import engine.Hiarachy.GameObject;
import engine.structs.*;

public class Transform extends Component{
    public Vector3 Position;
    public Quaternion Rotation;
    public Vector3 Scale;

    public Transform () {
        this.Position = new Vector3(0, 0, 0);
        this.Scale = Vector3.One;
        this.Rotation = new Quaternion();
    }
    public Transform (Vector3 Position, Quaternion Rotation) {
        this.Position = Position;
        this.Scale = Vector3.One;
        this.Rotation = Rotation;
    }
    public Transform (Vector3 Position, Quaternion Rotation, Vector3 Scale) {
        this.Position = Position;
        this.Scale = Scale;
        this.Rotation = Rotation;
    }
    public Transform (Vector3 Position, Quaternion Rotation, Float Scale) {
        this.Position = Position;
        this.Scale = Vector3.One.Scale(Scale);
        this.Rotation = Rotation;
    }

    public Matrix4f getTRXMatrix() {
        return new Matrix4f().translate(Position).rotate(Rotation).scale(Scale);
    }

    

    @Override
    public void setParent(GameObject go) {
        System.err.println("Cannot set Transform parent because this component is concidered core to gameobjects");
    }

    @Override
    public void setParent(Transform Transform) {
        System.err.println("Cannot set Transform parent because this component is concidered core to gameobjects");
    }

}
