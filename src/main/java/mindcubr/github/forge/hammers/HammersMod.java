package mindcubr.github.forge.hammers;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import mindcubr.github.forge.hammers.item.ItemHammerLevel;
import mindcubr.github.forge.hammers.item.ItemHammerTool;
import mindcubr.github.forge.hammers.register.HammerBlocks;
import mindcubr.github.forge.hammers.register.HammerItems;
import mindcubr.github.forge.hammers.unbreaking.generation.UnbreakingGenerator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * No description available.
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
@Mod(modid = Reference.MOD_ID, version = Reference.MOD_VERSION)
public class HammersMod {

    /**
     * The main creative tab, used to determine where to add new items,
     * enchantments, etc. into within the creative crafting table/s.
     *
     * @see CreativeTabs
     */
    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public Item getTabIconItem() {
            return HammerItems.iconicIcon;
        }
    };

    /**
     * This tool materials are actually the <em>base tool materials</em>
     * used for the hammers and rest of tool-based items, whose will
     * use any of the here given materials.
     * <p>The materials include:
     * <ul>
     *     <li>Wood</li>
     *     <li>Stone</li>
     *     <li>Iron</li>
     *     <li>Gold</li>
     *     <li>Diamond</li>
     *     <li>Unbreaking</li>
     * </ul>
     *
     * @see net.minecraft.item.Item.ToolMaterial
     */
    public static final Item.ToolMaterial[] TOOL_MATERIALS = {
            Item.ToolMaterial.WOOD,
            Item.ToolMaterial.STONE,
            Item.ToolMaterial.IRON,
            Item.ToolMaterial.GOLD,
            Item.ToolMaterial.EMERALD,

            //Add the unbreaking material
            HammerItems.UNBREAKING_MATERIAL
    };

    /**
     * This method is invoked when the <b>Forge Mod Loader</b> is loading
     * this modification, before the actual initialization.
     *
     * @param event the event input passed from the FML.
     */
    @Mod.EventHandler
    public void preInit(@Nonnull FMLPreInitializationEvent event) {
        registerLevels();   //Level registration first
        registerHammers();  //Tool registration second
        registerBlocks();   //Block registration third

        //Generator registration last
        GameRegistry.registerWorldGenerator(UnbreakingGenerator.SINGLETON, 100);
        UnbreakingHandler.register();
    }

    /**
     * This method is invoked, when the <b>Forge Mod Loader</b> initializes
     * this modification.
     *
     * @param event the event input passed from the FML.
     */
    @Mod.EventHandler
    public void init(@Nonnull FMLInitializationEvent event) {

        //Load every item of the registration
        List<HammerElement> register = HammerItems.SINGLETON.getRegistered();
        for (int n = register.size(), i = 0; i < n; i++) {
            HammerElement item = register.get(i);
            item.load(); //Load the iterated Hammer's item

            //Check compatibility and set iconic icon
            if (item instanceof ItemHammerTool) {
                HammerItems.iconicIcon = (ItemHammerTool) item;
            }
        }

        //Load all blocks
        HammerBlocks.SINGLETON.stream().forEach(HammerElement::load);

    }

    /**
     * Registers all the tool items
     */
    private void registerHammers() {
        HammerItems register = HammerItems.SINGLETON;

        /* Register all the hammer _Tools_*/
        for (Item.ToolMaterial material : TOOL_MATERIALS) {
            int i = ItemHammerLevel.MIN_LEVEL;
            for (; i <= ItemHammerLevel.MAX_LEVEL; i++) {
                ItemHammerLevel level = Validate.notNull(register.findLevel(i));
                register.register(new ItemHammerTool(level, material));
            }
        }
    }

    /**
     * Registers all the item levels and general levels
     */
    private void registerLevels() {
        HammerItems register = HammerItems.SINGLETON;
        int i = ItemHammerLevel.MIN_LEVEL;
        for (; i <= ItemHammerLevel.MAX_LEVEL; i++) {
            register.register(new ItemHammerLevel(i));
        }
    }

    /**
     * Registers all the blocks
     */
    private void registerBlocks() {
        /* TODO content */
        //Block registration is done when created rn
    }

}
