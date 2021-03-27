package mindcubr.github.forge.hammers.item;

import cpw.mods.fml.common.registry.GameRegistry;
import lombok.Getter;
import mindcubr.github.forge.hammers.HammerElement;
import mindcubr.github.forge.hammers.HammersMod;
import mindcubr.github.forge.hammers.Reference;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Objects;

/**
 * No description available.
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
        GameRegistry.addRecipe(new ItemStack(this), "ORO", "RRR", "DDD",
                'O', Blocks.obsidian, 'D', Blocks.diamond_block, 'R', getRecipeRepresentative());
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
     * to runtime anymore. JIT will do its work here.
     * TODO: fact check
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

}
