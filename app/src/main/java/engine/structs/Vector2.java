package engine.structs;

public class Vector2 {
    public float x, y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2 up =    new Vector2(0,1);
    public static Vector2 down =  new Vector2(0,-1);
    public static Vector2 left =  new Vector2(1,0);
    public static Vector2 right = new Vector2(-1,0);
}
