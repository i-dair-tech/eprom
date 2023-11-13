package com.dqlick.eprom.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String STUDY_COORDINATOR = "ROLE_STUDY_COORDINATOR";

    public static final String PATIENT = "ROLE_PATIENT";

    private AuthoritiesConstants() {}
}
