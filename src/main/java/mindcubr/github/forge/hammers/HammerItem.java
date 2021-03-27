package mindcubr.github.forge.hammers;

/**
 * No description available.
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
public interface HammerItem {

    /**
     * Registers all recipes, if existent, within this item and
     * bound to this item instance.
     *
     * @implNote This method is empty by default. This is due
     * to some items, ores, or general items that does not provide
     * any recipes. If using an empty body, nothing will happen.
     */
    default void registerRecipe() {
        ;
    }

    /**
     * Method invoked when this item instance is to be loaded,
     * which is invoked before the {@link #registerRecipe()} methodF is.
     */
    void load();

}
