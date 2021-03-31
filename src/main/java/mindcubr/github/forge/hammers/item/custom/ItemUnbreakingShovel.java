package mindcubr.github.forge.hammers.item.custom;

import cpw.mods.fml.common.registry.GameRegistry;
import mindcubr.github.forge.hammers.HammerElement;
import mindcubr.github.forge.hammers.HammersMod;
import mindcubr.github.forge.hammers.Reference;
import mindcubr.github.forge.hammers.hook.HammersHook;
import mindcubr.github.forge.hammers.register.HammerItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Unbreaking axe custom textured.
 *
 * @author mindcubr
 * @see ItemAxe
 * @since 2.0.0-alpha
 */
public class ItemUnbreakingShovel extends ItemSpade implements HammerElement {

    public ItemUnbreakingShovel() {
        super(HammerItems.UNBREAKING_MATERIAL);
        String branch = "unbreaking_shovel";
        setUnlocalizedName(branch);
        setTextureName(Reference.RESOURCE_PREFIX + branch);
        setCreativeTab(HammersMod.CREATIVE_TAB);
        setMaxDamage(-1);
    }

    /**
     * Register this custom recipe as item.
     */
    @Override
    public void registerRecipe() {
        ItemStack output = new ItemStack(this);
        HammersHook.ItemHook.setUnbreakable(output, true);
        GameRegistry.addRecipe(output, " U ", " S ", " S ", 'U',
                HammerItems.UNBREAKING_INGOT, 'S', Items.stick);

    }

    /**
     * When the {@link HammersMod} registers and loads this item.
     */
    @Override
    public void load() {
        GameRegistry.registerItem(this, getBranch());
        registerRecipe();   //Register recipe second
    }

    @Nonnull
    @Override
    public String getUnlocalized() {
        return getUnlocalizedName();
    }

}