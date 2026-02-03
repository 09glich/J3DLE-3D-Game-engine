package engine.structs;

import org.joml.Vector3f;

public class Vector3 extends Vector3f {

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 Scale(float Scale) {
        return mul(Scale);
    }

    public Vector3 add(Vector3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }

    public Vector3 sub(Vector3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }

    public Vector3 mul(Vector3 other) {
        this.x *= other.x;
        this.y *= other.y;
        this.z *= other.z;
        return this;
    }

    public Vector3 div(Vector3 other) {
        this.x /= other.x;
        this.y /= other.y;
        this.z /= other.z;
        return this;
    }

    public Vector3 mul(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }

    public Vector3 div(float scalar) {
        this.x /= scalar;
        this.y /= scalar;
        this.z /= scalar;
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
