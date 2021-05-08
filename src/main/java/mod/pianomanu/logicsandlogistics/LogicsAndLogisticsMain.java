package mod.pianomanu.logicsandlogistics;

import mod.pianomanu.logicsandlogistics.setup.Registration;
import mod.pianomanu.logicsandlogistics.setup.RenderSetup;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

/**
 * Some description will follow, when I have time
 *
 * @author PianoManu
 * @version 1.0 05/08/2021
 */
@Mod("logicsandlogistics")
public class LogicsAndLogisticsMain {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "logicsandlogistics";

    public LogicsAndLogisticsMain() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);

        LOGGER.info("Registering all blocks and items from Logics and Logistics...");
        Registration.init();
        LOGGER.info("Registered all blocks and items from Logics and Logistics!");

        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Setting up client stuff for Logics and Logistics...");
        RenderSetup.init();
        LOGGER.info("Set up client stuff for Logics and Logistics!");
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
    }

    public static class LogicsAndLogisticsItemGroup extends ItemGroup {

        public static final LogicsAndLogisticsItemGroup LOGICS_AND_LOGISTICS = new LogicsAndLogisticsItemGroup(ItemGroup.TABS.length,MOD_ID);
        private LogicsAndLogisticsItemGroup(int index, String label) {
            super(index, label);
        }

        @Override
        @Nonnull
        public ItemStack makeIcon() {
            return new ItemStack(Registration.AND_GATE.get());
        }
    }
}
//This mod is dedicated to the living God and His son, Jesus. Without His support, I would never have had enough strength and perseverance to get this project working and publish it. Learn to hear His voice, it will transform your life. (Based on a quote from Covert_Jaguar, creator of RailCraft)
//========SOLI DEO GLORIA========//