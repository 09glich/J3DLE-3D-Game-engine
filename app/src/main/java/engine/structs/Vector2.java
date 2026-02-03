package engine.structs;

import org.joml.Vector2f;
import org.joml.Vector2fc;

public class Vector2 extends Vector2f {

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2 up =    new Vector2(0,1);
    public static Vector2 down =  new Vector2(0,-1);
    public static Vector2 left =  new Vector2(1,0);
    public static Vector2 right = new Vector2(-1,0);
    public static Vector2 zero = new Vector2(0,0);
    public static Vector2 one = new Vector2(1,1);

    public Vector2 add(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    
    public Vector2 sub(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vector2 mul(Vector2 other) {
        this.x *= other.x;
        this.y *= other.y;
        return this;
    }

    public Vector2 div(Vector2 other) {
        this.x /= other.x;
        this.y /= other.y;
        return this;
    }

    public Vector2 mul(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public Vector2 div(float scalar) {
        this.x /= scalar;
        this.y /= scalar;
        return this;
    }
}
