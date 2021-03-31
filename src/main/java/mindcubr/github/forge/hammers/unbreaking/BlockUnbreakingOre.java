package mindcubr.github.forge.hammers.unbreaking;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.registry.GameRegistry;
import mindcubr.github.forge.hammers.HammerElement;
import mindcubr.github.forge.hammers.HammersMod;
import mindcubr.github.forge.hammers.Reference;
import mindcubr.github.forge.hammers.hook.HammersHook;
import mindcubr.github.forge.hammers.item.ItemUnbreakingIngot;
import mindcubr.github.forge.hammers.register.HammerItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The <b>Unbreaking Ore</b> is an ore, that can be used for several
 * tasks and items. It can also imply, that different breakable tools
 * are unbreakable until they getting thrown away.
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
public final class BlockUnbreakingOre extends Block implements HammerElement {

    public BlockUnbreakingOre() {
        super(Material.rock /* Sound */);
        setBlockName("unbreaking_ore");
        setHardness(20.0F /* Todo dynamic hardness */);
        setBlockTextureName(Reference.RESOURCE_PREFIX + getBranch());
        setCreativeTab(HammersMod.CREATIVE_TAB);
    }

    /**
     * Registers the recipe for the oven.
     * <p>When burning this ore, {@link ItemUnbreakingIngot} is the result.
     */
    @Override
    public void registerRecipe() {
        //Smelting recipe for this to become ingots
        GameRegistry.addSmelting(this, new ItemStack(HammerItems.UNBREAKING_INGOT), 2.0F);
    }

    /**
     * Method invoked when this element instance is to be loaded,
     * including registration, recipe, smelting, configuration, etc.
     */
    @Override
    public void load() {
        GameRegistry.registerBlock(this, getBranch());
        registerRecipe();
    }

    /**
     * Returns the amount of <em>single</em> items going to be dropped,
     * based on the input {@code fortune}.
     *
     * @param world    the world where to drop the drops
     * @param x        the horizontal tridi-position x of the drop location
     * @param y        the horizontal tridi-position y of the drop location
     * @param z        the horizontal tridi-position z of the drop location
     * @param metadata the meta data of the broke block
     * @param fortune  the possible fortune effect, that has an impact on the
     *                 drop amount
     * @return a data structure list containing every item drop for this
     * particular call.
     */
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        Random random = ThreadLocalRandom.current();
        ItemStack[] drops = new ItemStack[fortune + 1];
        int quantity = quantityDropped(metadata, fortune, random);
        for (int i = 0; i < quantity; i++) {
            drops[i] = new ItemStack(HammerItems.UNBREAKING_INGOT);
        }
        return Lists.newArrayList(drops);
    }

    /**
     * Returns a whole {@link Item} with its amount based on the {@code fortune}
     * and the input {@code random}.
     *
     * @param meta    the meta data of the broke block
     * @param random  the randomizer
     * @param fortune the possible fortune effect, that has an impact
     *                on the amount of the to be dropped returning item instance
     * @return the item/s to be dropped
     */
    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        //Get quantity and return the new stack
        int quantity = quantityDropped(meta, fortune, random);
        return new ItemStack(HammerItems.UNBREAKING_INGOT, quantity).getItem();
    }

    /**
     * Returns the quantity of amount of to be dropped items,
     * calculated by using the {@code fortune} level and {@code random}.
     *
     * @param fortune the fortune level
     * @param random  the randomization
     * @return the quantity to be dropped
     */
    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        return HammersHook.randomize(random, fortune + 1);
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
