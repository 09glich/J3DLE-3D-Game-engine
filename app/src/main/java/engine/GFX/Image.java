package engine.GFX;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.stb.STBImage;

import org.lwjgl.system.MemoryStack;

import engine.RenderingPipeline.Engine_Graphics;
import engine.structs.*;

public class Image {

    public enum ImageWrapMode {REPEAT, CLAMP, CLAMPX, CLAMPY}
    public enum ImageColorMode {R, RG, RGB, RGBA, sRGB}
    public enum ImageFilterMode {LINEAR, NEAREST}
    public enum MipmapMode {NONE, ENABLED}
    public enum AnisotropyMode {DISABLED, ENABLED}

    private boolean Dirty = true;
    private int ImageIndx = 0;

    private int width, height;
    private int channels;
    private ByteBuffer Pixels;

    private ImageWrapMode WrapMode;
    private ImageColorMode ColorMode;
    private ImageFilterMode FilterMode;
    private MipmapMode MipMode;
    private AnisotropyMode Anistropy;

    Image() {
        Dirty = true;
        width = 2;
        height = 2;
        WrapMode = WrapMode.REPEAT;
        ColorMode = ColorMode.RGBA;
        FilterMode = ImageFilterMode.LINEAR;
        MipMode = MipmapMode.ENABLED;
        Anistropy = AnisotropyMode.ENABLED;
    }
    

    // Wrap Mode
    public ImageWrapMode getWrapMode() { return WrapMode; }
    public void setWrapMode(ImageWrapMode wrapMode) {
        this.WrapMode = wrapMode;
        Dirty = true;
    }

    // Color Mode
    public ImageColorMode getColorMode() { return ColorMode; }
    public void setColorMode(ImageColorMode colorMode) {
        this.ColorMode = colorMode;
        Dirty = true;
    }

    // Filter Mode
    public ImageFilterMode getFilterMode() { return FilterMode; }
    public void setFilterMode(ImageFilterMode filterMode) {
        this.FilterMode = filterMode;
        Dirty = true;
    }

    // Mipmap Mode
    public MipmapMode getMipMode() { return MipMode; }
    public void setMipMode(MipmapMode mipMode) {
        this.MipMode = mipMode;
        Dirty = true;
    }

    // Anisotropy
    public AnisotropyMode getAnisotropy() {return Anistropy;}
    public void setAnisotropy(AnisotropyMode anisotropy) {
        this.Anistropy = anisotropy;
        Dirty = true;
    }

    // Dont mutate this unless you really know what you are doing
    public ByteBuffer getBuffer() {return Pixels;}

    // GetChannels
    public int getChannels() { return channels; }

    // Backend Building
    public void rebuild() {
        if (Dirty) {
            ImageIndx = Engine_Graphics.PushImage(this);
            Dirty = false;
        }
    }

    // Backend Storage Location
    public int getImageIndex() { return ImageIndx; }

    // Gets Size
    public Vector2 getSize() { return new Vector2(width, height); }
    public int getWidth() { return width; }
    public int getHeight() { return height ;}

    public static Image LoadImageFromFile(String path) {
        Image currentImage = new Image();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer W = stack.mallocInt(1);
            IntBuffer H = stack.mallocInt(1);
            IntBuffer C = stack.mallocInt(1);

            currentImage.Pixels = STBImage.stbi_load(path, W, H, C, 0);
            if (currentImage.Pixels == null) {throw new ImageLoadError("Image Failed to load. Image path : " + path);}

            currentImage.width = W.get();
            currentImage.height = H.get();
            currentImage.channels = C.get();
        }

        if (currentImage.channels == 3) {
            currentImage.ColorMode = ImageColorMode.RGB;
        } else if (currentImage.channels == 4) {
            currentImage.ColorMode = ImageColorMode.RGBA;
        } else if (currentImage.channels == 2) {
            currentImage.ColorMode = ImageColorMode.RG;
            } else if (currentImage.channels == 1) {
            currentImage.ColorMode = ImageColorMode.R;
        }


        currentImage.rebuild();

        return currentImage;
    }
}

class ImageLoadError extends RuntimeException {
    ImageLoadError(String msg) {
        super(msg);
    }
}
