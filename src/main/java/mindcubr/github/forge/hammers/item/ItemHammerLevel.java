package mindcubr.github.forge.hammers.item;

import cpw.mods.fml.common.registry.GameRegistry;
import lombok.Getter;
import mindcubr.github.forge.hammers.HammerItem;
import mindcubr.github.forge.hammers.Reference;
import net.minecraft.item.Item;

/**
 * No description available.
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
public class ItemHammerLevel extends Item implements HammerItem {

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
        setTextureName(Reference.MOD_ID + branch);
    }

    @Override
    public final void registerRecipe() {
        GameRegistry.addRecipe(/*TODO*/null);
    }

    /**
     * @implNote This method is loading this {@link #registerRecipe() recipes}.
     */
    @Override
    public final void load() {
        registerRecipe();
    }

    /**
     * Returns the radius based on this {@link #level}.
     * <pre><code>
     *     3 + {@link #level}
     * </code></pre>
     *
     * @return the resulting radius, based on this {@link #level}
     */
    public final int getRadial() {
        return 3 + level;
    }


}
