package engine.structs;

import org.joml.Quaternionf;
public class Quaternion extends Quaternionf {
    public static final float DEFAULT_EPSILON = 0.0001f;

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

    public boolean equals(Quaternion other) {
        return equals(other, DEFAULT_EPSILON);
    }

    public boolean equals(Quaternion other, float epsilon) {
        if (other == null) {
            return false;
        }
        return Math.abs(this.x - other.x) <= epsilon
            && Math.abs(this.y - other.y) <= epsilon
            && Math.abs(this.z - other.z) <= epsilon
            && Math.abs(this.w - other.w) <= epsilon;
    }

    public boolean equalsExact(Quaternion other) {
        return other != null
            && this.x == other.x
            && this.y == other.y
            && this.z == other.z
            && this.w == other.w;
    }

    // Rotation-aware check: q and -q represent the same orientation.
    public boolean equalsRotation(Quaternion other) {
        return equalsRotation(other, DEFAULT_EPSILON);
    }

    public boolean equalsRotation(Quaternion other, float epsilon) {
        if (other == null) {
            return false;
        }

        float lenA2 = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
        float lenB2 = other.x * other.x + other.y * other.y + other.z * other.z + other.w * other.w;
        if (lenA2 <= 0f || lenB2 <= 0f) {
            return false;
        }

        float dot = this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
        float normDot = Math.abs(dot) / (float)Math.sqrt(lenA2 * lenB2);
        normDot = Math.min(1f, normDot);
        return (1f - normDot) <= epsilon;
    }
}
