package com.promise.security;

public class SecurityConstants {
  public static final String SIGN_UP_URL = "/api/users/**";
  public static final String H2_URL = "h2-console/**";
  public static final String SECRET = "SecretKeyToGenJWTs";
  public static final String TOKEN = "Bearer ";
  public static final String HEADER_STRING = "Authorization";
  public static final Integer EXPIRATION_TIME = 864_000_000;
}
