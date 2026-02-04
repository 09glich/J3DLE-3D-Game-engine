package engine.structs;

import org.joml.Quaternionf;
public class Quaternion extends Quaternionf {

    public Quaternion copy() {
        Quaternion q = new Quaternion();
        q.set(this);
        return q;
    }
    

    public static Quaternion Euler(float X, float Y, float Z) {
        return (Quaternion) new Quaternion().rotationXYZ(
            (float)Math.toRadians(X), 
            (float)Math.toRadians(Y), 
            (float)Math.toRadians(Z)
        );
    
    }

    public static Quaternion Euler(Vector3 rotation) {
        return (Quaternion) new Quaternion().rotationXYZ(
            (float)Math.toRadians(rotation.x), 
            (float)Math.toRadians(rotation.y),  
            (float)Math.toRadians(rotation.z)
        );
    
    }
}
