package de.uni_stuttgart.it_rex.media.security.written;

import de.uni_stuttgart.it_rex.media.domain.written.enumeration.COURSEROLE;
import de.uni_stuttgart.it_rex.media.domain.written.enumeration.REXROLE;

import java.util.UUID;

/**
 * Constants for internal use.
 */
public final class RexAuthzConstants {
    /**
     * The string template for a {@link REXROLE} role string.
     */
    public static final String TEMPLATE_REX_ROLE = "ROLE_ITREX_%s";

    /**
     * The number of components the TEMPLATE_REX_ROLE String is made of.
     */
    public static final Integer TEMPLATE_REX_ROLE_COMPONENTS = 3;

    /**
     * The match string for a {@link REXROLE} string.
     */
    public static final String MATCHER_ROLE_REX = "ROLE_ITREX_";


    /**
     * The string template for a {@link COURSEROLE} role string. <p>
     * - 1st param: {@link UUID} as string <p>
     * - 2nd param: {@link COURSEROLE} as string <p>
     */
    public static final String TEMPLATE_COURSE_ROLE = "ROLE_COURSE_%1$s_%2$s";

    /**
     * The number of components the TEMPLATE_COURSE_ROLE String is made of.
     */
    public static final Integer TEMPLATE_COURSE_ROLE_COMPONENTS = 4;

    /**
     * The string template for a {@link COURSEROLE} group string. <p>
     * - 1st param: {@link UUID} as string <p>
     * - 2nd param: {@link COURSEROLE} as string <p>
     */
    public static final String TEMPLATE_COURSE_GROUP = "COURSE_%1$s_%2$s";

    /**
     * The match string for a {@link COURSEROLE} string.
     */
    public static final String MATCHER_ROLE_COURSE = "ROLE_COURSE_";

    /**
     * Do not create an instance of this class.
     */
    private RexAuthzConstants() {
    }
}
