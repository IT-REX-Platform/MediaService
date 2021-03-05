package de.uni_stuttgart.it_rex.media.domain.written.enumeration;

/**
 * The roles available in a course.
 */
public enum COURSEROLE {
    /**
     * The Owner of a Course.
     * <p>
     * They have all rights to manage a course.
     */
    OWNER,
    /**
     * The Manager of a course.
     * <p>
     * They have extended rights to manage a course.
     */
    MANAGER,
    /**
     * The Participant of a Course.
     * <p>
     * They have basic rights to consume Content offered by a Course
     */
    PARTICIPANT
}
