package engine.RenderingPipeline;

import engine.RenderingPipeline.Backend.*;


public class RenderFactory {
    public enum Backend {OPENGL, VULKAN}

    public static BackendGraphics Pipeline;
    public static BackendGraphics init(Backend backend) 
    {
        Pipeline = switch (backend) 
        {
            case OPENGL -> new GLBackend();
            case VULKAN -> new VulkanBackend();
        };
        return Pipeline;
    }

    public static BackendGraphics GetPipeline() 
    {
        return Pipeline;
    }
}
