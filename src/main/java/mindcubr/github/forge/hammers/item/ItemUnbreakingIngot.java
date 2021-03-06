package mindcubr.github.forge.hammers.item;

import cpw.mods.fml.common.registry.GameRegistry;
import mindcubr.github.forge.hammers.HammerElement;
import mindcubr.github.forge.hammers.HammersMod;
import mindcubr.github.forge.hammers.Reference;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

import javax.annotation.Nonnull;

/**
 * No description available.
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
public class ItemUnbreakingIngot extends Item implements HammerElement {

    public ItemUnbreakingIngot() {
        String branch = "unbreaking_ingot";
        setUnlocalizedName(branch);
        setTextureName(Reference.RESOURCE_PREFIX + branch);
        setCreativeTab(HammersMod.CREATIVE_TAB);
    }

    /**
     * Register ingot crafting recipe, besides the smelting from
     * an ore to this.
     */
    @Override
    public void registerRecipe() {
        GameRegistry.addRecipe(new ItemStack(this, 2), "ONO", "NON", "ONO",
                'N', Items.nether_star, 'O', Blocks.obsidian);
    }

    /**
     * Method invoked when this element instance is to be loaded.
     * <p>This method will invoke this {@link #registerRecipe()} automatically,
     * as every loading method.
     */
    @Override
    public void load() {
        GameRegistry.registerItem(this, getBranch());
        registerRecipe();
    }

    /**
     * Implements a new {@link net.minecraft.item.Item.ToolMaterial} that contains this type
     * of unbreaking breakability and data.
     *
     * @return the new meshed material.
     */
    public static ToolMaterial registerMaterial() {
        return EnumHelper.addToolMaterial("UNBREAKING", 4, Integer.MAX_VALUE, 8.0F, 4.0F, 10);
    }

    /**
     * Equivalent to {@link #getUnlocalizedName()} as an error occurred.
     * This equivalent is targeting the {@link #getBranch()} method,
     * that is requiring the actual unlocalized name for its main branch
     * return statement.
     *
     * @return the actual unlocalized name
     */
    @Nonnull
    @Override
    public final String getUnlocalized() {
        return getUnlocalizedName();
    }

}
