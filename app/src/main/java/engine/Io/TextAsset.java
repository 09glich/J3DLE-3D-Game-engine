package engine.Io;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextAsset {

    public Path FilePath;

    public String ReadString() 
    {
        try {
            return Files.readString(FilePath);
        } catch (IOException e) {
            System.err.println("Failed to read file: " + FilePath);
            return "";
        }
        
    }

    public TextAsset() {}
    public TextAsset(Path assetPath) { FilePath = assetPath; }
    public TextAsset(String PathString) { FilePath = Path.of(PathString); }
    
    
}
