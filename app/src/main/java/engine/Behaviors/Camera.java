package engine.Behaviors;

import org.joml.Matrix4f;

import engine.GFX.Material;
import engine.GFX.Mesh;
import engine.structs.Color;
import engine.structs.Vector2;


public class Camera extends Component{
    public static enum CameraMode {PERSPECTIVE, ORTHOGRAPHIC, CUSTOM}
    public static enum CameraClearMode {COLOR, SKYBOX, BLANK, NONE}

    private Vector2 ViewSize;
    private float AspectRatio;
    
    public CameraMode CameraMode;
    public Matrix4f CustomPerspectiveMatrix;

    public CameraClearMode ClearMode = CameraClearMode.BLANK;
    public Color ClearColor = Color.black;
    private Material SkyboxMaterial;
    

    public float FeildOfVeiw = 90f;
    
    private float NearClippingPlane = 0.01f;
    private float FarClippingPlane = 1000f;

    public Matrix4f PerspectiveMatrix;

    public Camera() {
        setVeiwSize(new Vector2(1280, 720)) ;
        CameraMode = CameraMode.PERSPECTIVE;
        ClearMode = CameraClearMode.COLOR;
    }

    private void BuildPerspective() {
        PerspectiveMatrix = new Matrix4f().perspective((float)Math.toRadians(FeildOfVeiw), AspectRatio, NearClippingPlane, FarClippingPlane);
    }

    public Matrix4f getProjectionMatrix4f() 
    {
        AspectRatio = ViewSize.x / ViewSize.y;
        if (CameraMode == CameraMode.PERSPECTIVE) {
            return PerspectiveMatrix; 
        }
        return new Matrix4f();
    }

    public Matrix4f getVeiwMatrix4f()
    {
        return new Matrix4f(transform().getTRXMatrix()).invert();
    }

    public CameraClearMode getCameraClearMode() {
        return ClearMode;
    }
    public void setCameraClearMode(CameraClearMode mode) {
        this.ClearMode = mode;
    }

    public Color getClearColor() {
        return ClearColor;
    }
    public void setClearColor(Color color) {
        this.ClearColor = color;
    }

    public Material getSkyboxMaterial() {
        return SkyboxMaterial;
    }
    public void setSkyboxMaterial(Material material) {
        this.SkyboxMaterial = material;
    }

    // VeiwSize
    public void setVeiwSize(Vector2 Size) {
        ViewSize = Size;
        AspectRatio = this.ViewSize.x / this.ViewSize.y;
        BuildPerspective();
    }
    public Vector2 getViewSize() {
        return this.ViewSize;
    }

    // Near Clipping plane
    public void setNearClip(float nearClip) {
        NearClippingPlane = nearClip;
        BuildPerspective();
    }
    public float getNearClip() {
        return NearClippingPlane;
    }

    //Far Clipping Plane 
    public void setFarClip(float farClip) {
        FarClippingPlane = farClip;
        BuildPerspective();
    }
    public float getFarClip() {
        return FarClippingPlane;
        
    }
}
