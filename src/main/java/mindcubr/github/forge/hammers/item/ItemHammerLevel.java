package mindcubr.github.forge.hammers.item;

import cpw.mods.fml.common.registry.GameRegistry;
import lombok.Getter;
import mindcubr.github.forge.hammers.HammerElement;
import mindcubr.github.forge.hammers.HammersMod;
import mindcubr.github.forge.hammers.Reference;
import mindcubr.github.forge.hammers.register.HammerItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

/**
 * The Hammers' Level Item, that can be used to upgrade or
 * downgrade any {@link ItemHammerTool}.
 * <p>This leveling system is also applied to every tools instance
 * of different materials and levels, whose boundary is set to a particular
 * instance of this, to determine a possibility in leveling up and down.
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
public final class ItemHammerLevel extends Item implements HammerElement {

    /* The minimum level definable */
    public static final int MIN_LEVEL = 1;

    /* The maximum level definable */
    public static final int MAX_LEVEL = 3;

    /**
     * The level of this <em>leveling item</em>.
     */
    @Getter
    private final int level;

    public ItemHammerLevel(int level) {
        this.level = level;

        //Builds the basic branch name of this item hammer level
        String branch = "hammer_level_" + level;
        setUnlocalizedName(branch);
        setTextureName(Reference.RESOURCE_PREFIX + branch);
        setCreativeTab(HammersMod.CREATIVE_TAB);
    }

    @Override
    public final void registerRecipe() {
        GameRegistry.addRecipe(new ItemStack(this), "ORO", "RVR", "DRD",
                'O', Blocks.obsidian, 'D', Blocks.diamond_block, 'R', getRecipeRepresentative(), 'V',
                getRecipeRequirement());
    }


    /**
     * @implNote This method is loading this {@link #registerRecipe() recipes}.
     */
    @Override
    public final void load() {
        GameRegistry.registerItem(this, getBranch());
        registerRecipe();
    }

    /**
     * Returns the radius based on this {@link #level}.
     * <pre><code>
     *     3 + {@link #level} - 1
     * </code></pre>
     *
     * @return the resulting radius, based on this {@link #level}
     * @apiNote This method is existent and not a finalised private modified field
     * that caches the radial value, as changing the level or overriding would not apply
     * to runtime anymore.
     */
    public int getRadial() {
        return 3 + level - 1;
    }

    /**
     * Returns the in recipe used item, to declare this special type of {@link #level}.
     *
     * @return the item representative within any recipe based
     * on or even for this level.
     * @see #level
     * @see net.minecraft.item.Item.ToolMaterial
     */
    public Object getRecipeRepresentative() {
        switch (level) {
            //Level II recipe representative
            case 2:
                return Items.iron_ingot;

            //Level III recipe representative
            case 3:
                return Items.diamond;

            //TODO include more levels if wanted

            //Default levels including the first; I
            default:
                return Blocks.planks;
        }
    }

    /**
     * Representative requirement as item stack required for the crafting
     * upgrade.
     *
     * @return the recipe requirement
     */
    private Object getRecipeRequirement() {
        int max = HammerItems.itemHammerLevels.length;

        //Return planks as default requirement
        if (level <= 1 || level > max)
            return Blocks.planks;

        return HammerItems.itemHammerLevels[level - 2];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemHammerLevel that = (ItemHammerLevel) o;
        return level == that.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level);
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

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List desc, boolean param) {
        int cubicMetres = getRadial();
        desc.add("\u00a79Sets a hammers cubic radius");
        desc.add("\u00a79to \u00a77" + cubicMetres + " \u00a79cubic metres");
    }

}
