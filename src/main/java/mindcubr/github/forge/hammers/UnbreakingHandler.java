package mindcubr.github.forge.hammers;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.registry.GameRegistry;
import mindcubr.github.forge.hammers.hook.HammersHook;
import mindcubr.github.forge.hammers.register.HammerItems;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

/**
 * The unbreaking handler is the actual recipe register for any element
 * that is surrounded with {@link HammerItems#unbreakingIngot unbreaking ingots},
 * within any crafting inventory with size greater or equal to <b>nine</b>.
 * In other words, this handler is actually registering foreign and third party
 * breakable items and stacks to be Unbreaking-applicable.
 *
 * @author mindcubr
 * @apiNote This class contains no important information for developers but for
 * developers working on this modification, as this class only contains <em>protected</em>
 * <em>static</em> modified methods for safety-reasons.
 * @since 1.0.0-0.1
 */
public class UnbreakingHandler {

    /**
     * Boolean that is declared <em>True</em>, if this handler
     * is registered.
     */
    public static boolean registered;

    /**
     * Registers this unbreaking handler and creates a recipe
     * or multiple recipes and registers whose purpose is,
     * to create infinite unbreaking things, like tools, swords,
     * armor, etc.
     * <p>This method can only be invoked once.
     */
    protected static void register() {
        Validate.isTrue(!registered);
        registerRecipe();
        registered = true;
    }

    /**
     * Registers the named recipe from the {@link #register()}.
     *
     * @apiNote This seems to be like the {@link mindcubr.github.forge.hammers.HammerElement}
     * register recipe method, but it is not. This method has the static
     * modifier and is therefore independent from any extension and instance.
     * The purpose is the same. This method should not be executed
     * manually, as it is already by invoking {@link #register()}.
     */
    protected static void registerRecipe() {
        GameRegistry.addRecipe(new IRecipe() {

            final int center = 4;

            @Override
            public boolean matches(InventoryCrafting inv, World world) {
                for (int n = getRecipeSize(), i = 0; i < n; i++) {
                    ItemStack stack = inv.getStackInSlot(i);

                    //Don't allow any air inbetween those lines when matching
                    if (stack == null)
                        return false;

                    //Get item and continue
                    Item item = stack.getItem();

                    //Just skip if i is equals the center item
                    if (i == center) {
                        //Only allow breakable items
                        if (!EnumEnchantmentType.breakable.canEnchantItem(stack.getItem()))
                            return false;

                        //Recheck for damage
                        if (!stack.isItemStackDamageable())
                            return false;

                        continue;
                    }

                    //Check item for unbreaking ingot, ow return false
                    if (item != HammerItems.unbreakingIngot)
                        return false;

                }
                return true;
            }

            @Override
            public ItemStack getCraftingResult(InventoryCrafting inv) {
                ItemStack output = inv.getStackInSlot(center);
                output = output.copy();

                //Make item unbreakable
                HammersHook.ItemHook.setUnbreakable(output, true);

                //Update the lore of that item
                HammersHook.ItemHook.setLore(output, Lists.newArrayList("\u00a77Forever Unbreaking"));
                output.setStackDisplayName(EnumChatFormatting.AQUA + output.getDisplayName());
                return output;
            }

            @Override
            public int getRecipeSize() {
                return 9;
            }

            @Override
            public ItemStack getRecipeOutput() {
                return null;
            }
        });
    }


}
