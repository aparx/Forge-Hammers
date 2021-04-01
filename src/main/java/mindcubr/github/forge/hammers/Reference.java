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
     * The <em>resource prefix</em> is the prefix for the resource location
     * to locate the right texture for the right {@linkplain #MOD_ID modification identifier}.
     * <p>Generally the rsrc-prefix (Resource Prefix) is based on the
     * {@code mod-id} and a {@code colon} as trailing with the resource path leading:
     * <pre>
     *     = {@link #MOD_ID} + ':' + <em>&lt;PATH_TO_RESOURCE&gt;</em>
     * </pre>
     *
     * @see #MOD_ID
     */
    public static final String RESOURCE_PREFIX = MOD_ID + ':';

    /**
     * The current version display of this modification, by using
     * the <b>Semantic Versioning</b>, that can be found
     * <a href="https://semver.org/">here</a>.
     *
     * @see <a href="https://semver.org/">https://semver.org/</a>
     */
    public static final String MOD_VERSION = "3.0.0-alpha";

}
