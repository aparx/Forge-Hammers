package mindcubr.github.forge.hammers.register;

import com.google.common.collect.Lists;
import mindcubr.github.forge.hammers.HammerElement;
import mindcubr.github.forge.hammers.hook.Register;
import mindcubr.github.forge.hammers.unbreaking.BlockUnbreakingOre;

/**
 * A non-register list of blocks.
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
public class HammerBlocks extends Register<HammerElement> {


    /**
     * Default singleton instance of this class.
     */
    public static final HammerBlocks SINGLETON = new HammerBlocks();

    /**
     * The unbreaking ore instance.
     */
    public static BlockUnbreakingOre unbreakingOre = new BlockUnbreakingOre();

    protected HammerBlocks() {
        super(Lists.newArrayList());
    }

    /* TODO: automation for field recognition for multiple fields, if required */
    static {
        //Register unbreaking ore
        SINGLETON.register(unbreakingOre);
    }


}
