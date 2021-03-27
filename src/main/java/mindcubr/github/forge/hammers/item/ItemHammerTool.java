package mindcubr.github.forge.hammers.item;

import lombok.Getter;
import mindcubr.github.forge.hammers.HammerItem;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

/**
 * A <b>Hammer</b> tool is used to destroy a certain range of blocks
 * with a single pickaxe-like breaking of a block in the center of that
 * specified range.
 * <p>The <em>Hammer Tool</em> is especially useful, when destroying
 * large areas. This class contains information and the processors and executors,
 * so a <blockquote>Hammers Tool</blockquote> exists.
 * <p>Using this hammer is like using a pickaxe, of the same given {@link net.minecraft.item.Item.ToolMaterial}.
 * But with a different item damage and processing algorithm including a
 * different handling of when, what and how the processing and breaking of a
 * block is happening.
 *
 * @author mindcubr
 * @see ItemPickaxe
 * @see HammerItem
 * @since 1.0.0-0.1
 */
public class ItemHammerTool extends ItemPickaxe implements HammerItem {

    /**
     * The exponential damage growing rate is used to calculate the
     * maximum damage the item has.
     * <p>By default, this value is used as base for the exponent
     * of {@code level} used. The level is also defined by the {@link #boundary}
     * and can be seen with {@link #getLevel()}.</p>
     * <p>The higher the growing rate, the higher the maximum usability.<br>
     * By default:
     * <pre>
     *     = damageGrowRate {@link Math#pow(double, double) pow} {@linkplain #getLevel() level}
     * </pre>
     *
     * @apiNote Non-final protected modifier for possible bytecode and/or
     * reflection manipulation.
     * @see Math#pow(double, double)
     * @see #getLevel()
     * @see #calcDamage(int)
     */
    protected static int damageGrowRate = 3;

    /**
     * The bounded hammer level required for this hammer tool
     * to proceed the processing procedures.
     */
    @Getter
    private final ItemHammerLevel boundary;

    /**
     * The amount of cubes possibly break, based on this
     * {@link #getRadial() radial} length.
     */
    private final int cubes;

    /**
     * The radial length of this {@link #boundary}.
     */
    private final int radial;


    public ItemHammerTool(@Nonnull ItemHammerLevel level, @Nonnull ToolMaterial material) {
        super(Validate.notNull(material));
        Validate.notNull(level);
        this.boundary = level;
        this.radial = getRadial();              //Changes to value at RT won't apply anymore TODO
        this.cubes = radial * radial * radial;  //Calculation of the cubes involved

        //Calculate the maximum damage of this item
        setMaxDamage(calcDamage(this.radial));
    }

    /**
     * Calculates the maximum damage of this item and returns it.
     * <p>Per destroyed block, this hammer loses exactly <b>one</b> durability.
     * <p>This method is using this {@link #damageGrowRate} as key factor.
     *
     * @return The maximum damage of the item, to be returned any possibly set;
     * @implNote If you want to overwrite it, you can do so.
     * @see #damageGrowRate
     * <em>{@link #ItemHammerTool See constructor}</em>.
     * @see #ItemHammerTool(ItemHammerLevel, ToolMaterial)
     */
    public int calcDamage(int radial) {
        ToolMaterial material = getToolMaterial();
        int crops = material.getHarvestLevel();
        int level = crops + getLevel();
        int usages = (int) Math.pow(damageGrowRate, level);
        return Math.max(radial * level * cubes * usages, -1);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, int breakX, int breakY, int breakZ, EntityPlayer player) {
        //The state is equal the cancel state. The superclass' method is empty
        //this is just a placeholder for possible future variables. TODO
        boolean state = super.onBlockStartBreak(stack, breakX, breakY, breakZ, player);

        //Don't pass illegal arguments
        if (stack == null)
            return state;

        //Get world and check for remote XOR state
        final World world = player.worldObj;
        if (world.isRemote ^ state)
            return state;

        //The victim block is the block, that is going to be breaked
        if (!isBreakable(world, breakX, breakY, breakZ))
            return state;

        //Determines the radius, off the radial length
        final int radius = getRadial();
        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    //Redefine the coordinates related to the iteration
                    int xPos = breakX + x, yPos = breakY + y, zPos = breakZ + z;

                    //Check if the block is unbreakable
                    if (!isBreakable(world, xPos, yPos, zPos))
                        continue;

                    //Destroy block and damage the item, if possible
                    world.func_147480_a(xPos, yPos, zPos, true /*drop*/);
                    damageItem(stack, player);
                }
            }
        }

        return state;
    }

    /**
     * Damages the input {@code stack} based on certain conditions and
     * values.
     *
     * @param stack the stack passed, not {@code null}
     */
    public void damageItem(ItemStack stack, EntityLivingBase holder) {
        //Validate and check for no damage possibility
        Validate.notNull(stack);
        if (!stack.isItemStackDamageable())
            return;

        //Damage the item, if possible
        stack.damageItem(1, holder);
    }


    /**
     * Returns whether the block within the {@code world} at {@code x}, {@code y}
     * and {@code z} coordinate is breakable for the hammer or not.
     *
     * @param world the world, where the target block is located in
     * @param x     the <em>tridi-</em>coordinate x, the horizontal position of the block
     * @param y     the <em>tridi-</em>-coordinate y, the vertical position of the block
     * @param z     the <em>tridi-</em>-coordinate z, the depth of the blocks position
     * @return <em>True</em> if the measured block is really breakable, and <em>False</em>
     * otherwise.
     */
    public final boolean isBreakable(World world, int x, int y, int z) {
        //Get block and undefine check
        Block block = world.getBlock(x, y, z);
        if (block == null)
            return false;

        //Check for compatibility via minecraft methods
        if (!func_150897_b(block))
            return false;

        //Check if hardness is equal to unbreaking
        if (block.getBlockHardness(world, x, y, z) < 0.0F)
            return false;

        //Check if block is just a normal cube or not
        return block.isNormalCube();
    }

    @Override
    public void registerRecipe() {

    }

    /**
     * @implNote This method is loading this {@link #registerRecipe() recipes}.
     */
    @Override
    public void load() {

    }

    /**
     * Returns this {@link #boundary boundary's} level.
     * <p>This level is used for the calculation of the radius,
     * of this <em>Hammer Tool</em>.
     *
     * @return the level of this {@linkplain #boundary}.
     * @see ItemHammerLevel#getLevel()
     * @see #boundary
     * @see #getBoundary()
     */
    public int getLevel() {
        return boundary.getLevel();
    }

    /**
     * Returns this {@link #boundary} radial, that is determined
     * by the {@linkplain #boundary} itself.
     * <p>The radius is used to
     *
     * @return
     */
    public int getRadial() {
        return boundary.getRadial();
    }

    /**
     * Returns this {@link #func_150913_i() tool material}.
     * <p>This method is equivalent to {@link #func_150913_i()} but does represent
     * a more valid name to it.
     *
     * @return the tool material of this tool.
     */
    public ToolMaterial getToolMaterial() {
        return func_150913_i();
    }

}
