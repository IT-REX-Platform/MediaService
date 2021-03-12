package de.uni_stuttgart.it_rex.media.security.written;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.uni_stuttgart.it_rex.media.domain.written.enumeration.COURSEROLE;
import de.uni_stuttgart.it_rex.media.domain.written.enumeration.REXROLE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.GrantedAuthority;

/**
 * Provides functionality to check the roles of a user. This includes both
 * system wide and course specific roles. <p>
 * These are the classes currently used: <p>
 * Authentication: <p>
 * org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
 * Principal: <p>
 * org.springframework.security.oauth2.jwt.Jwt <p>
 * Details: <p>
 * org.springframework.security.web.authentication.WebAuthenticationDetails <p>
 * <p>
 * Roles need to start with "ROLE_"
 */
public final class RexAuthz {
    private RexAuthz() {
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(RexAuthz.class);

    // ----- PUBLIC STRING API -----

    /**
     * Returns the role string given a {@link REXROLE}.
     *
     * @param role the role to make the role string for.
     * @return the role string.
     */
    public static String getRexRoleString(final REXROLE role) {
        return makeStringFromTemplate(RexAuthzConstants.TEMPLATE_REX_ROLE,
            role);
    }

    /**
     * Returns the role string given a course {@link UUID}
     * and a {@link COURSEROLE}.
     *
     * @param courseId the {@link UUID} of the course.
     * @param role     the {@link COURSEROLE} to make the role string for.
     * @return the role string.
     */
    public static String getCourseRoleString(final UUID courseId,
                                             final COURSEROLE role) {
        return makeStringFromTemplate(RexAuthzConstants.TEMPLATE_COURSE_ROLE,
            courseId, role);
    }

    /**
     * Returns the group string given a course {@link UUID}
     * and a {@link COURSEROLE}.
     *
     * @param courseId the {@link UUID} of the course.
     * @param role     the {@link COURSEROLE} to make the group string for.
     * @return the group string.
     */
    public static String getCourseGroupString(final UUID courseId,
                                              final COURSEROLE role) {
        return makeStringFromTemplate(RexAuthzConstants.TEMPLATE_COURSE_GROUP,
            courseId, role);
    }

    // ----- PUBLIC USER API -----

    /**
     * Returns the {@link UUID} of the current user.
     *
     * @return the user {@link UUID}.
     * @throws RexAuthzException if JWT does not contain a UUID in field "sub"
     */
    public static UUID getUserId() {
        UUID uuid;
        try {
            uuid = UUID.fromString(getUserAuthn().getName());
        } catch (Exception e) {
            String msg = "Field sub in JWT does not contain a valid UUID";
            LOGGER.error(msg);
            throw new RexAuthzException(msg);
        }
        return uuid;
    }

    /**
     * Returns the name of the user
     *
     * @return the name as string if extraction was successful,
     * empty {@link Optional} otherwise.
     */
    public static Optional<String> getName() {
        return getClaimFromToken("name");
    }

    /**
     * Returns the user name of the user
     *
     * @return the user name as string if extraction was successful,
     * empty {@link Optional} otherwise.
     */
    public static Optional<String> getUserName() {
        return getClaimFromToken("preferred_username");
    }

    /**
     * Returns the given name of the user
     *
     * @return the given name as string if extraction was successful,
     * empty {@link Optional} otherwise.
     */
    public static Optional<String> getGivenName() {
        return getClaimFromToken("given_name");
    }

    /**
     * Returns the family name of the user
     *
     * @return the family name as string if extraction was successful,
     * empty {@link Optional} otherwise.
     */
    public static Optional<String> getFamilyName() {
        return getClaimFromToken("family_name");
    }

    /**
     * Returns the email of the user
     *
     * @return the email as string if extraction was successful,
     * empty {@link Optional} otherwise.
     */
    public static Optional<String> getEmail() {
        return getClaimFromToken("email");
    }

    /**
     * Returns the {@link REXROLE} of the current user.
     *
     * @return the {@link REXROLE}.
     * @throws RexAuthzException if User has no REXROLE.
     * @throws RexAuthzException if User has more than one REXROLE.
     */
    public static REXROLE getRexRole() {
        Set<String> rexRoles = getUserAuthorities().stream()
            .filter(o -> o.startsWith(RexAuthzConstants.MATCHER_ROLE_REX))
            .collect(Collectors.toSet());
        Set<REXROLE> rexRolesStr =
            rexRoles.stream().map(RexAuthz::getRexRoleFromRoleString)
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toSet());
        if (rexRolesStr.isEmpty()) {
            String msg = "User has no RexRole";
            LOGGER.error(msg);
            throw new RexAuthzException(msg);
        }
        if (rexRolesStr.size() > 1) {
            String msg = "User has more than one RexRole";
            LOGGER.error(msg);
            throw new RexAuthzException(msg);
        }
        return rexRolesStr.iterator().next();
    }

    /**
     * Checks whether the current user has a specific {@link REXROLE}.
     *
     * @param role the {@link REXROLE} to be checked.
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userHasRexRole(final REXROLE role) {
        return userHasAuthority(getRexRoleString(role));
    }

    /**
     * Checks whether the current user has the {@link REXROLE}
     * <code>REXROLE.ADMIN</code>.
     *
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userIsRexAdmin() {
        return userHasRexRole(REXROLE.ADMIN);
    }

    /**
     * Checks whether the current user has the {@link REXROLE}
     * <code>REXROLE.LECTURER</code>.
     *
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userIsRexLecturer() {
        return userHasRexRole(REXROLE.LECTURER);
    }

    /**
     * Checks whether the current user has the {@link REXROLE}
     * <code>REXROLE.STUDENT</code>.
     *
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userIsRexStudent() {
        return userHasRexRole(REXROLE.STUDENT);
    }

    /**
     * Returns the Set of course {@link UUID}s
     * in which the current user participates.
     *
     * @return Set of course {@link UUID}.
     */
    public static Set<UUID> getCoursesOfUser() {
        Set<String> courseRoles = getUserAuthorities().stream()
            .filter(o -> o.startsWith(RexAuthzConstants.MATCHER_ROLE_COURSE))
            .collect(Collectors.toSet());
        return courseRoles.stream().map(RexAuthz::getCourseIdFromRoleString)
            .filter(Optional::isPresent)
            .map(Optional::get).collect(Collectors.toSet());
    }

    /**
     * Returns the Map of course {@link UUID}s
     * and the related {@link COURSEROLE}s
     * in which the current user participates.
     *
     * @return Map of course {@link UUID} and {@link COURSEROLE}.
     */
    public static Map<UUID, COURSEROLE> getCoursesAndRolesOfUser() {
        Set<String> courseRoles = getUserAuthorities().stream()
            .filter(o -> o.startsWith(RexAuthzConstants.MATCHER_ROLE_COURSE))
            .collect(Collectors.toSet());
        return courseRoles.stream()
            .map(RexAuthz::getCourseIdAndRoleFromRoleString)
            .filter(Optional::isPresent)
            .map(Optional::get).collect(
                Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
    }

    /**
     * Checks whether the current user has a
     * specific {@link COURSEROLE} for a given course {@link UUID}.
     *
     * @param courseId the {@link UUID} of the course.
     * @param role     the {@link COURSEROLE} to be checked.
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userHasCourseRole(final UUID courseId,
                                            final COURSEROLE role) {
        return userHasAuthority(getCourseRoleString(courseId, role));
    }

    /**
     * Checks whether the current user has the {@link COURSEROLE}
     * <code>REXROLE.PARTICIPANT</code> for a given course {@link UUID}.
     *
     * @param courseId the {@link UUID} of the course.
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userIsCourseParticipant(final UUID courseId) {
        return userHasCourseRole(courseId, COURSEROLE.PARTICIPANT);
    }

    /**
     * Checks whether the current user has one of the
     * following {@link COURSEROLE}s for a given course {@link UUID}. <p>
     * - <code>REXROLE.PARTICIPANT</code> <p>
     * - <code>REXROLE.MANAGER</code> <p>
     * - <code>REXROLE.OWNER</code> <p>
     *
     * @param courseId the {@link UUID} of the course.
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userIsCourseParticipantOrManagerOrOwner(
        final UUID courseId) {
        return (userHasCourseRole(courseId, COURSEROLE.PARTICIPANT)
            || userHasCourseRole(courseId, COURSEROLE.MANAGER)
            || userHasCourseRole(courseId, COURSEROLE.OWNER));
    }

    /**
     * Checks whether the current user has one of the
     * following {@link COURSEROLE}s for a given course {@link UUID}.<p>
     * - <code>REXROLE.MANAGER</code> <p>
     * - <code>REXROLE.OWNER</code> <p>
     *
     * @param courseId the {@link UUID} of the course.
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userIsCourseManagerOrOwner(final UUID courseId) {
        return (userHasCourseRole(courseId, COURSEROLE.MANAGER)
            || userHasCourseRole(courseId, COURSEROLE.OWNER));
    }

    /**
     * Checks whether the current user has one of the
     * following {@link COURSEROLE}s for a given course {@link UUID}.<p>
     * - <code>REXROLE.OWNER</code> <p>
     *
     * @param courseId the {@link UUID} of the course.
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userIsCourseOwner(final UUID courseId) {
        return userHasCourseRole(courseId, COURSEROLE.OWNER);
    }

    // ----- PRIVATE API -----

    /**
     * Returns a string given a template, a course {@link UUID}
     * and a {@link COURSEROLE}.
     *
     * @param template the format template which will be filled.
     * @param courseId the course {@link UUID}.
     * @param role     the {@link COURSEROLE}.
     * @return a string containing the filled-in parameters.
     */
    private static String makeStringFromTemplate(final String template,
                                                 final UUID courseId,
                                                 final COURSEROLE role) {
        return String.format(template, courseId, role.toString());
    }

    /**
     * Returns a string given a template and a {@link REXROLE}.
     *
     * @param template the format template which will be filled.
     * @param role     the {@link REXROLE}.
     * @return a string containing the filled-in parameters.
     */
    private static String makeStringFromTemplate(final String template,
                                                 final REXROLE role) {
        return String.format(template, role.toString());
    }

    /**
     * Returns the {@link Authentication} of the current user.
     *
     * @return {@link Authentication}.
     */
    private static Authentication getUserAuthn() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Extracts the {@link REXROLE} given a {@link REXROLE} role string.
     *
     * @param roleString a {@link REXROLE} according to
     *                   {@link RexAuthzConstants#TEMPLATE_REX_ROLE}
     * @return {@link REXROLE} if extraction was successful,
     * empty {@link Optional} otherwise.
     */
    private static Optional<REXROLE> getRexRoleFromRoleString(
        final String roleString) {
        Optional<REXROLE> role = Optional.empty();
        String[] roleComponents = roleString.split("_");
        if (roleComponents.length
            == RexAuthzConstants.TEMPLATE_REX_ROLE_COMPONENTS) {
            try {
                role = Optional.of(REXROLE.valueOf(roleComponents[2]));
            } catch (Exception e) {
                LOGGER.error(e.getLocalizedMessage());
            }
        }
        return role;
    }

    /**
     * Extracts the course {@link UUID} given a {@link COURSEROLE} role string.
     *
     * @param roleString a {@link REXROLE} according to
     *                   {@link RexAuthzConstants#TEMPLATE_COURSE_ROLE}
     * @return course {@link UUID} if extraction was successful,
     * empty {@link Optional} otherwise.
     */
    private static Optional<UUID> getCourseIdFromRoleString(
        final String roleString) {
        Optional<UUID> uuid = Optional.empty();
        String[] roleComponents = roleString.split("_");
        if (roleComponents.length
            == RexAuthzConstants.TEMPLATE_COURSE_ROLE_COMPONENTS) {
            try {
                uuid = Optional.of(UUID.fromString(roleComponents[2]));
            } catch (Exception e) {
                LOGGER.error(e.getLocalizedMessage());
            }
        }
        return uuid;
    }

    /**
     * Extracts the course {@link UUID} with the related {@link COURSEROLE}
     * given a {@link COURSEROLE} role string.
     *
     * @param roleString a {@link REXROLE} according to
     *                   {@link RexAuthzConstants#TEMPLATE_COURSE_ROLE}
     * @return a {@link SimpleEntry} of {@link UUID} and {@link COURSEROLE}
     * if extraction was successful, empty {@link Optional} otherwise.
     */
    private static Optional<SimpleEntry<UUID, COURSEROLE>> getCourseIdAndRoleFromRoleString(
        final String roleString) {
        Optional<SimpleEntry<UUID, COURSEROLE>> entry = Optional.empty();
        String[] roleComponents = roleString.split("_");
        if (roleComponents.length
            == RexAuthzConstants.TEMPLATE_COURSE_ROLE_COMPONENTS) {
            try {
                UUID uuid = UUID.fromString(roleComponents[2]);
                COURSEROLE role = COURSEROLE.valueOf(roleComponents[3]);
                entry = Optional.of(new SimpleEntry<>(uuid, role));
            } catch (Exception e) {
                LOGGER.error(e.getLocalizedMessage());
            }
        }
        return entry;
    }

    /**
     * Returns a Set of Strings representing the
     * {@link GrantedAuthority}s of the current user.
     *
     * @return the set of granted authorities
     */
    private static Set<String> getUserAuthorities() {
        return getUserAuthn().getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    /**
     * Checks whether the current user as a specific authority.
     *
     * @param authority a role string
     * @return <code>true</code>/<code>false</code>.
     */
    private static boolean userHasAuthority(final String authority) {
        return getUserAuthorities().contains(authority);
    }

    /**
     * Extracts a claim string from the authz token
     *
     * @param claim the claim name
     * @return the content of the claim as string if extraction was successful,
     * empty {@link Optional} otherwise.
     */
    private static Optional<String> getClaimFromToken(String claim) {
        Optional<String> rtn = Optional.empty();
        Object principal = getUserAuthn().getPrincipal();
        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt)principal;
            rtn = Optional.ofNullable((String) jwt.getClaims().get(claim));
        }
        return rtn;
    }
}
