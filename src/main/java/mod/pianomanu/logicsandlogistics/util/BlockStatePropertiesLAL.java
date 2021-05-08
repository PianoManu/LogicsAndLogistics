package mod.pianomanu.logicsandlogistics.util;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;

public class BlockStatePropertiesLAL {
    public static final BooleanProperty LEFT_POWERED = BooleanProperty.create("left_powered");
    public static final BooleanProperty RIGHT_POWERED = BooleanProperty.create("right_powered");
    public static final IntegerProperty OUTPUT_VALUE = IntegerProperty.create("output_value",0, 15);
}
