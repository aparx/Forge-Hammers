package mindcubr.github.forge.hammers;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Random;

/**
 * No description available.
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
public class HammersHook {

    public static void setLore(ItemStack stack, ArrayList<String> lore) {
        Validate.notNull(stack);
        //Get compounds etc.
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        //Get display preferences
        NBTTagCompound display = new NBTTagCompound();
        if (compound.hasKey("display", 10))
            display = compound.getCompoundTag("display");
        compound.setTag("display", display);

        //Create new NBT lore list
        NBTTagList loreList = new NBTTagList();
        display.setTag("Lore", loreList);

        //Append to lore list
        for (int n = lore.size(), i = 0; i < n; ++i) {
            loreList.appendTag(new NBTTagString(lore.get(i)));
        }

        //Pass the edited compound to the item, in case it didn't exist yet
        stack.setTagCompound(compound);
    }

    public static ArrayList<String> getLore(ItemStack stack) {
        Validate.notNull(stack);
        ArrayList<String> lore = Lists.newArrayList();
        NBTTagCompound compound = stack.getTagCompound();
        if (!compound.hasKey("display", 10))
            return lore;

        NBTTagCompound display = compound.getCompoundTag("display");
        if (!display.hasKey("Lore"))
            return lore;

        NBTTagList loreList = display.getTagList("Lore", 8);
        for (int j = 0; j < loreList.tagCount(); ++j) {
            lore.add(loreList.getStringTagAt(j));
        }
        return lore;
    }

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
    public static String levelToString(int level) {
        //TODO update method, this is pretty lazy I admit
        StringBuilder builder = new StringBuilder();

        //Get level and check for compatibility
        level = Math.max(level, 1);
        if (level > 5)
            return String.valueOf(level);

        switch (level) {
            //I appendix as prefix
            case 4:
                builder.append('I');

                //I appendix as prefix (=5) or suffix (=4)
            case 5:
                builder.append('V');
                break;

            //Default implementation of: I, II, II
            default:
                //Implement I as roman number
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
     * TODO fact check
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
    public static void sendMessage(@Nonnull EntityPlayer receiver, @Nonnull Object message) {
        Validate.notNull(receiver);
        Validate.notNull(message);
        ChatComponentText text = new ChatComponentText("[Hammers] " + message);
        receiver.addChatComponentMessage(text);
    }

}
