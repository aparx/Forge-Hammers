package mindcubr.github.forge.hammers.unbreaking.generation;

import cpw.mods.fml.common.IWorldGenerator;
import mindcubr.github.forge.hammers.register.HammerBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.Random;

import static mindcubr.github.forge.hammers.HammersHook.randomize;

/**
 * This class is a class ore generator of the ore {@link mindcubr.github.forge.hammers.unbreaking.BlockUnbreakingOre}.
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
public class UnbreakingGenerator implements IWorldGenerator {

    public static final UnbreakingGenerator SINGLETON = new UnbreakingGenerator();

    private int spawnChance = 2;

    private int vienChance = 4;

    private int vienMaxY = 20;

    private int vienMinY = 5;

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

    void generate(@Nonnull Block block, @Nonnull Random rand, @Nonnull World world,
                  int dimension, int chunkX, int chunkZ) {
        Validate.notNull(block);
        Validate.notNull(rand);
        Validate.notNull(world);

        //Check whether to generate the ore or not
        for (int i = 0; i <= spawnChance; i++) {
            if (!rand.nextBoolean())
                return;
        }

        int vienChance = this.vienChance;
        int vienBeginY = this.vienMaxY;
        int vienMinY = this.vienMinY;

        //Region where to spawn the ore
        Block target = Blocks.stone;
        switch (dimension) {
            case 1: //End
                vienChance += 15;
                vienBeginY += 15;
                target = Blocks.end_stone;
            case -1: //Nether
                vienBeginY += 100;
                target = Blocks.netherrack;
                break;
            default: //Overworld or third
                break;
        }

        int length = randomize(rand, vienChance);
        int height = randomize(rand, vienBeginY - vienMinY);
        WorldGenMinable generator = new WorldGenMinable(block, length, target);

        //Loop through the spawn chance, that is also the amount
        //of possible equal ores in the same chunk
        for (int i = 0; i < spawnChance; i++) {
            //Random horizontal position inbetween the chunk
            int xPos = chunkX * 16 + randomize(rand, 16);
            int zPos = chunkZ * 16 + randomize(rand, 16);

            //Random vertical position inbetween the height
            int yPos = randomize(rand, height) + vienMinY;
            if (!generator.generate(world, rand, xPos, yPos, zPos))
                break;
        }

    }

}
