package mindcubr.github.forge.hammers.hook;

import com.google.common.collect.Lists;
import mindcubr.github.forge.hammers.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * The Hammers' Hook that contains special functionality <em>without</em>
 * the need of overriding the default Minecraft methods with Mixins or adding
 * new functionality to those classes.
 * <p>This class' content is not a copy of the Minecraft source.
 * This class contains helpful and simple utilities that are very useful
 * when working with this modification.
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
public class HammersHook {

    /**
     * Randomizes the input range of 1 and {@code max} within {@code random},
     * without generating an exception when any of the bound is invalid.
     * <p>The key difference between this and {@link #randomize(Random, int, int)} is,
     * that this method does not require a minimum value, as it is <b>1</b> automatically
     * in here.
     *
     * @param random the randomizer
     * @param max    the maximum value of the randomizer
     * @return the randomized value inbetween {@code min} and {@code max}.
     */
    public static int randomize(@Nonnull Random random, int max) {
        return randomize(random, 1, max);
    }

    /**
     * Randomizes the input range of {@code min} and {@code max} within {@code random},
     * without generating an exception when any of the bound is invalid.
     *
     * @param random the randomizer
     * @param min    the minimum the randomizer returns
     * @param max    the maximum value of the randomizer
     * @return the randomized value inbetween {@code min} and {@code max}.
     */
    public static int randomize(@Nonnull Random random, int min, int max) {
        //Validate arguments, don't pass illegal ones
        Validate.notNull(random);
        if (min <= 0 || max <= 0)
            return min;

        //Generate random
        return random.nextInt(Math.max(max - min, 1)) + min;
    }

    /**
     * Converts the input {@code level} to a roman number, if possible.
     * <p>Everything greater than level 5 or less than zero will
     * not be counted. Everything greater than level 5 will return just
     * the default latin numeric number.
     * <pre>
     *     levelToStr(-1) = I
     *     levelToStr(0) = I
     *     levelToStr(1) = I
     *     levelToStr(2) = II
     *     levelToStr(3) = III
     *     levelToStr(4) = IV
     *     levelToStr(5) = V
     *     TODO levelToStr(6) = 6
     * </pre>
     *
     * @param level the level in
     * @return the stringify version of the {@code level}
     */
    public static String levelToString(int level) { //TODO lazy algorithm
        StringBuilder builder = new StringBuilder();
        level = Math.max(level, 1);
        if (level > 5)
            return String.valueOf(level);

        switch (level) {
            //'I' appendix as prefix
            case 4:
                builder.append('I');

                //'I' appendix as prefix (=5) or suffix (=4)
            case 5:
                builder.append('V');
                break;

            //Default implementation of: I, II, II
            default:
                for (int i = 1; i <= level; i++) {
                    builder.append('I');
                }
        }

        return builder.toString();
    }

    /**
     * Returns the alternative lowercase name for the {@code material}.
     * <p>This is used, to avoid naming any tool or material <em>EMERALD</em>,
     * as the actual <em>Diamond</em> tool is {@link net.minecraft.item.Item.ToolMaterial#EMERALD}.
     * <p>The returning result is either diamond, if input is {@linkplain net.minecraft.item.Item.ToolMaterial#EMERALD},
     * or the lower enum name of the input material.
     *
     * @param material the material to be possibly named differently
     * @return the alternate named {@code material}
     */
    public static String lowerAlternativeToolName(Item.ToolMaterial material) {
        //Check for undefined value
        if (material == null)
            return "null";

        //If the name is emerald, which is actually diamond
        if (material == Item.ToolMaterial.EMERALD)
            return "diamond";

        //Lowercase enum name of input enum as name
        return material.name().toLowerCase();
    }

    /**
     * Sends a chat {@code message} to the {@code receiver}.
     *
     * @param receiver the receiver of the {@code message}
     * @param message  the {@code message} to be send to the {@code receiver}
     * @deprecated This will be removed in future versions, as this is just
     * for development purposes.
     */
    @Deprecated
    public static void sendMessage(@Nonnull EntityPlayer receiver, @Nonnull Object message) {
        Validate.notNull(receiver);
        Validate.notNull(message);
        message = String.format("[%tT] [%s] %s", new Date(), Reference.MOD_ID, message);
        ChatComponentText text = new ChatComponentText((String) message);
        receiver.addChatComponentMessage(text);
    }

    /**
     * Item utility class to change items states.
     */
    public static class ItemHook {

        /**
         * Creates either a new {@link NBTTagCompound} if given {@code stack}
         * does not contain it or the main compound of that {@code stack}.
         *
         * @param stack the item stack possibly containing the wanted compound
         * @return either a new compound instance, if current is undefined,
         * or the actual {@code stack} NBT.
         * @see NBTTagCompound
         */
        public static NBTTagCompound createAbsent(@Nonnull ItemStack stack) {
            //Validate and get stacks main stack compound
            Validate.notNull(stack);
            NBTTagCompound compound = stack.getTagCompound();
            if (compound != null)
                return compound;

            return new NBTTagCompound();
        }

        /**
         * Makes the {@code stack} unbreakable if the {@code value} is <em>True</em>.
         *
         * @param stack the stack to make either unbreakable or breakable
         * @param value the value of the unbreakable state, if set to <em>True</em>
         *              the {@code stack} is unbreakable, otherwise <em>breakable</em>.
         * @since 1.0.1-alpha
         */
        public static void setUnbreakable(@Nonnull ItemStack stack, boolean value) {
            Validate.notNull(stack);

            //Get compound of element unbreaking NBT editing
            NBTTagCompound compound = HammersHook
                    .ItemHook.createAbsent(stack);

            //Update the "Unbreakable" state of the item
            compound.setBoolean("Unbreakable", value);
            stack.setTagCompound(compound);
        }

        /**
         * Updates the <em>Lore</em> (description) of the {@code stack} to the
         * input {@code lore}.
         *
         * @param stack the stack to apply the {@code lore} onto, only {@code defined}
         * @param lore  the lore to be applied to the {@code stack}, only {@code defined}
         */
        public static void setLore(@Nonnull ItemStack stack, @Nonnull List<String> lore) {
            Validate.notNull(stack);
            Validate.notNull(lore);

            NBTTagCompound compound = createAbsent(stack);

            final String displaysName = "display";
            NBTTagCompound display = new NBTTagCompound();
            if (compound.hasKey(displaysName, 10))
                display = compound.getCompoundTag(displaysName);

            //Create new NBT lore list
            NBTTagList loreList = new NBTTagList();
            display.setTag("Lore", loreList);

            //Append to lore list
            for (int n = lore.size(), i = 0; i < n; ++i) {
                loreList.appendTag(new NBTTagString(lore.get(i)));
            }

            //Pass the edited compounds to the item, in case they didn't exist yet
            compound.setTag(displaysName, display);
            stack.setTagCompound(compound);
        }

        /**
         * Returns the <em>Lore</em> (description) of the {@code stack}, if existent.
         * <p>If the {@code stack} is neither providing any information regarding the lore
         * and its compounds nor is defined, an empty list is returned.
         *
         * @param stack the stack to get the lore off
         */
        public static List<String> getLore(ItemStack stack) {
            Validate.notNull(stack);
            List<String> lore = Lists.newArrayList();
            NBTTagCompound compound = createAbsent(stack);

            //Check for display compound
            if (!compound.hasKey("display", 10))
                return lore;

            //Get display compound and check for possible lore
            NBTTagCompound display = compound.getCompoundTag("display");
            if (!display.hasKey("Lore"))
                return lore;

            //Get the appended lore list and add it
            NBTTagList loreList = display.getTagList("Lore", 8);
            for (int j = 0; j < loreList.tagCount(); ++j) {
                lore.add(loreList.getStringTagAt(j));
            }
            return lore;
        }

    }

}
