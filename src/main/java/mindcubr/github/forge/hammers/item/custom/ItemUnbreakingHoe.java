package mindcubr.github.forge.hammers.item.custom;

import cpw.mods.fml.common.registry.GameRegistry;
import mindcubr.github.forge.hammers.HammerElement;
import mindcubr.github.forge.hammers.HammersMod;
import mindcubr.github.forge.hammers.Reference;
import mindcubr.github.forge.hammers.hook.HammersHook;
import mindcubr.github.forge.hammers.register.HammerItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Unbreaking hoe custom textured.
 *
 * @author mindcubr
 * @see ItemHoe
 * @since 2.0.0-alpha
 */
public class ItemUnbreakingHoe extends ItemHoe implements HammerElement {

    public ItemUnbreakingHoe() {
        super(HammerItems.UNBREAKING_MATERIAL);
        String branch = "unbreaking_hoe";
        setUnlocalizedName(branch);
        setTextureName(Reference.RESOURCE_PREFIX + branch);
        setCreativeTab(HammersMod.CREATIVE_TAB);
    }

    /**
     * Register this custom recipe as item.
     */
    @Override
    public void registerRecipe() {
        ItemStack output = new ItemStack(this);
        HammersHook.ItemHook.setUnbreakable(output, true);
        GameRegistry.addRecipe(output, "UU ", " S ", " S ", 'U',
                HammerItems.UNBREAKING_INGOT, 'S', Items.stick);
        setMaxDamage(-1);

    }

    /**
     * When the {@link HammersMod} registers and loads this item.
     */
    @Override
    public void load() {
        GameRegistry.registerItem(this, getBranch());
        registerRecipe();   //Register recipe second
    }

    /**
     * This is required for the enchanting to work.
     *
     * @param stack the item stack
     * @return whether the input {@code stack} is enchantable, in our case.
     * @since 3.0.0-alpha
     */
    @Override
    public boolean isItemTool(ItemStack stack) {
        //Return true, as the item should be this instance
        return stack != null;
    }

    @Nonnull
    @Override
    public String getUnlocalized() {
        return getUnlocalizedName();
    }

}
