package mod.pianomanu.logicsandlogistics.setup;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

/**
 * Some description will follow, when I have time
 *
 * @author PianoManu
 * @version 1.0 05/08/2021
 */
public class RenderSetup {
    public static void init() {
        RenderTypeLookup.setRenderLayer(Registration.AND_GATE.get(), RenderType.cutoutMipped());
    }
}
//========SOLI DEO GLORIA========//