package com.yarmook.realstate.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String AGENT = "ROLE_AGENT";

    /** @deprecated use AGENT instead */
    @Deprecated(forRemoval = false)
    public static final String USER = AGENT;

    public static final String VISITOR = "ROLE_VISITOR";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}
