package com.enrsolidr.energyanalysis.util;

public class SecurityConstants {
    public static final String SECRET = "jscreveJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
    public static final String LOGIN_URL = "/users/generate-token";
    public static final String ENERGY_USAGE = "/energy/usage";
    public static final String MAIL_SEND = "/mail/send";
    public static final String PAYMENT_CHARGE = "/payment/charge";
    public static final String[] ALLOWED_URLS = new String[]{"/users/sign-up", "/users/generate-token", "/energy/usage", "/mail/send", "/payment/charge"};
}