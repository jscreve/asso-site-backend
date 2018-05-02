package com.enrsolidr.energyanalysis.util;

public class SecurityConstants {
    public static final String SECRET = "jscreveJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String[] ALLOWED_URLS = new String[]{"/members/sign-up", "/members/remove/**", "/auth/generate-token", "/energy/usage", "/mail/send", "/payment/charge", "/payment/charge/member", "/payment/charge/newmember"};
    public static final String ADMIN_ROLE = "ROLE_ADMIN";
    public static final String MEMBER_ROLE = "ROLE_MEMBER";

}
