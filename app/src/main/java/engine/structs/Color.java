package engine.structs;

public class Color {
    public final int r,g,b,a;

    public Color(int r, int g, int b) { this(r, g, b, 255); }

    public Color(float r, float g, float b, float a) {
        this.r = clamp8((int)(r*255));
        this.g = clamp8((int)(g*255));
        this.b = clamp8((int)(b*255));
        this.a = clamp8((int)(a*255));
    }

    public Color(float r, float g, float b) {
        this.r = clamp8((int)(r*255));
        this.g = clamp8((int)(g*255));
        this.b = clamp8((int)(b*255));
        this.a = 255;
    }
    
    // Getters
    public int r() {return this.r;}
    public int g() {return this.g;}
    public int b() {return this.b;}
    public int a() {return this.a;}

    public float rf() {return 1f / this.r;}
    public float gf() {return 1f / this.g;}
    public float bf() {return 1f / this.b;}
    public float af() {return 1f / this.a;}


    // static set colors
    public static Color red =   new Color(255,0,0);
    public static Color green = new Color(0,255,0);
    public static Color blue =  new Color(0,0,255);
    public static Color black = new Color(0,0,0);
    public static Color white = new Color(255,255,255);

    // HSV helpers (h in degrees [0..360), s/v in [0..1])
    public static Color FromHSV(float h, float s, float v) {
        return FromHSV(h, s, v, 1.0f);
    }

    public static Color FromHSV(float h, float s, float v, float a) {
        float c = v * s;
        float hh = ((h % 360f) + 360f) % 360f;
        float hPrime = hh / 60f;
        float x = c * (1f - Math.abs(hPrime % 2f - 1f));

        float r1 = 0f, g1 = 0f, b1 = 0f;
        if (hPrime < 1f) {
            r1 = c; g1 = x; b1 = 0f;
        } else if (hPrime < 2f) {
            r1 = x; g1 = c; b1 = 0f;
        } else if (hPrime < 3f) {
            r1 = 0f; g1 = c; b1 = x;
        } else if (hPrime < 4f) {
            r1 = 0f; g1 = x; b1 = c;
        } else if (hPrime < 5f) {
            r1 = x; g1 = 0f; b1 = c;
        } else {
            r1 = c; g1 = 0f; b1 = x;
        }

        float m = v - c;
        return new Color(r1 + m, g1 + m, b1 + m, a);
    }

    private static int clamp8(int v) { return Math.max(0, Math.min(255, v)); }
}
