package mod.pianomanu.logicsandlogistics.setup;

import mod.pianomanu.logicsandlogistics.LogicsAndLogisticsMain;
import mod.pianomanu.logicsandlogistics.block.AndGate;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Some description will follow, when I have time
 *
 * @author PianoManu
 * @version 1.0 05/08/2021
 */
public class Registration {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LogicsAndLogisticsMain.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LogicsAndLogisticsMain.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, LogicsAndLogisticsMain.MOD_ID);

    public static void init() {
        LOGGER.info("Registering Blocks from Logics and Logistics...");
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registered Blocks from Logics and Logistics!");
        LOGGER.info("Registering Items from Logics and Logistics...");
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registered Items from Logics and Logistics!");
        LOGGER.info("Registering Tiles from Logics and Logistics...");
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registered Tiles from Logics and Logistics!");
    }

    public static final RegistryObject<Block> AND_GATE = BLOCKS.register("and_gate", () -> new AndGate(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)));
    public static final RegistryObject<Item> AND_GATE_ITEM = ITEMS.register("and_gate", () -> new BlockItem(AND_GATE.get(), new Item.Properties().tab(LogicsAndLogisticsMain.LogicsAndLogisticsItemGroup.LOGICS_AND_LOGISTICS)));
}
//========SOLI DEO GLORIA========//