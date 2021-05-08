package mod.pianomanu.logicsandlogistics.setup;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public class RenderSetup {
    public static void init() {
        RenderTypeLookup.setRenderLayer(Registration.AND_GATE.get(), RenderType.cutoutMipped());
    }
}
