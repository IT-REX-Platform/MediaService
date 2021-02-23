package de.uni_stuttgart.it_rex.media.service.written;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    // todo: replace with marcel's implementation so we don't have duplicates of the same stuff
    public static final String ROLE_COURSE_TEMPLATE = "ROLE_COURSE_%1$s_%2$s";

    private enum CourseRole {
        OWNER,
        MANAGER,
        PARTICIPANT
    }

    private static String getCourseRoleAuthorityString(CourseRole role, UUID courseID) {
        return String.format(ROLE_COURSE_TEMPLATE, role.toString(), courseID.toString());
    }

    private static Collection<? extends GrantedAuthority> getUserAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    private static boolean userHasAuthority(String authority) {

        for (GrantedAuthority grantedAuthority : getUserAuthorities()) {
            if (grantedAuthority.getAuthority().equals(authority)) {
                return true;
            }
        }

        return false;
    }

    public static boolean userIsCourseParticipant(UUID courseID) {

        String participantInCourseAuthority = getCourseRoleAuthorityString(CourseRole.PARTICIPANT, courseID);

        return userHasAuthority(participantInCourseAuthority);
    }

    public static boolean userIsCourseParticipantOrManagerOrOwner(UUID courseID) {

        String participantInCourseAuthority = getCourseRoleAuthorityString(CourseRole.PARTICIPANT, courseID);
        String managerInCourseAuthority = getCourseRoleAuthorityString(CourseRole.MANAGER, courseID);
        String ownerInCourseAuthority = getCourseRoleAuthorityString(CourseRole.OWNER, courseID);

        return (userHasAuthority(participantInCourseAuthority) ||
                userHasAuthority(managerInCourseAuthority) ||
                userHasAuthority(ownerInCourseAuthority));
    }

    public static boolean userIsCourseManagerOrOwner(UUID courseID) {

        String managerInCourseAuthority = getCourseRoleAuthorityString(CourseRole.MANAGER, courseID);
        String ownerInCourseAuthority = getCourseRoleAuthorityString(CourseRole.OWNER, courseID);

        return (userHasAuthority(managerInCourseAuthority) ||
                userHasAuthority(ownerInCourseAuthority));
    }

    public static boolean userIsCourseOwner(UUID courseID) {

        String ownerInCourseAuthority = getCourseRoleAuthorityString(CourseRole.OWNER, courseID);

        return userHasAuthority(ownerInCourseAuthority);
    }

    private static UUID extractCourseUUIDFromRoleAuthority(String authority) {

        UUID uuid = null;

        String[] authorityComponents = authority.split("_");
        String courseIDString = authorityComponents[3];

        if (authorityComponents.length == 4) {
            try {
                uuid = UUID.fromString(courseIDString);
            } catch (Error e) {
                e.printStackTrace();
            }
        }

        return uuid;
    }

    public static List<UUID> getCoursesOfUser() {

        ArrayList<UUID> courses = new ArrayList<>();

        Collection<? extends GrantedAuthority> authorities = getUserAuthorities();
        String courseIndicatorString = "ROLE_COURSE_";

        for (GrantedAuthority authority : authorities) {
            String authorityString = authority.getAuthority();

            if (authorityString.contains(courseIndicatorString)) {
                courses.add(extractCourseUUIDFromRoleAuthority(authorityString));
            }
        }

        return courses;
    }
}
