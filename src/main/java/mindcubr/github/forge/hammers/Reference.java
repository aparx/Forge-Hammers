package mindcubr.github.forge.hammers;

/**
 * The <em>Reference</em> class contains little information about
 * the base modification itself.
 * <ul>
 *     <li>{@link #MOD_ID}</li>
 *     <li>{@link #MOD_VERSION}</li>
 * </ul>
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
public class Reference {

    /**
     * The modification id for the FML loading process required.
     * This ID is also used to implement textures and resources in general, etc.
     */
    public static final String MOD_ID = "hammers";

    /**
     * The current version of the modification, by using
     * the <b>Semantic Versioning</b> that can be found
     * <a href="https://semver.org/">here</a>.
     *
     * @see <a href="https://semver.org/">https://semver.org/</a>
     */
    public static final String MOD_VERSION = "1.0.0-0.1";

}
