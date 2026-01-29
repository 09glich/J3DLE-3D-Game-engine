package engine.Behaviors;

import org.joml.Matrix4f;

import engine.structs.Vector2;


public class Camera extends Component{
    public static enum CameraMode {PERSPECTIVE, ORTHOGRAPHIC, CUSTOM}

    private Vector2 ViewSize;
    private float AspectRatio;
    
    public CameraMode CameraMode;
    public Matrix4f CustomPerspectiveMatrix;

    public float FeildOfVeiw = 90f;
    
    public float NearClippingPlane = 0.01f;
    public float FarClippingPlane = 1000f;

    public Matrix4f getProjectionMatrix4f() 
    {
        AspectRatio = ViewSize.x / ViewSize.y;
        if (CameraMode == CameraMode.PERSPECTIVE) {
            return new Matrix4f().perspective((float)Math.toRadians(FeildOfVeiw), AspectRatio, NearClippingPlane, FarClippingPlane);
        }
        return new Matrix4f();
    }

    public Matrix4f getVeiwMatrix4f()
    {
        return new Matrix4f().translate(transform().Position).rotate(transform().Rotation).scale(1).invert();
    }

    public void setVeiwSize(Vector2 Size) {
        ViewSize = Size;
        AspectRatio = this.ViewSize.x / this.ViewSize.y;
    }
    public Vector2 getViewSize() {
        return this.ViewSize;
    }
}
