package engine.Behaviors;

import org.joml.Matrix4f;

import engine.Hiarachy.GameObject;
import engine.structs.*;

public class Transform extends Component{
    public Vector3 Position;
    public Quaternion Rotation;
    public Vector3 Scale;

    private Vector3 MatPosition;
    private Quaternion MatRotation;
    private Vector3 MatScale;

    Matrix4f StoredMat;

    public Transform () {
        this.Position = new Vector3(0, 0, 0);
        this.Scale = Vector3.One.copy();
        this.Rotation = new Quaternion();
    }
    public Transform (Vector3 Position, Quaternion Rotation) {
        this.Position = Position;
        this.Scale = Vector3.One.copy();
        this.Rotation = Rotation;
    }
    public Transform (Vector3 Position, Quaternion Rotation, Vector3 Scale) {
        this.Position = Position;
        this.Scale = Scale;
        this.Rotation = Rotation;
    }
    public Transform (Vector3 Position, Quaternion Rotation, Float Scale) {
        this.Position = Position;
        this.Scale = Vector3.One.copy().Scale(Scale);
        this.Rotation = Rotation;
    }

    public Matrix4f getTRXMatrix() {
        boolean needsRebuild = StoredMat == null
            || MatPosition == null
            || MatRotation == null
            || MatScale == null
            || !Position.equals(MatPosition)
            || !Rotation.equalsRotation(MatRotation)
            || !Scale.equals(MatScale);

        if (needsRebuild) {
            StoredMat = new Matrix4f().translate(Position).rotate(Rotation).scale(Scale);
            MatPosition = Position.copy();
            MatRotation = Rotation.copy();
            MatScale = Scale.copy();
        }

        return new Matrix4f(StoredMat);
    }

    

    @Override
    public void setParent(GameObject go) {
        System.err.println("Cannot set Transform parent because this component is concidered core to gameobjects");
    }

    @Override
    public void setParent(Transform Transform) {
        System.err.println("Cannot set Transform parent because this component is concidered core to gameobjects");
    }

    // Called by GameObject to attach its core Transform.
    public void attachTo(GameObject go) {
        super.setParent(go);
    }

}
