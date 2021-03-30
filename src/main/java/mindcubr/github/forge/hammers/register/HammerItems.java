package mindcubr.github.forge.hammers.register;

import com.google.common.collect.Lists;
import mindcubr.github.forge.hammers.HammerElement;
import mindcubr.github.forge.hammers.hook.Register;
import mindcubr.github.forge.hammers.item.ItemHammerLevel;
import mindcubr.github.forge.hammers.item.ItemHammerTool;
import mindcubr.github.forge.hammers.item.ItemUnbreakingIngot;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;

/**
 * No description available.
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
public class HammerItems extends Register<HammerElement> {

    /**
     * The hammer item that is equal the last added hammer tool.
     */
    public static ItemHammerTool iconicIcon;

    /**
     * The main instance for this item register.
     */
    public static final HammerItems SINGLETON = new HammerItems();

    /**
     * Additional array providing information about registered
     * {@link ItemHammerLevel} with their level as index within this array,
     * but subtracted by one (as the beginning of a level starts at 1, but
     * computing at 0).
     */
    public static ItemHammerLevel[] itemHammerLevels = new ItemHammerLevel[ItemHammerLevel.MAX_LEVEL];

    public static final ItemUnbreakingIngot unbreakingIngot = new ItemUnbreakingIngot();

    /**
     * The unbreaking material, related to the {@link #unbreakingIngot}.
     */
    public static Item.ToolMaterial unbreakingMaterial;

    /**
     * Creates a private repository of the register.
     */
    protected HammerItems() {
        super(Lists.newArrayList());
    }

    static {
        //Register unbreaking ingot
        HammerItems.SINGLETON.register(unbreakingIngot);
    }

    /**
     * Registers the input {@code element} if it is not already
     * registered and returns whether the insertion of the {@code element}
     * happened.
     * <p>If the {@code element} is undefined, <em>False</em> is returned.
     * No side effects occur.
     * <p>This operation is synchronized with the register itself.
     *
     * @param element the element
     */
    @Override
    public boolean register(@Nonnull HammerElement element) {
        boolean response = super.register(element);

        //Check for array registration, if possible
        if (response && element instanceof ItemHammerLevel) {
            ItemHammerLevel itemLevel = (ItemHammerLevel) element;
            int level = itemLevel.getLevel();
            if (isInLevelArray(level)) {
                itemHammerLevels[level - 1] = itemLevel;
                return true;
            }
        }
        return response;
    }

    /**
     * Searches a {@link ItemHammerLevel} within this register, that matches
     * the {@code level} of every element within this register with the input arguments.
     * <p>If no element was found, undefined is returned.
     *
     * @param level the level for the search query
     * @return the possibly found hammer level instance, if matching with both input arguments,
     * of no was found, {@code undefined} is returned.
     */
    @Nullable
    public ItemHammerLevel findLevel(int level) {
        //Checks whether the level is within a possible array for O(1) complexity
        if (isInLevelArray(level)) {
            return itemHammerLevels[level - 1];
        }

        //Continue iterating through every item, as the level exceeds the default array
        for (final Iterator<HammerElement> itr = iterator(); itr.hasNext(); ) {
            HammerElement item = itr.next();

            //Check for compatibility and get element if possible
            if (!(item instanceof ItemHammerLevel))
                continue;

            //Cast hammer itemable to tool
            ItemHammerLevel element = (ItemHammerLevel) item;

            //Check if level is matching or not, if true return current itr. element
            if (element.getLevel() == level)
                return element;

        }
        return null;
    }

    /**
     * Returns whether the input {@code level} could be used to
     * be either inserted into this {@link #itemHammerLevels} or to get
     * a level out of it, with the given {@code level} for e.g. a better
     * time complexity of O(1).
     *
     * @param level the level to be provided. Excluding zero,
     *              including {@link #itemHammerLevels length}.
     * @return whether the {@code level} is a valid index within this
     * {@linkplain #itemHammerLevels} array.
     * @apiNote Overwrite this, if you think to concatenate the array, but
     * it is not a must, as this method is using the length of this
     * {@link #itemHammerLevels} array.
     * @see #itemHammerLevels
     */
    protected boolean isInLevelArray(int level) {
        return level > 0 && level <= itemHammerLevels.length;
    }

    /**
     * Searches a {@link ItemHammerTool} within this register, that matches
     * the {@code material} and {@code level} of every element within this register
     * with the input arguments.
     * <p>If {@code level} or {@code material} is undefined and any registered element
     * level or material is undefined too, this very iterated element is returned.
     * <p>If no element was found, undefined is returned.
     *
     * @param level    the level for the search query, {@code undefinable}
     * @param material the material for the search query, {@code undefinable}
     * @return the possibly found tool, if matching with both input arguments,
     * of no was found, {@code undefined} is returned.
     */
    @Nullable
    public ItemHammerTool findTool(ItemHammerLevel level, Item.ToolMaterial material) {
        for (final Iterator<HammerElement> itr = iterator(); itr.hasNext(); ) {
            HammerElement item = itr.next();

            //Check for compatibility and get element if possible
            if (!(item instanceof ItemHammerTool))
                continue;

            //Cast hammer itemable to tool
            ItemHammerTool tool = (ItemHammerTool) item;

            //Get attributes of iterated tool and compare
            if (!Objects.equals(tool.getBoundary(), level))
                continue;

            //Check if material is matching or not, if true return current itr. element
            if (Objects.equals(tool.getToolMaterial(), material))
                return tool;
        }
        return null;
    }

}
