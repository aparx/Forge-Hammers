package mindcubr.github.forge.hammers;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;

/**
 * No description available.
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
@Mod(modid = Reference.MOD_ID, version = Reference.MOD_VERSION, acceptedMinecraftVersions = "1.7.10")
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
            return null;
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
     *     <li>Unbreaking TODO</li>
     * </ul>
     *
     * @see net.minecraft.item.Item.ToolMaterial
     */
    public static final Item.ToolMaterial[] TOOL_MATERIALS = {
            Item.ToolMaterial.WOOD,
            Item.ToolMaterial.STONE,
            Item.ToolMaterial.IRON,
            Item.ToolMaterial.GOLD,
            Item.ToolMaterial.EMERALD
    };

    /**
     * This method is invoked when the <b>Forge Mod Loader</b> is loading
     * this modification, before the actual initialization.
     *
     * @param event the event input passed from the FML.
     */
    @Mod.EventHandler
    public void preInit(@Nonnull FMLPreInitializationEvent event) {

    }

    /**
     * This method is invoked, when the <b>Forge Mod Loader</b> initializes
     * this modification.
     *
     * @param event the event input passed from the FML.
     */
    @Mod.EventHandler
    public void init(@Nonnull FMLInitializationEvent event) {

    }

}
