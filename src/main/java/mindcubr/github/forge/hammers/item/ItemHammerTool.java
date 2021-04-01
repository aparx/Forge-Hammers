package mindcubr.github.forge.hammers.item;

import cpw.mods.fml.common.registry.GameRegistry;
import lombok.Getter;
import mindcubr.github.forge.hammers.HammerElement;
import mindcubr.github.forge.hammers.HammersMod;
import mindcubr.github.forge.hammers.Reference;
import mindcubr.github.forge.hammers.hook.HammersHook;
import mindcubr.github.forge.hammers.register.HammerBlocks;
import mindcubr.github.forge.hammers.register.HammerItems;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

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
 * @see HammerElement
 * @since 1.0.0-0.1
 */
public class ItemHammerTool extends ItemPickaxe implements HammerElement {

    /**
     * The actual growing rate exponent of basis to tenth, multiplied
     * with a certain calculated method.
     *
     * @apiNote Non-final protected modifier for possible bytecode and/or
     * reflection manipulation. It is highly recommended to not use a grow
     * rate above {@code 4}.
     * @see Math#pow(double, double)
     * @see #getLevel()
     * @see #calcDamage(int)
     */
    protected static double damageGrowRate = 2;

    /**
     * The damage curvature.
     */
    private static final double curvature = 1.1;

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
        this.radial = getRadial();              //Changes to value at RT won't apply anymore
        this.cubes = radial * radial * radial;  //Calculation of the cubes involved

        //Calculate the maximum damage of this item
        setMaxDamage(calcDamage(this.radial));

        //Update locals
        String strmat = HammersHook.lowerAlternativeToolName(material);
        String branch = "hammer_" + strmat + '_' + getLevel();
        setUnlocalizedName(branch);
        setTextureName(Reference.RESOURCE_PREFIX + branch);
        setCreativeTab(HammersMod.CREATIVE_TAB);
    }

    /**
     * Calculates the maximum damage of this item and returns it.
     * <p>Per destroyed block, this hammer loses exactly <b>one</b> durability.
     * <p>This method is using this {@link #damageGrowRate} as key factor.
     *
     * @return The maximum damage of the item, to be returned any possibly set;
     * @author mindcubr
     * @implNote If you want to overwrite it, you can do so.
     * @see #damageGrowRate
     * <em>{@link #ItemHammerTool See constructor}</em>.
     * @see #ItemHammerTool(ItemHammerLevel, ToolMaterial)
     */
    public int calcDamage(int radial) {
        ToolMaterial material = getToolMaterial();
        int crops = material.getHarvestLevel();
        int level = crops + getLevel();
        double curve = Math.pow(level, curvature);
        double base = Math.min(Math.max(10 - radial, 1), 10);
        double multiplier = Math.pow(base, damageGrowRate);
        return (int) (curve * cubes * multiplier);
    }

    /**
     * Returns the amount of blocks left until the hammer destroys.
     *
     * @param stack the input stack to calculate
     * @return the amount of blocks left to be destroyable.
     */
    public int getBlocksLeft(@Nonnull ItemStack stack) {
        //Validate and check if item is unbreakable
        Validate.notNull(stack);
        if (!stack.isItemStackDamageable())
            return -1;

        //Return blocks left
        return Math.max(getMaxDamage() - getDamage(stack), 1);
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

        //Increase NBT usage amount of the stack
        int usages = usagesIncr(stack);

        //Determines the radius, off the radial length
        final int radius = getRadial() / 2;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    //Redefine the coordinates related to the iteration
                    int xPos = breakX + x, yPos = breakY + y, zPos = breakZ + z;

                    //Check if the block is unbreakable
                    if (!isBreakable(world, xPos, yPos, zPos))
                        continue;

                    //Destroy block and damage the item, if possible
                    if (world.func_147480_a(xPos, yPos, zPos, true /*drop*/)) {
                        //Damage item, that returns, whether to reset the usages
                        if (damageItem(stack, 1, player)) {
                            usagesReset(stack);
                        }
                    }
                }
            }
        }

        return state;
    }

    /**
     * Damages the input {@code stack} based on certain conditions and
     * values with the amount of input {@code damage} and resets
     * the usage if required.
     * <p>If the {@code stack} is enchanted this method is enhanced
     * in <code>2.0.0</code> to check for <em>unbreaking</em> enchantment
     * and its level. The enhanced method includes a usage count to alter
     * when to damage the item and even <em>if</em>.
     *
     * @param stack the stack passed, not {@code null}
     * @return whether the damage was completed. This is mostly used
     * in relation to check whether to reset the usages of the input
     * {@code stack}.
     */
    public boolean damageItem(ItemStack stack, int damage, EntityLivingBase holder) {
        //Validate and check for no damage possibility
        Validate.notNull(stack);
        if (!stack.isItemStackDamageable())
            return false;

        //Check if item is enchanted
        if (stack.isItemEnchanted()) {
            int ubId = Enchantment.unbreaking.effectId;
            int level;
            if ((level = EnchantmentHelper
                    .getEnchantmentLevel(ubId, stack)) != 0) {
                final int usages = usagesGet(stack);
                final int alive = 2 * level; //Unbreaking I: every 2nd, II: 4th, III: 6th, ...
                if (usages < alive)
                    return false;
            }
        }

        //Damage the item, if possible
        stack.damageItem(damage, holder);
        return true;
    }

    /**
     * Returns the amount of usages the {@code stack} has.
     *
     * @param stack the item stack containing the possible compound
     * @return the usages the stack has, if no one is set, zero is returned.
     */
    protected final int usagesGet(@Nonnull ItemStack stack) {
        Validate.notNull(stack);
        NBTTagCompound compound = HammersHook.ItemHook.createAbsent(stack);
        if (compound.hasKey("usages"))
            return compound.getInteger("usages");

        //Return zero, as no compound is set or not contained
        return 0;
    }

    /**
     * Increases the amount of usages of the {@code stack}.
     *
     * @param stack the item stack containing the possible compound
     * @return the new usages amount of the {@code stack}
     */
    protected final int usagesIncr(@Nonnull ItemStack stack) {
        Validate.notNull(stack);
        NBTTagCompound compound = HammersHook.ItemHook.createAbsent(stack);
        int usages = usagesGet(stack);
        compound.setInteger("usages", ++usages);
        return usages;
    }

    /**
     * Resets the amount of usages of the {@code stack}.
     *
     * @param stack the item stack containing the possible compound
     */
    protected final void usagesReset(@Nonnull ItemStack stack) {
        Validate.notNull(stack);
        NBTTagCompound compound = HammersHook.ItemHook.createAbsent(stack);
        compound.setInteger("usages", 0);
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

        //Denies instant break of unbreaking material with non unbreaking hammer
        //since '3.0.0-alpha'
        if (block == HammerBlocks.unbreakingOre
                && this.toolMaterial != HammerItems.UNBREAKING_MATERIAL)
            return false;

        //Check if block is just a normal cube or not
        return block.isNormalCube();
    }

    @Override
    public void registerRecipe() {
        Object thisRecipeRep = getRecipeRepresentative();
        GameRegistry.addRecipe(new ItemStack(this), "ODO", "RSR", " S ",
                'O', Blocks.obsidian, 'D', Items.diamond, 'R', thisRecipeRep, 'S', Items.stick);

        /* Begin registering every level recipe combined with this instance */
        ItemHammerLevel[] itemLevels = HammerItems.itemHammerLevels;
        for (int n = itemLevels.length, i = 0; i < n; i++) {
            //Get level from array and then check for concurrent bind.
            ItemHammerLevel itemLevel = itemLevels[i];
            if (!itemLevel.equals(this.boundary)) {
                //The iterated level is not bound with this instance
                ItemHammerTool equality = HammerItems.SINGLETON.findTool(itemLevel, toolMaterial);
                GameRegistry.addRecipe(createDownUpRecipes(new ItemStack(equality), itemLevel));
            }
        }
    }

    /**
     * @implNote This method is loading this {@link #registerRecipe() recipes}.
     */
    @Override
    public void load() {
        GameRegistry.registerItem(this, getBranch());
        registerRecipe();
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
     * Returns the in recipe used item, to declare this special type
     * of {@link #toolMaterial tool material}.
     * <p>This result is either an {@link Item} or a {@link Block}.
     *
     * @return the item representative
     * @see #toolMaterial
     * @see net.minecraft.item.Item.ToolMaterial
     */
    @Nonnull
    public Object getRecipeRepresentative() {
        //Check if tool material is unbreaking material, return ingots
        if (toolMaterial == HammerItems.UNBREAKING_MATERIAL)
            return HammerItems.UNBREAKING_INGOT;

        //Check for existing materials
        switch (toolMaterial) {
            //Diamond material
            case EMERALD:
                return Blocks.diamond_block;

            //Stone material
            case STONE:
                return Blocks.stone;

            //Golden material
            case GOLD:
                return Blocks.gold_block;

            //Iron material
            case IRON:
                return Blocks.iron_block;

            //Anything else, wooden included
            default:
                return Blocks.planks;
        }
    }

    /**
     * Returns this {@link #boundary} {@link ItemHammerLevel#getRadial()},
     * that is determined by the {@linkplain #boundary} itself.
     * <p>{@link ItemHammerLevel#getRadial() Read more about the radial value}.
     *
     * @return the radial length of this {@linkplain #boundary}.
     * @see ItemHammerLevel#getRadial()
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

    /**
     * Adds additional hover information for the viewer or player to see.
     * <p>Client sided.
     *
     * @param stack  the itemstack to be added additional information
     * @param player the player whom to belong this item
     * @param info   the information list containing the lines to be
     *               additional
     * @param param  unknown and unimportant parameter
     */
    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean param) {
        final List<String> desc = (List<String>) info;
        Item item = stack.getItem();
        if (item instanceof ItemHammerTool) {
            int blocksLeft = getBlocksLeft(stack);
            ItemHammerTool tool = (ItemHammerTool) item;
            int thisLevel = tool.getLevel();
            desc.add("\u00a7aLevel " + HammersHook.levelToString(tool.getLevel()));
            desc.add("\u00a79" + tool.getRadial() + " cubic meter");
            desc.add("\u00a79Blocks Left: \u00a77" + (blocksLeft < 0 ? "Infinite" : blocksLeft));

            /* Check if current item is displayed as result within a crafting bench */
            if (player.openContainer instanceof ContainerWorkbench) {
                ContainerWorkbench workbench = (ContainerWorkbench) player.openContainer;

                //Get workbenches output and negated compare check
                ItemStack result = workbench.craftResult.getStackInSlot(0);
                if (result != stack)
                    return;

                /* Iterate matrix to check for comparable tool, to determine downgrade or upgrade.*/
                InventoryCrafting matrix = workbench.craftMatrix;
                for (int n = matrix.getSizeInventory(), i = 0; i < n; i++) {
                    //Get item and undefine-check
                    ItemStack itr = matrix.getStackInSlot(i);
                    if (itr == null)
                        continue;

                    //Get item and compare
                    Item itmItr = itr.getItem();
                    if (itmItr instanceof ItemHammerTool) {
                        ItemHammerTool other = (ItemHammerTool) itmItr;
                        int otherLevel = other.getLevel();
                        desc.add(StringUtils.EMPTY);
                        if (otherLevel > thisLevel) //Append downgrade info
                            desc.add("\u00a74\u00a7lDOWNGRADE \u2193");
                        else //Append upgrade info
                            desc.add("\u00a7a\u00a7lUPGRADE \u2191");

                        String fromLevel = HammersHook.levelToString(otherLevel);
                        String toLevel = HammersHook.levelToString(thisLevel);
                        desc.add(String.format("\u00a77Level %s \u21D2 Level %s", fromLevel, toLevel));
                        break;
                    }
                }
            }
        }
    }

    /**
     * Creates a recipe out of the {@code output}, the input {@code level} and
     * this instance.
     * <p>The recipe is used to e.g. upgrade or downgrade this tool instance
     * with a certain {@link ItemHammerLevel level element item}.
     *
     * @param output the output of the recipe, so the up- or downgraded
     *               tool instance in this case
     * @param level  the {@linkplain ItemHammerLevel level item} required to access this {@code output}
     * @return the baked recipe of both inputs
     * @see ItemHammerLevel
     */
    private IRecipe createDownUpRecipes(ItemStack output, ItemHammerLevel level) {
        final ItemHammerTool instance = this;
        return new IRecipe() {
            @Override
            public boolean matches(InventoryCrafting inv, World world) {
                //Last iterated item, that was successful
                Item lastDefined = null;
                int matches = 0;

                //Iterate through the slots: i
                for (int n = getRecipeSize(), i = 0; i < n; i++) {
                    //Get stack and undefine-check
                    ItemStack stack = inv.getStackInSlot(i);
                    if (stack == null)
                        continue;

                    //Get item off stack and do final comparison
                    Item item = stack.getItem();
                    if (!Objects.equals(item, instance)
                            && !Objects.equals(item, level))
                        return false;

                    //Do not allow twice the same items
                    if (lastDefined == item)
                        return false;

                    //Increase the amount of matches
                    ++matches;

                    //Redefine last iterated success item to avoid multiple causes
                    //to exist. As twice the same items, which would be illegal.
                    lastDefined = item;
                }
                return matches == 2;
            }

            @Override
            public ItemStack getCraftingResult(InventoryCrafting inv) {
                return getRecipeOutput();
            }

            @Override
            public int getRecipeSize() {
                return 9;
            }

            @Override
            public ItemStack getRecipeOutput() {
                return output;
            }
        };
    }

    /**
     * Override block destroyed event.
     */
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World w, Block b, int x, int y, int z, EntityLivingBase exec) {
        return false;
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
