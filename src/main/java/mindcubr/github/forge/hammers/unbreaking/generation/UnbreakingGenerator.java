package mindcubr.github.forge.hammers.unbreaking.generation;

import cpw.mods.fml.common.IWorldGenerator;
import mindcubr.github.forge.hammers.register.HammerBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.Random;

import static mindcubr.github.forge.hammers.hook.HammersHook.randomize;

/**
 * This class is a class ore generator of the ore {@link mindcubr.github.forge.hammers.unbreaking.BlockUnbreakingOre}.
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
public class UnbreakingGenerator implements IWorldGenerator {

    public static final UnbreakingGenerator SINGLETON = new UnbreakingGenerator();

    /**
     * The maximum spawn chance of ores in percent
     *
     * @apiNote This value might vary on chunk generation,
     * as multiple randomized processes occur and operate
     */
    private int maxSpawnRate = 15;

    /**
     * The maximum amount of ores pro chunk possible.
     * The maximum amount is equal to this but squared:
     * <pre><code>
     *     amountPerCh unkMax = maxPerChunk * maxPerChunk
     * </code></pre>
     *
     * @implNote By default, this is using the square root
     * of the number <b>8.0</b>. This is due to an easier
     * display, as it then stands for the actual maximum amount
     * of ores possible at max.
     */
    private int maxPerChunk = (int) Math.sqrt(8);

    /**
     * The actual radius of a vein to be calculated off.
     * This has no impact or effect on the {@link #maxPerChunk}
     * or {@link #maxSpawnRate}.
     */
    private int maxVeinRadius = 4;

    private int maxHeight = 20;

    private int minHeight = 5;

    /**
     * The maximum rate boundary, so the actual amount of percentage.
     * By default this is set to <code>100</code>.
     */
    private int spawnRateBoundary = Math.max(100, maxPerChunk);

    /**
     * Creates an new instance of this class and
     * registeres this as an event handler to the Forge <b>Event Bus</b>.
     */
    protected UnbreakingGenerator() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        Validate.notNull(random);
        Validate.notNull(chunkProvider);
        Validate.notNull(chunkGenerator);

        //Get world provider and world dimension for generation
        WorldProvider provider = world.provider;
        int dimension = provider.dimensionId;

        //Generate random ore, as this is allowed to
        generate(HammerBlocks.unbreakingOre, random, world, dimension, chunkX, chunkZ);
    }

    /**
     * Generates the {@code block} as ore in {@code world} in chunk
     * {@code chunkX} and {@code chunkZ}.
     *
     * @param block     the block ore, so the actual block
     *                  in our instance it is just the unbreaking ore,
     *                  but as separate block instance for possible RT changes
     * @param rand      the randomization using the {@code worlds} seed
     * @param world     the world where to spawn
     * @param dimension the dimension of the {@code world}
     * @param chunkX    the beginning tridi-x of chunk
     * @param chunkZ    the beginning tridi-z of chunk
     * @see #veinGenerate(World, Random, int, int, int, int, Block, int)
     */
    protected void generate(@Nonnull Block block, @Nonnull Random rand, @Nonnull World world,
                            int dimension, int chunkX, int chunkZ) {
        Validate.notNull(block);
        Validate.notNull(rand);
        Validate.notNull(world);

        int spawnRate = this.maxSpawnRate;
        int beginY = minHeight;
        Block target = Blocks.stone;

        //Manipulation for the environment attributes and parameters
        //to spawn the actual ore
        switch (dimension) {
            case 1: //End
                spawnRate += 8 /* 8 percent more in the End */;
                beginY += 50;
                target = Blocks.end_stone;
                break;
            case -1: //Nether
                spawnRate += 5 /* 5 percent more in the Nether */;
                beginY += 50;
                target = Blocks.netherrack;
                break;
            default: //Overworld or third parties world
                break;
        }

        //Loop through the spawn chance, that is also the amount
        //of possible equal ores in the same chunk
        for (int i = 0; i < maxPerChunk; i++) {
            //Generate a random vein
            veinGenerate(world, rand, chunkX, chunkZ, beginY, maxHeight, target, spawnRate);
        }
    }

    /**
     * Generates a possible vein at {@code world} at {@code chunkX}
     * {@code chunkZ} and inbetween the heights at a randomized
     * position calculated in here.
     *
     * @param world     the world where to generate the vein in
     * @param random    the randomizer used for randomization processes
     * @param chunkX    the beginning tridi-x of chunk
     * @param chunkZ    the beginning tridi-z of chunk
     * @param minHeight the minimum height of the vein
     * @param maxHeight the maximum height of the vein
     * @param target    the blocks inbetween a block can be generated and replaced
     *                  with this ore
     * @param spawnRate a copied version of this {@link #maxSpawnRate}, but as
     *                  parameter for changing purpose, e.g. that demands on
     *                  the dimension of generation
     * @since 3.0.0-alpha
     */
    private void veinGenerate(@Nonnull World world, @Nonnull Random random,
                              int chunkX, int chunkZ, int minHeight, int maxHeight,
                              Block target, int spawnRate) {
        Validate.notNull(world);
        Validate.notNull(random);
        Validate.notNull(target);

        //Calculate the center of the vein to be calculated onto
        int veinX = chunkX * 16 + randomize(random, 16);
        int veinZ = chunkZ * 16 + randomize(random, 16);
        int veinY = randomize(random, minHeight, maxHeight);
        int vein = randomize(random, maxPerChunk);
        int amount = 0;

        //Generation process block scope
        gen:
        {
            final int radius = maxVeinRadius;
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        int posX = veinX + x, posY = veinY + y, posZ = veinZ + z;

                        //The vein rate is the spawnRate but times the inverted amount of
                        //already spawned ores in that vein, to calculate and reach
                        //a better spawnrate.
                        if (!allowSpawn(random, Math.min(spawnRate, spawnRateBoundary)))
                            continue;

                        Block block = world.getBlock(posX, posY, posZ);
                        if (!block.isReplaceableOreGen(world, posX, posY, posZ, target))
                            continue;

                        world.setBlock(posX, posY, posZ, HammerBlocks.unbreakingOre);
                        if (++amount >= vein)
                            break gen;
                    }
                }
            }
        }
    }

    /**
     * Returns whether the {@code random} is returning a random boolean
     * with value <em>True</em> the amount of repeats
     * {@link #maxSpawnRate chance} gives.
     *
     * @param value  the actual rate required to return true,
     *               within {@link #spawnRateBoundary}.
     * @param random the random to determine the boolean value
     * @return whether the spawning should occur and is therefore is <em>True</em>
     * @see #spawnRateBoundary
     * @since 3.0.0-alpha
     */
    protected boolean allowSpawn(final Random random, int value) {
        Validate.notNull(random);
        return random.nextInt(Math.max(spawnRateBoundary, value + 1)) <= value;
    }


}
