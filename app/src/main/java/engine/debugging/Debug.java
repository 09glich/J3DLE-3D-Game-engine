package engine.debugging;


public class Debug {

    
    public static void DebugAssert(boolean Point, Exception currentexeption) throws Exception 
    {
        if (Point) {
            throw currentexeption;
        }
    }

    public static void log(String str) {
        System.out.println(str);
    }
    
}