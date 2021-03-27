package mindcubr.github.forge.hammers;

import org.apache.commons.lang3.StringUtils;

/**
 * The hammer element is an interface that implements functionality
 * of loading recipes, registering and branching a certain element.
 * <p>This features are mostly used within an Item or Blocks.
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
public interface HammerElement {

    /**
     * Registers all recipes, if existent, within this element and
     * bound to this element instance.
     *
     * @implNote This method is empty by default. This is due
     * to some elements, ores, or general elements that does not provide
     * any recipes. If using an empty body, nothing will happen.
     */
    default void registerRecipe() {
        ;
    }

    /**
     * Method invoked when this element instance is to be loaded,
     * including registration, recipe, smelting, configuration, etc.
     */
    void load();

    /**
     * Returns this elements branch name, so the actual name,
     * without any resource or data path prefix.
     * <p>This is done by sub stringing {@link #getUnlocalizedName()}
     *
     * @return basically the localized name, or just the branch name of this element
     * @implNote By default, the {@link #getUnlocalizedName()} method is used
     * to get to the branch. Overriding is allowed and should be completed.
     * @apiNote The returning result must not be undefined.
     */
    default String getBranch() {
        String name = getUnlocalizedName();
        if (StringUtils.isEmpty(name))
            return StringUtils.EMPTY;

        return name.substring(name.indexOf('.') + 1);
    }

    String getUnlocalizedName();

}
