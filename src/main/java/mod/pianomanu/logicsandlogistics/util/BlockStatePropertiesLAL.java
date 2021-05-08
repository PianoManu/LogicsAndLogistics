package mod.pianomanu.logicsandlogistics.util;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;

/**
 * Some description will follow, when I have time
 *
 * @author PianoManu
 * @version 1.0 05/08/2021
 */
public class BlockStatePropertiesLAL {
    public static final BooleanProperty LEFT_POWERED = BooleanProperty.create("left_powered");
    public static final BooleanProperty RIGHT_POWERED = BooleanProperty.create("right_powered");
    public static final IntegerProperty OUTPUT_VALUE = IntegerProperty.create("output_value",0, 15);
}
//========SOLI DEO GLORIA========//