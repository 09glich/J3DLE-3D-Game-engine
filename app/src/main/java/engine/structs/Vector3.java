package engine.structs;

import org.joml.Vector3f;

public class Vector3 extends Vector3f {

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 Scale(float Scale) {
        this.x = this.x * Scale;
        this.y = this.y * Scale;
        this.z = this.z * Scale;

        return this;
    }

    //defaults
    public static Vector3 Zero = new Vector3(0,0,0);
    public static Vector3 One = new Vector3(1,1,1);
    public static Vector3 Up = new Vector3(0,1,0);
    public static Vector3 Left = new Vector3(1,0,0);
    public static Vector3 Forward = new Vector3(0,0,1);
    public static Vector3 Down = new Vector3(0,-1,0);
    public static Vector3 Right = new Vector3(-1,0,0);
    public static Vector3 Back = new Vector3(0,0,-1);

}
